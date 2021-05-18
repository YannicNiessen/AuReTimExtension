package de.stzeyetrial.auretim.controller;

import com.sun.javafx.scene.control.skin.FXVK;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.SpatialWorkingMemoryUpdateTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

/**
 * FXML Controller class
 *
 * @author niessen
 */
public class SpatialWorkingMemoryUpdatingTestController extends AbstractController {
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

	@FXML
	protected HBox _stimulusContainerBox;

	Stimulus.Type _stimulusType;

	protected int roundCounter;

	protected int frameCount;

	List<Result> _results;

	private Stimulus[] lastStimulus;

	private HBox[] frames;
	Node containerNode;

	public SpatialWorkingMemoryUpdatingTestController() {
		_executor = Executors.newSingleThreadExecutor(r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(_executor::shutdown));
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
		setLayout();
		roundCounter = 0;
		final int length				= Config.getInstance().spatialWorkingMemoryRepetitionsProperty().get();
		final int interval				= Config.getInstance().spatialWorkingMemoryIntervalProperty().get();
		final int initialDelay 			= Config.getInstance().spatialWorkingMemoryInitialDelayProperty().get();


		_results = Session.getCurrentSession().getResults();
		_results.clear();

		final SpatialWorkingMemoryUpdateTask task = new SpatialWorkingMemoryUpdateTask(_results, length, frameCount, initialDelay, interval);

		//task.setOnSucceeded(event -> getScreenManager().setScreen(Screens.RESULT));
		task.setOnSucceeded(event -> getUserInput());
		//task.setOnFailed(event -> getScreenManager().showException(task.getException()));
		bind(task);
		_future = _executor.submit(task);
	}


	private void modifyVirtualKeyboard(){
		Window popupWindow = getPopupWindow();

		Node vkParent = popupWindow.getScene().getRoot().getChildrenUnmodifiable().get(0);

		Object[] keys = vkParent.lookupAll(".key").toArray();

		Node key3 = (Node) keys[3];
		Node key7 = (Node) keys[7];
		Node key11 = (Node) keys[11];
		Node key12 = (Node) keys[12];
		Node key13 = (Node) keys[13];
		Node key14 = (Node) keys[14];
		Node key15 = (Node) keys[15];
		Node key16 = (Node) keys[16];

		key3.setVisible(false);
		key3.setManaged(false);
		key7.setVisible(false);
		key7.setManaged(false);
		key11.setVisible(false);
		key11.setManaged(false);
		key12.setVisible(false);
		key12.setManaged(false);
		key13.setVisible(false);
		key13.setManaged(false);
		key14.setVisible(false);
		key14.setManaged(false);
		key15.setVisible(false);
		key15.setManaged(false);
		key16.setManaged(false);
		key16.setManaged(false);

		FXVK vk = (FXVK) vkParent.lookup(".fxvk");
		vk.setPrefHeight(320);
		vk.setPrefWidth(635);
	}

	private void restoreVirtualKeyboard(){
		Window popupWindow = getPopupWindow();

		Node vkParent = popupWindow.getScene().getRoot().getChildrenUnmodifiable().get(0);

		Object[] keys = vkParent.lookupAll(".key").toArray();

		for (int i = 0; i < keys.length; i++) {

			((Node) keys[i]).setVisible(true);
			((Node) keys[i]).setManaged(true);

		}
		FXVK vk = (FXVK) vkParent.lookup(".fxvk");
		vk.setPrefHeight(230);
		vk.setPrefWidth(450);
	}


	private void getUserInput() {

			if(roundCounter < Config.getInstance().spatialWorkingMemoryRepetitionsProperty().get() * frameCount - 1)
				return;

			List<Integer> frameProbingOrder = new LinkedList<>();

			while(frameProbingOrder.size() < frameCount){
				int index = (int) (Math.random() * frameCount);
				if(!frameProbingOrder.contains(index)){
					frameProbingOrder.add(index);
				}
			}

			clearFrames();
			((TextField)frames[frameProbingOrder.get(0)].getChildren().get(0)).setText("?");
			TextField inputTextField = new TextField();
			inputTextField.setOnKeyTyped(keyEvent -> {
				String userResponse = keyEvent.getCharacter();

				boolean answerCorrect = lastStimulus[frameProbingOrder.get(_results.size())].get_values()[0] + 1 == Integer.parseInt(userResponse);

				_results.add(new Result(System.currentTimeMillis(), 0, answerCorrect ? Result.Type.TRUE_POSITIVE : Result.Type.FALSE_POSITIVE));
				keyEvent.consume();
				inputTextField.setText("");
				((TextField)frames[frameProbingOrder.get(_results.size()-1)].getChildren().get(0)).setText(userResponse);

				if (_results.size() >= frameCount) {
					restoreVirtualKeyboard();
					FXVK.detach();
					_stimulusContainerBox.getChildren().remove(inputTextField);

					getScreenManager().setScreen(Screens.RESULT);
				}else{
					((TextField)frames[frameProbingOrder.get(_results.size())].getChildren().get(0)).setText("?");
				}
			});
			inputTextField.getProperties().put("vkType", 1);
			inputTextField.setMaxHeight(0);
			inputTextField.setMaxWidth(0);
			inputTextField.setMinWidth(0);
			inputTextField.setMinHeight(0);
			inputTextField.setPrefHeight(0);
			inputTextField.setPrefWidth(0);
			_stimulusContainerBox.getChildren().add(inputTextField);


			Platform.runLater(() -> {
				inputTextField.requestFocus();
				inputTextField.setVisible(true);
				FXVK.init(inputTextField);
				FXVK.attach(inputTextField);
				modifyVirtualKeyboard();
			});


	}

