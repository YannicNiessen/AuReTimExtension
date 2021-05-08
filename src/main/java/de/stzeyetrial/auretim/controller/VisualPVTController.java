package de.stzeyetrial.auretim.controller;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.AbstractRunnerTask;
import de.stzeyetrial.auretim.tasks.NoGoRunnerTask;
import de.stzeyetrial.auretim.tasks.RunnerTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * FXML Controller class
 *
 * @author strasser
 */
public class VisualPVTController extends AbstractController {
	private final ExecutorService _executor;

	private Timeline _timelineGreen;
	private Timeline _timelineRed;
	private ResourceBundle _resources;
	private Future<?> _future;

	@FXML
	private Button _startButton;

	@FXML
	private Button _backButton;

	@FXML
	private Button _cancelButton;

	@FXML
	private Button _endButton;

	@FXML
	private ProgressBar _progressBar;

	@FXML
	private TextField _responseTimeTextField;

	@FXML
	private Circle _indicator;

	@FXML
	private TextField _volumeTextField;

	@FXML
	private Button _lowerVolumeButton;

	@FXML
	private Button _higherVolumeButton;

	@FXML
	private HBox _stimulusContainerBox;

	private Rectangle _stimulusRectangle;

	public VisualPVTController() {
		_executor = Executors.newSingleThreadExecutor(r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			_executor.shutdown();
		}));
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		_resources = resources;

		_timelineGreen = new Timeline();
		_timelineGreen.setCycleCount(1);
		_timelineGreen.setAutoReverse(true);
		_timelineGreen.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(_indicator.fillProperty(), Color.GREEN)));
		_timelineGreen.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(_indicator.fillProperty(), Color.web("0x9f9f9f"))));

		_timelineRed = new Timeline();
		_timelineRed.setCycleCount(1);
		_timelineRed.setAutoReverse(true);
		_timelineRed.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(_indicator.fillProperty(), Color.RED)));
		_timelineRed.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(_indicator.fillProperty(), Color.web("0x9f9f9f"))));


		double availableSpace = Math.min(_stimulusContainerBox.getPrefHeight(), _stimulusContainerBox.getPrefWidth());
		_stimulusRectangle = new Rectangle(availableSpace, availableSpace);
		_stimulusRectangle.setFill(Paint.valueOf("white"));
		_stimulusContainerBox.getChildren().add(_stimulusRectangle);

	}

	@Override
	public void enter() {
		unbind();
		_responseTimeTextField.textProperty().set("");
		_progressBar.progressProperty().setValue(0);
	}

	@Override
	public void leave() {
		unbind();
	}

	@FXML
	private void start() {
		unbind();

		final int frequency				= Config.getInstance().visualPVTfrequencyProperty().get();
		final int pulseDuration			= Config.getInstance().visualPVTpulseDurationProperty().get();
		final int minimumResponseTime	= Config.getInstance().visualPVTminimumResponseTimeProperty().get();
		final int minimumDelay			= Config.getInstance().visualPVTminimumDelayProperty().get();
		final int maximumDelay			= Config.getInstance().visualPVTmaximumDelayProperty().get();
		final int timeout				= Config.getInstance().visualPVTtimeoutProperty().get();
		final int repetitions			= Config.getInstance().visualPVTrepetitionsProperty().get();

		final List<Result> results = Session.getCurrentSession().getResults();
		results.clear();

		final AbstractRunnerTask task = (Config.getInstance().visualPVTuseNoGoProperty().get())
			? new NoGoRunnerTask(results, frequency, Config.getInstance().visualPVTvolumeProperty(), pulseDuration, minimumResponseTime, minimumDelay, timeout, repetitions, true)
			: new RunnerTask(results, frequency, Config.getInstance().visualPVTvolumeProperty(), pulseDuration, minimumResponseTime, minimumDelay, maximumDelay, timeout, repetitions, true)
		;
		task.setOnSucceeded(event -> getScreenManager().setScreen(Screens.RESULT));
		//task.setOnFailed(event -> getScreenManager().showException(task.getException()));
		bind(task);
		_future = _executor.submit(task);
	}

	@FXML
	private void cancel() {
		if (_future != null) {
			_future.cancel(true);
			TriggerFactory.getInstance().createTrigger().trigger(TriggerType.END_TEST);
		}

		_endButton.disableProperty().set(Session.getCurrentSession().getResults().stream().filter(r -> r.getType() == Result.Type.TRUE_POSITIVE).count() < 3);
	}

	@FXML
	private void end() {
		unbind();
		getScreenManager().setScreen(Screens.RESULT);
	}

	@FXML
	private void buttonBack() {
		if (_backButton.disableProperty().not().get()) {
			Session.getCurrentSession().clearResults();
			getScreenManager().setScreen(Screens.MAIN);
		}
	}

	private void unbind() {

		_startButton.disableProperty().unbind();
		_backButton.disableProperty().unbind();
		_progressBar.progressProperty().unbind();
		_responseTimeTextField.textProperty().unbind();
		_cancelButton.disableProperty().unbind();
	}

	private void bind(final AbstractRunnerTask task) {

		_startButton.disableProperty().bind(task.runningProperty());
		_backButton.disableProperty().bind(task.runningProperty());
		_cancelButton.disableProperty().bind(task.runningProperty().not());
		_progressBar.progressProperty().bind(task.progressProperty());
		_responseTimeTextField.textProperty().bind(new StringBinding() {
			{
				bind(task.currentResultProperty());
			}
			@Override
			protected String computeValue() {
				final Result result = task.currentResultProperty().get();
				if (result == null) {
					return "";
				} else {
					if (Result.Type.FALSE_NEGATIVE == result.getType()) {
						return _resources.getString("false_negative");
					} else if (Result.Type.FALSE_POSITIVE == result.getType()) {
						_timelineRed.play();
						return _resources.getString("false_positive");
					} else {
						_timelineGreen.play();
						return String.format("%d ms", result.getDuration());
					}
				}
			}
		});

		task.currentStimulusProperty().addListener((observableValue, stimulus, t1) -> {
				if (stimulus.isUnreal())
					return;
				Platform.runLater(() -> {
						try {
							outputStimulus(t1);
							task.gate.await();
						} catch (InterruptedException | BrokenBarrierException e) {
							e.printStackTrace();
						}

				});

				Runnable r = () -> {
					try {
						Thread.sleep(Config.getInstance().visualPVTpulseDurationProperty().getValue());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Platform.runLater(() -> outputStimulus(Stimulus.unrealStimulus()));
				};

				(new Thread(r)).start();
		});
	}


	private void outputStimulus(Stimulus stimulus){
		if (stimulus.isGo()){
			_stimulusRectangle.setFill(Color.web(Config.getInstance().visualPVTgoColorProperty().getValue()));
		}else if (stimulus.isNoGo()){
			_stimulusRectangle.setFill(Color.web(Config.getInstance().visualPVTnoGoColorProperty().getValue()));
		}else if(stimulus.isUnreal()){
			_stimulusRectangle.setFill(Paint.valueOf("white"));
		}
	}
}