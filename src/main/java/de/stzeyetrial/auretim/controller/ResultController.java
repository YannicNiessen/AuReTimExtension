package de.stzeyetrial.auretim.controller;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.config.ConfigMeta;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.util.PreferencesMap;
import de.stzeyetrial.auretim.util.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

import de.stzeyetrial.auretim.util.Test;
import de.stzeyetrial.auretim.util.TestType;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javax.swing.*;

/**
 * FXML Controller class
 *
 * @author strasser
 */
public class ResultController extends AbstractBackSupportController {
	private final DoubleProperty _mean = new SimpleDoubleProperty();
	private final DoubleProperty _sd = new SimpleDoubleProperty();
	private final DoubleProperty _median = new SimpleDoubleProperty();
	private final DoubleProperty _q25 = new SimpleDoubleProperty();
	private final DoubleProperty _q75 = new SimpleDoubleProperty();
	private final DoubleProperty _q10 = new SimpleDoubleProperty();
	private final DoubleProperty _q90 = new SimpleDoubleProperty();
	private final IntegerProperty _max = new SimpleIntegerProperty();
	private final IntegerProperty _min = new SimpleIntegerProperty();
	private final IntegerProperty _n = new SimpleIntegerProperty();
	private final IntegerProperty _total = new SimpleIntegerProperty();
	private final IntegerProperty _misses = new SimpleIntegerProperty();
	private final IntegerProperty _hits = new SimpleIntegerProperty();
	private final IntegerProperty _correctRejections = new SimpleIntegerProperty();
	private final IntegerProperty _falsePositives = new SimpleIntegerProperty();
	private final DoubleProperty _cv = new SimpleDoubleProperty();
	private final DoubleProperty _reactionSpeed = new SimpleDoubleProperty();
	private final DoubleProperty _performanceScore = new SimpleDoubleProperty();
	private final DoubleProperty _lapseProbability = new SimpleDoubleProperty();

	@FXML
	private Button _saveButton;

	@FXML
	private TextField _meanSDTextfield;

	@FXML
	private TextField _totalTextfield;

	@FXML
	private TextField _minMaxTextField;

	@FXML
	private TextField _percentilesTextField;

	@FXML
	private TextField _cvTextField;

	@FXML
	private TextField _q10TextField;

	@FXML
	private TextField _q90TextField;

	@FXML
	private TextField _reactionSpeedTextField;

	@FXML
	private TextField _performanceScoreTextField;

