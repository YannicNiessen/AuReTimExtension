package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.controller.AbstractController;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.*;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * FXML Controller class
 *
 * @author niessen
 */
public abstract class AbstractNBackTestController extends AbstractController {
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

	Stimulus.Type _stimulusType;

	public AbstractNBackTestController() {
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
	protected abstract void start();

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

	protected void bind(final AbstractNBackTask task) {
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
		task.currentStimulusProperty().addListener(new ChangeListener<Stimulus>() {
			@Override
			public void changed(ObservableValue<? extends Stimulus> observableValue, Stimulus stimulus, Stimulus t1) {
				if (t1.isUnreal())
					return;
				Runnable r = new Runnable() {
					@Override
					public void run() {
						try {
							outputStimulus(t1);
							task.gate.await();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				Platform.runLater(r); //Execute on UI Thread / Application Thread
			}
		});


	}


	public void setConfig(){
		_stimulusType = Stimulus.Type.valueOf(Config.getInstance().visualIdentityStimulusTypeProperty().getValue());
		setLayout();
	}

	protected abstract void setLayout();

	protected abstract void outputStimulus(Stimulus stimulus) throws Exception;

}