	protected void setLayout() {
		lastStimulus = new Stimulus[frameCount];
		roundCounter = 0;
		frames = new HBox[frameCount];
		_stimulusContainerBox.getChildren().clear();

		double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth() / frameCount, _stimulusContainerBox.getPrefHeight());

		for (int i = 0; i < frameCount; i++) {

			frames[i] = new HBox();
			frames[i].setPrefWidth(availableSpace);
			frames[i].setPrefHeight(availableSpace);
			frames[i].setMaxHeight(availableSpace);
			frames[i].setMaxWidth(availableSpace);
			frames[i].setStyle("-fx-border-color: white");
			_stimulusContainerBox.getChildren().add(frames[i]);
		}


		for (int i = 0; i < frameCount; i++) {

			int rowNumber = 3;
			GridPane stimulusGridPane = new GridPane();

			double blockSize = (availableSpace * 1) / rowNumber;

			for (int column = 0; column < rowNumber; column++) {
				for (int row = 0; row < rowNumber; row++) {
					Rectangle rect = new Rectangle(blockSize, blockSize);
					rect.setFill(Paint.valueOf("#2F3439"));

					stimulusGridPane.add(rect, row, column);
				}
			}
			stimulusGridPane.setMaxHeight(availableSpace);
			stimulusGridPane.setMaxWidth(availableSpace);
			stimulusGridPane.setPrefHeight(availableSpace);
			stimulusGridPane.setPrefWidth(availableSpace);
			stimulusGridPane.setHgap(1);
			stimulusGridPane.setVgap(1);
			stimulusGridPane.setStyle("-fx-background-color: white;");

			frames[i].getChildren().add(stimulusGridPane);

		}
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

	protected void bind(final SpatialWorkingMemoryUpdateTask task) {
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
						try{
							outputStimulus(t1);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				};
				Platform.runLater(r);
			}
		});


	}


	public void setConfig(){
		frameCount = Config.getInstance().spatialWorkingMemoryFramesProperty().getValue();
		setLayout();
	}


	private void clearFrames(){

		for (int i = 0; i < frameCount; i++) {


			HBox frame = frames[i];

			Node contentNode = frame.getChildren().get(0);

			if(contentNode instanceof TextField){
				((TextField) contentNode).setText("");
			}else if(contentNode instanceof GridPane){
				GridPane grid = (GridPane) contentNode;
				for (int j = 0; j < grid.getChildren().size(); j++) {
					((Rectangle) grid.getChildren().get(j)).setFill(Paint.valueOf("#2F3439"));
				}


			}

		}

	}

	private void changeGridToText(){
		for (int i = 0; i < frameCount; i++) {
			frames[i].getChildren().clear();
			double availableSpace= Math.min(frames[i].getPrefWidth(), frames[i].getPrefHeight());
			containerNode = Stimulus.getContainerNode(Stimulus.Type.LETTER, availableSpace, availableSpace);
			((TextField) containerNode).setMaxHeight(availableSpace);
			((TextField) containerNode).setMaxWidth(availableSpace);
			((TextField) containerNode).setPrefWidth(availableSpace);
			((TextField) containerNode).setPrefHeight(availableSpace);
			containerNode.setStyle("-fx-background-color: -dark;-fx-text-fill: white;");
			frames[i].getChildren().add(containerNode);
		}
	}

	protected void outputStimulus(Stimulus stimulus) {


		int value;

		if (stimulus.get_values().length != 0){
			value = stimulus.get_values()[0];
		}else{
			return;
		}

		clearFrames();

		int frameIndex = roundCounter % frameCount;

		HBox currentFrame = frames[frameIndex];

		if (roundCounter < frameCount){

			GridPane grid = (GridPane) currentFrame.getChildren().get(0);

			FillTransition fillBlue = new FillTransition(Duration.millis(100),((Rectangle) grid.getChildren().get(value)) ,Color.WHITE, Color.web(Config.getInstance().spatialWorkingMemoryColorProperty().getValue()));

			fillBlue.play();

		}else{
			if (roundCounter  == frameCount){
				changeGridToText();
			}
			TextField textField = (TextField) currentFrame.getChildren().get(0);
			String directionArrow;
			switch (lastStimulus[frameIndex].get_values()[0] - value){
				case -4:
					directionArrow = "↘";
					break;
				case -3:
					directionArrow = "↓";
					break;
				case -2:
					directionArrow = "↙";
					break;
				case -1:
					directionArrow = "→";
					break;
				case 1:
					directionArrow = "←";
					break;
				case 2:
					directionArrow = "↗";
					break;
				case 3:
					directionArrow = "↑";
					break;
				case 4:
					directionArrow = "↖";
					break;
				default:
					directionArrow = "";
			}
			textField.setText(directionArrow);

		}

		lastStimulus[frameIndex] = stimulus;
		roundCounter++;

	}

	private PopupWindow getPopupWindow() {

		final ObservableList<Window> windows = Window.getWindows();

		for (int i = 0; i < windows.size(); i++) {
			final Window window = windows.get(i);
			if (window instanceof PopupWindow) {
				if (window.getScene() != null && window.getScene().getRoot() != null) {
					Parent root = window.getScene().getRoot();
					if (root.getChildrenUnmodifiable().size() > 0) {
						Node popup = root.getChildrenUnmodifiable().get(0);
						if (popup.lookup(".fxvk") != null) {
							FXVK vk = (FXVK) popup.lookup(".fxvk");
							return (PopupWindow) window;
						}
					}
				}
				return null;
			}
		}
		return null;
	}

}