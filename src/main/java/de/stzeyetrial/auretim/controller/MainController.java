package de.stzeyetrial.auretim.controller;

import com.sun.javafx.scene.control.skin.FXVKSkin;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.input.SpeechDecoder;
import de.stzeyetrial.auretim.screens.ScreenManager;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.util.EnterSubmitHandler;
import de.stzeyetrial.auretim.util.PreferencesMap;

import java.io.IOException;
import java.net.URL;
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
		_testTextField.textProperty().set(Integer.toString(1));
		_testTextField.getProperties().put("vkType", 1);
		_testTextField.setOnKeyPressed(new EnterSubmitHandler());
		_subjectTextField.getProperties().put("vkType", 0);
		_subjectTextField.setOnKeyPressed(new EnterSubmitHandler());

		_testComboBox.itemsProperty().get().add("nBack_Visual_Identity");
		_testComboBox.itemsProperty().get().add("nBack_Visual_Location");
		_testComboBox.itemsProperty().get().add("nBack_Auditive");
		_testComboBox.itemsProperty().get().add("Mackworth Clock");
		_testComboBox.itemsProperty().get().add("PVT");
		_testComboBox.itemsProperty().get().add("Visual Identity & Auditive Dual-nBack");
		_testComboBox.itemsProperty().get().add("Visual Location & Auditive Dual-nBack");
		_testComboBox.itemsProperty().get().add("Visual Location & Identity Dual-nBack");
		_testComboBox.itemsProperty().get().add("Spatial Working Memory Update Test");
		_testComboBox.getSelectionModel().select("nBack_Visual_Identity");


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

	@FXML
	private void test(final ActionEvent e) {
		if (!_validation.isInvalid()) {
			Session.newSession(_subjectTextField.getText(), _testTextField.getText());
			String testType = _testComboBox.getValue();

			if (testType.equals("nBack_Visual_Location")){
				getScreenManager().setScreen(Screens.N_BACK_TEST_VISUAL_LOCATION);
			}else if (testType.equals("nBack_Visual_Identity")){
				getScreenManager().setScreen(Screens.N_BACK_TEST_VISUAL_IDENTITY);
			} else if (testType.equals("nBack_Auditive")) {
				getScreenManager().setScreen(Screens.N_BACK_TEST_AUDITIVE);
			}else if (testType.equals("Mackworth Clock")) {
				getScreenManager().setScreen(Screens.MACKWORTH_CLOCK_TEST);
			}else if (testType.equals("Visual Identity & Auditive Dual-nBack")){
				getScreenManager().setScreen(Screens.N_BACK_TEST_VISUAL_IDENTITY_AUDITIVE_DUAL);
			}else if (testType.equals("Visual Location & Auditive Dual-nBack")){
				getScreenManager().setScreen(Screens.N_BACK_TEST_VISUAL_LOCATION_AUDITIVE_DUAL);
			}else if (testType.equals("Visual Location & Identity Dual-nBack")){
				getScreenManager().setScreen(Screens.N_BACK_TEST_VISUAL_LOCATION_IDENTITY_DUAL);
			}else if (testType.equals("Spatial Working Memory Update Test")){
				getScreenManager().setScreen(Screens.SPATIAL_WORKING_MEMORY_UPDATE_TEST);
			}
			else if (testType.equals("PVT")){
				getScreenManager().setScreen(Screens.TEST);
			}

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