package de.stzeyetrial.auretim.controller;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.MackworthClockTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * FXML Controller class
 *
 * @author niessen
 */
public class MackworthClockTestController extends AbstractController {
	protected final ExecutorService _executor;

	private Timeline _timelineGreen;
	private Timeline _timelineRed;
	private ResourceBundle _resources;
	protected Future<?> _future;

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
	protected AnchorPane anchorPane;

	private ImageView _imageView;

	private long ts;

	@FXML
	private HBox _stimulusContainerBox;

	Stimulus.Type _stimulusType;

	private int _currentIndex;

	private int _nCircles = 24;

	public MackworthClockTestController() {
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
		setConfig();
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
	protected void start() {
		unbind();

		final int length				= Config.getInstance().mackworthLengthProperty().get();
		final int nMatch				= Config.getInstance().mackworthTargetsProperty().get();
		final int timeout				= Config.getInstance().mackworthIntervalProperty().get();
		final int minimumResponseTime	= Config.getInstance().minimumResponseTimeProperty().get();

		final List<Result> results = Session.getCurrentSession().getResults();
		results.clear();

		_currentIndex = 0;
		clearCircles();

		final MackworthClockTask task = new MackworthClockTask(results, length, nMatch, timeout, minimumResponseTime);
		//final AbstractNBackSpeechTask task = new NBackSpeechTask(results, length, nRepeat, nMatch, nLures, nBackLevel, timeout, reUseElements, minimumResponseTime);

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

	protected void unbind() {
		_startButton.disableProperty().unbind();
		_backButton.disableProperty().unbind();
		_progressBar.progressProperty().unbind();
		_responseTimeTextField.textProperty().unbind();
		_cancelButton.disableProperty().unbind();
	}

	protected void bind(final MackworthClockTask task) {
		_startButton.disableProperty().bind(task.runningProperty());
		_backButton.disableProperty().bind(task.runningProperty());
		_cancelButton.disableProperty().bind(task.runningProperty().not());
		_progressBar.progressProperty().bind(task.progressProperty());
		_responseTimeTextField.textProperty().bind(new StringBinding() {
			{
				bind(task.currentResultStringProperty());
			}

			@Override
			protected String computeValue() {

				final String result = task.currentResultStringProperty().get();
					if (result == null){
						return "";
					}
					if (result == "wrong") {
						_timelineRed.play();
					} else if (result == "right") {
						_timelineGreen.play();
					}
					return result;
				}

		});
		task.currentStimulusProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
				System.out.println("stimulus changed to " + t1);
				if (t1 == null)
					return;
				Runnable r = new Runnable() {
					@Override
					public void run() {
						try{
							outputStimulus(t1);
						}catch(Exception e){
							e.printStackTrace();
							//TODO
						}
						System.out.println(task.gate.getNumberWaiting());
						try {
							task.gate.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (BrokenBarrierException e) {
							e.printStackTrace();
						}
						System.out.println("here is reached");

					}
				};
				Platform.runLater(r);


			}
		});

		task.currentStimulusProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean stimulus, Boolean t1) {

			}});


	}

	protected void setLayout(){
		if (_stimulusContainerBox.getChildren().size() > 0) {
			((Pane) _stimulusContainerBox.getChildren().get(0)).getChildren().clear();
		}

			_stimulusContainerBox.getChildren().clear();
		_stimulusContainerBox.setLayoutX(0);
		_stimulusContainerBox.setLayoutY(0);
		double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());
		double centerX = _stimulusContainerBox.getLayoutX() + _stimulusContainerBox.getPrefWidth() / 2;
		double centerY = _stimulusContainerBox.getLayoutY() + _stimulusContainerBox.getPrefHeight() / 2;
		System.out.println("Pref Height:" + _stimulusContainerBox.getPrefHeight());
		Pane p = new Pane();
		p.setPrefWidth(_stimulusContainerBox.getPrefWidth());
		p.setPrefHeight(_stimulusContainerBox.getPrefHeight());
		_stimulusContainerBox.getChildren().add(p);
		System.out.println("Available Space = " + availableSpace);
		System.out.println("Center X = " + centerX);
		System.out.println("Center Y = " + centerY);

		double spacing = Math.sqrt(Math.pow(((centerX + 100 * Math.cos(0)) - (centerX + 100 * Math.cos((2 * Math.PI) / _nCircles))), 2) + Math.pow(((centerY + 100 * Math.sin(0)) - (centerY + 100 * Math.sin((2 * Math.PI) / _nCircles))), 2));

		for (int i = 0; i < _nCircles; i++) {

			Circle c = new Circle();
			double angle = (2 * Math.PI) / _nCircles * i;
			c.setCenterX(centerX + 100 * Math.cos(angle));
			c.setCenterY(centerY + 100 * Math.sin(angle));
			c.setRadius((spacing / 2) * 0.9);
			c.setFill(Color.WHITE);
			p.getChildren().add(c);

		}

	};

	protected void outputStimulus(Boolean stimulus){

		System.out.println(System.currentTimeMillis() - ts);

		Pane p = (Pane) _stimulusContainerBox.getChildren().get(0);

		int nextIndex = (_currentIndex + (stimulus ? 2 : 1)) % _nCircles;


		((Circle) p.getChildren().get(_currentIndex)).setFill(Color.WHITE);
		((Circle) p.getChildren().get(nextIndex)).setFill(Color.GREEN);

		_currentIndex = nextIndex;
		System.out.println(System.currentTimeMillis() - ts);

		ts = System.currentTimeMillis();
	};

	private void clearCircles(){

		if (_stimulusContainerBox.getChildren().size() > 0){
			Pane p = (Pane) _stimulusContainerBox.getChildren().get(0);
			for (int i = 0; i < p.getChildren().size(); i++) {
				((Circle) p.getChildren().get(i)).setFill(Color.WHITE);
			}
		}
	}

	public void setConfig(){
		clearCircles();
			_nCircles = Config.getInstance().mackworthNCirclesProperty().getValue();

			setLayout();
	}

}