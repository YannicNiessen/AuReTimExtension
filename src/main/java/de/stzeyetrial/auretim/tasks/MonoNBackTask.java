package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.input.SpeechDecoder;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.RandomSequence;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author strasser
 */
public class MonoNBackTask extends AbstractNBackTask {

	private Integer[] _sequence;

	public MonoNBackTask(final List<Result> results, final int length, final int nOptions, final int nRepeat, final int nMatch, final int nLures, final int nBackLevel, final int timeout, final boolean reUseElements) throws Exception {
		super(results, length, nBackLevel, timeout);

		_executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE, r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() ->_executor.shutdown()));

		_sequence = RandomSequence.getRandomSequenceNBack(_length, nOptions, nRepeat, nMatch, nLures, _nBackLevel, true);

	}

	public ReadOnlyObjectProperty<Stimulus> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}
	public ReadOnlyObjectProperty<String> currentResultStringProperty() {return _currentResultString.getReadOnlyProperty();}


	@Override
	protected List<Result> call() throws Exception {

		_trigger.trigger(TriggerType.START_TEST);

		final long testStart = System.currentTimeMillis();

		updateProgress(0, _length + _nBackLevel);

		for (int i = 0; i < _sequence.length && !isCancelled(); i++) {

			boolean positive = false;
			if (i >= _nBackLevel)
				positive = (_sequence[i - _nBackLevel].equals(_sequence[i]));

			final AbstractInputTask inputTask = getInputTask(gate, testStart, positive);

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
		_trigger.trigger(TriggerType.END_TEST);
		return Collections.unmodifiableList(_results);
	}

	protected AbstractInputTask getInputTask(final CyclicBarrier gate, final long testStart, boolean result){
		return new NBackInputTask(gate, testStart, _timeout,0 , result);
	};
}