	@FXML
	private TextField _lapseProbabilityTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_meanSDTextfield.textProperty().bind(Bindings.concat(_mean.asString("%.1f"), " ± ", _sd.asString("%.1f"), " ms"));
		_totalTextfield.textProperty().bind(Bindings.concat(_total.asString(), " / ", _hits.asString()," / ", _correctRejections.asString(), " / ", _misses.asString(), " / ", _falsePositives.asString()));
		_minMaxTextField.textProperty().bind(Bindings.concat("[", _min.asString(), ", ", _max.asString(), "] ms"));
		_percentilesTextField.textProperty().bind(Bindings.concat(_median.asString("%.1f"), " [", _q25.asString("%.1f"), ", ", _q75.asString("%.1f"), "] ms"));
		_q10TextField.textProperty().bind(_q10.asString());
		_q90TextField.textProperty().bind(_q90.asString());
		_reactionSpeedTextField.textProperty().bind(_reactionSpeed.asString());
		_performanceScoreTextField.textProperty().bind(_performanceScore.asString());
		_lapseProbabilityTextField.textProperty().bind(_lapseProbability.asString());
		_cvTextField.textProperty().bind(_cv.asString("%.4f"));
	}

	@Override
	public void enter() {
		_saveButton.disableProperty().set(false);

		final PreferencesMap p = new PreferencesMap(10, "subjects");
		p.putIfAbsent(Session.getCurrentSession().getSubjectId(), Integer.valueOf(Session.getCurrentSession().getTestId()));
		p.save();

		final List<Result> results = Session.getCurrentSession().getResults();

		final int total = results.size();
		double hits = 0;
		double falseAlarms = 0;
		double misses = 0;
		double correctRejections = 0;

		final DescriptiveStatistics stats = new DescriptiveStatistics();
		for (final Result result : results) {
			switch (result.getType()) {
				case TRUE_POSITIVE:
					if ((Session.getCurrentSession().getTestType() == TestType.PVT_AUDITORY) && ((!Config.getInstance().auditoryPVTuseNoGoProperty().get()) || (result.getDuration() < Config.getInstance().auditoryPVTtimeoutProperty().get()))) {
						stats.addValue(result.getDuration());
					}else if((Session.getCurrentSession().getTestType() == TestType.PVT_VISUAL) && ((!Config.getInstance().visualPVTuseNoGoProperty().get()) || (result.getDuration() < Config.getInstance().visualPVTtimeoutProperty().get()))){
						stats.addValue(result.getDuration());
					}else if(Session.getCurrentSession().getTestType() != TestType.PVT_VISUAL || Session.getCurrentSession().getTestType() != TestType.PVT_AUDITORY){
						stats.addValue(result.getDuration());
					}
					hits++;
					break;
				case TRUE_NEGATIVE:
					correctRejections++;
					break;
				case FALSE_POSITIVE:
					falseAlarms++;
					break;
				case FALSE_NEGATIVE:
					misses++;
					break;
			}
		}

		final double cv = stats.getStandardDeviation() / stats.getMean();

		_mean.setValue(stats.getMean());
		_sd.setValue(stats.getStandardDeviation());
		_median.setValue(stats.getPercentile(50));
		_q25.setValue(stats.getPercentile(25));
		_q75.setValue(stats.getPercentile(75));
		_max.setValue(stats.getMax());
		_min.setValue(stats.getMin());
		_n.setValue(stats.getN());
		_total.setValue(total);
		_cv.setValue(cv);
		_misses.setValue(misses);
		_hits.setValue(hits);
		_correctRejections.setValue(correctRejections);
		_falsePositives.setValue(falseAlarms);
		_q10.setValue(stats.getPercentile(10));
		_q90.setValue(stats.getPercentile(90));
		_reactionSpeed.setValue(1000 / _mean.getValue());
		_performanceScore.setValue(1 - (misses + falseAlarms) / results.size());
		_lapseProbability.setValue(misses / results.size());

		Queue<TestType> testTypeQueue = Session.getCurrentSession().getTestQueue();
		if (!testTypeQueue.isEmpty()){
			TestType nextTest = testTypeQueue.poll();

			try {
				buttonSave(null, true);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Session.getCurrentSession().setTestType(nextTest);
			Session.getCurrentSession().clearResults();
			getScreenManager().setScreen(Screens.valueOf(nextTest.name()));

		}

	}

	@Override
	protected void back() {
		getScreenManager().setScreen(Screens.MAIN);
	}

	@FXML
	private void buttonSave(final ActionEvent e, boolean quiet) throws IOException {
		_saveButton.disableProperty().set(true);

		File directory = new File("results");
		if (! directory.exists()){
			directory.mkdir();
		}

		String directoryName = ConfigMeta.getInstance().resultDirectoryProperty().getValue();
		final String subjectId = Session.getCurrentSession().getSubjectId().replaceAll("[:\\\\/*\"?|<>]", "_");
		final String testId = Session.getCurrentSession().getTestId().replaceAll("[:\\\\/*\"?|<>]", "_");
		final String testType = Session.getCurrentSession().getTestType().name();
		final String baseFilename = String.format("%s_%s_%s", testType, subjectId, testId);

		if (Files.notExists(FileSystems.getDefault().getPath(directoryName))) {
			directoryName = ".";
		}

		Path path = FileSystems.getDefault().getPath(directoryName, String.format("%s.csv", baseFilename));
		for (int i = 1; Files.exists(path); i++) {
			path = FileSystems.getDefault().getPath(directoryName, String.format("%s.%d.csv", baseFilename, i));
		}

		CSVPrinter writer = new CSVPrinter(new FileWriter(path.toFile()), CSVFormat.newFormat('\t').withCommentMarker('#').withRecordSeparator('\n'));
		writer.printComment(String.format("subjectId=%s", subjectId));
		writer.printComment(String.format("testId=%s", testId));
		writer.printComment(String.format("testType=%s", testType));
		writer.printComment(String.format("total=%d", _total.get()));
		writer.printComment(String.format("hits=%d", _hits.get()));
		writer.printComment(String.format("correct rejections=%d", _correctRejections.get()));
		writer.printComment(String.format("misses=%d", _misses.get()));
		writer.printComment(String.format("false positives=%d", _falsePositives.get()));
		writer.printComment(String.format("CV'=%.4f", _cv.get()));
		writer.printComment(String.format("mean=%.2f ms", _mean.get()));
		writer.printComment(String.format("sd=%.2f ms", _sd.get()));
		writer.printComment(String.format("median=%.2f ms", _median.get()));
		writer.printComment(String.format("Q10=%.2f ms", _q10.get()));
		writer.printComment(String.format("Q25=%.2f ms", _q25.get()));
		writer.printComment(String.format("Q75=%.2f ms", _q75.get()));
		writer.printComment(String.format("Q90=%.2f ms", _q90.get()));
		writer.printComment(String.format("max=%d ms", _max.get()));
		writer.printComment(String.format("min=%d ms", _min.get()));
		writer.printComment(String.format("reaction speed=%f", _reactionSpeed.get()));
		writer.printComment(String.format("performance score=%f", _performanceScore.get()));
		writer.printComment(String.format("lapse probability=%f", _lapseProbability.get()));

		writer.printRecord("timepoint (ms)", "duration (ms)", "type");

		for (final Result result : Session.getCurrentSession().getResults()) {
			writer.printRecord(
				result.getRuntime(),
				result.getDuration(),
				result.getType()
			);
		}

		writer.flush();

		if(!quiet){
			getScreenManager().showMessage(String.format("Test saved as\n%s.", path.getFileName().toString()));
			getScreenManager().setScreen(Screens.MAIN);
		}



	}

	public void buttonSave(final ActionEvent e){
		try{
			buttonSave(e, false);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}


}