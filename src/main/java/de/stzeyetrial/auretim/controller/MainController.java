package de.stzeyetrial.auretim.controller;

import com.sun.javafx.scene.control.skin.FXVKSkin;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.input.SpeechDecoder;
import de.stzeyetrial.auretim.screens.ScreenManager;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.util.*;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javax.sound.sampled.LineUnavailableException;

public class MainController extends AbstractController {
	@FXML
	private TextField _subjectTextField;

	@FXML
	private TextField _testTextField;

	private final ValidationSupport _validation = new ValidationSupport();

	@FXML
	private ComboBox<String> _testComboBox;
    
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
		_testTextField.getProperties().put("vkType", 1);
		_testTextField.setOnKeyPressed(new EnterSubmitHandler());
		_subjectTextField.getProperties().put("vkType", 0);
		_subjectTextField.setOnKeyPressed(new EnterSubmitHandler());

		for(TestType t : TestType.values()){
			_testComboBox.getItems().add(t.name());
		}

		for(TestSequence t: TestSequence.getLoadedSets()){
			_testComboBox.getItems().add(t.get_name());
		}

		_testComboBox.getSelectionModel().select(TestType.PVT_AUDITORY.name());


		_validation.registerValidator(_testTextField, false, Validator.createEmptyValidator(rb.getString("emptyTestId.text")));
		_validation.registerValidator(_subjectTextField, false, Validator.createEmptyValidator(rb.getString("emptySubjectId.text")));
		
		final PreferencesMap p = new PreferencesMap(10, "subjects");
		TextFields.bindAutoCompletion(_subjectTextField, (AutoCompletionBinding.ISuggestionRequest param) -> {
			if (!param.isCancelled() && Config.getInstance().useAutoCompletionProperty().get()) {
				return p.keySet().stream().filter(text -> text.startsWith(param.getUserText())).collect(Collectors.toList());
			} else {
				return null;
			}
		}).setOnAutoCompleted((event) -> {
			final Integer value = p.get(event.getCompletion());
			try {
				_testTextField.textProperty().setValue(Integer.toString(value));
			} catch (NumberFormatException e) {
			}
		});


    }

    public void setAvailableTests(){
    	_testComboBox.getItems().clear();
		for(TestType t : TestType.values()){
			_testComboBox.getItems().add(t.name());
		}

		for(TestSequence t: TestSequence.getLoadedSets()){
			_testComboBox.getItems().add(t.get_name());
		}

		_testComboBox.getSelectionModel().select(TestType.PVT_AUDITORY.name());
	}

	@FXML
	private void test(final ActionEvent e) {
		if (!_validation.isInvalid()) {

			String testType = _testComboBox.getValue();

			Queue<TestType> testTypeQueue = new LinkedList<>();

			if(TestType.contains(testType))
				testTypeQueue.add(TestType.valueOf(testType));
			else {
				for(String testTypeName: TestSequence.getSet(testType).get_elements()){
					testTypeQueue.add(TestType.valueOf(testTypeName));
				}
			}

			Session.newSession(_subjectTextField.getText(), _testTextField.getText(),testTypeQueue.peek(), testTypeQueue);

			getScreenManager().setScreen(Screens.valueOf(testTypeQueue.poll().name()));

		} else {
			_validation.initInitialDecoration();
		}
	}

	@FXML
	private void settings(final ActionEvent e) {
		getScreenManager().setScreen(Screens.SETTINGS);
	}

	@FXML
	private void exit(final ActionEvent e) {
		getScreenManager().showExitConfirmationDialog();
	}

	@Override
	public void enter() {

	}
}