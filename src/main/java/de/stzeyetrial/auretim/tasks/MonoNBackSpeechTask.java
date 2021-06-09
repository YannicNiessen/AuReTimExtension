package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.input.SpeechDecoder;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.RandomSequence;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import de.stzeyetrial.auretim.util.StimulusSet;
import javafx.beans.property.ReadOnlyObjectProperty;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author strasser
 */
public class MonoNBackSpeechTask extends AbstractNBackTask {

	private Integer[] _sequence;

	private StimulusSet _stimulusSet;

	public MonoNBackSpeechTask(final List<Result> results, final int length, final int nOptions, final int nRepeat, final int nMatch, final int nLures, final int nBackLevel, final int timeout, final boolean reUseElements, StimulusSet stimulusSet) throws Exception {
		super(results, length, nBackLevel, timeout);

		_executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE, r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() ->_executor.shutdown()));

		_sequence = RandomSequence.getRandomSequenceNBack(_length, nOptions, nRepeat, nMatch, nLures, _nBackLevel, true);
		_stimulusSet = stimulusSet;

	}

	public ReadOnlyObjectProperty<Stimulus> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}
	public ReadOnlyObjectProperty<String> currentResultStringProperty() {return _currentResultString.getReadOnlyProperty();}


	@Override
	protected List<Result> call() throws Exception {

		_trigger.trigger(TriggerType.START_TEST);

		final long testStart = System.currentTimeMillis();

		updateProgress(0, _length + _nBackLevel);

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					SpeechDecoder.getInstance().initialize(SpeechDecoder.Language.GERMAN, _stimulusSet.get_type());
					SpeechDecoder.getInstance().startRecording();
				} catch (InterruptedException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();

		for (int i = 0; i < _sequence.length && !isCancelled(); i++) {

			boolean positive = false;
			String expectedValue = "-1";
			if (i >= _nBackLevel){
				positive = (_sequence[i - _nBackLevel].equals(_sequence[i]));
				expectedValue = _stimulusSet.get_elements().get(_sequence[i - _nBackLevel]);
			}

			final NBackSpeechInputTask inputTask = getSpeechInputTask(gate, testStart, positive, expectedValue);

			_currentStimulus.setValue(Stimulus.unrealStimulus());

			_currentStimulus.setValue(new Stimulus(1, new Integer[]{_sequence[i]}));

			final Result result = _executor.submit(inputTask).get();

			updateProgress(i+1, _length);
			_currentResultString.setValue(result.getType().toString());

			_results.add(result);
			final long wait = _timeout - result.getDuration();
			if (wait > 0) {
				_executor.schedule(() -> {}, wait, TimeUnit.MILLISECONDS).get();
			}

		}

		SpeechDecoder.getInstance().stopRecording();

		_trigger.trigger(TriggerType.END_TEST);
		return Collections.unmodifiableList(_results);
	}

	protected NBackSpeechInputTask getSpeechInputTask(final CyclicBarrier gate, final long testStart, boolean result, String expectedValue){
		return new NBackSpeechInputTask(gate, testStart, _timeout,0 , result, expectedValue);
	};
}

