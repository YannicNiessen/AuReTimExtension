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
public abstract class AbstractNBackTask extends Task<List<Result>> {
	private static final int THREAD_POOL_SIZE = 2;

	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();
	private final ReadOnlyObjectWrapper<String> _currentResultString = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<Stimulus> _currentStimulus = new ReadOnlyObjectWrapper<>();

	protected final int _length;
	protected final int _nOptions;
	protected final int _nRepeat;
	private final int _nMatch;
	private final int _nLures;
	private final int _nBackLevel;
	private final boolean _reUseElements;

	private final List<Result> _results;
	private final int _timeout;
	private boolean _dual;

	private final ScheduledExecutorService _executor;
	public final CyclicBarrier gate = new CyclicBarrier(2);

	protected AbstractNBackTask(final List<Result> results, final int length, final int nOptions, final int nRepeat, final int nMatch, final int nLures, final int nBackLevel, final int timeout, final boolean reUseElements, boolean dual) {
		_results				= results;
		_length					= length;
		_nOptions				= nOptions;
		_nRepeat				= nRepeat;
		_nMatch					= nMatch;
		_nLures					= nLures;
		_timeout				= timeout;
		_nBackLevel 			= nBackLevel;
		_reUseElements			= reUseElements;
		_dual 					= dual;

		_executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE, r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() ->_executor.shutdown()));
	}

	public ReadOnlyObjectProperty<Stimulus> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}
	public ReadOnlyObjectProperty<String> currentResultStringProperty() {return _currentResultString.getReadOnlyProperty();}


	@Override
	protected List<Result> call() throws Exception {
		updateProgress(0, _length + _nBackLevel);

		callImpl();

		return Collections.unmodifiableList(_results);
	}


	protected abstract void callImpl() throws Exception;

	protected void callImpl2() throws Exception {
		final long testStart = System.currentTimeMillis();
		_trigger.trigger(TriggerType.START_TEST);

		Integer[] sequence = RandomSequence.getRandomSequenceNBack(_length, _nOptions, _nRepeat, _nMatch, _nLures, _nBackLevel, true);
		Integer[] sequence2 = RandomSequence.getRandomSequenceNBack(_length, _nOptions, _nRepeat, _nMatch, _nLures, _nBackLevel, true);


		for (int i = 0; i < sequence.length && !isCancelled(); i++) {

			boolean positive = false;
			if (i >= _nBackLevel)
				positive = (sequence[i - _nBackLevel] == sequence[i]);

			final AbstractInputTask inputTask = getInputTask(gate, testStart, positive);

			_currentStimulus.setValue(Stimulus.unrealStimulus());

			if (_dual){
				_currentStimulus.setValue(new Stimulus(2, new Integer[]{sequence[i], sequence2[i]}));
			}else{
				_currentStimulus.setValue(new Stimulus(1, new Integer[]{sequence[i]}));
			}
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

	}



	protected abstract int getDelay();
	protected abstract Tone getTone();
	protected abstract AbstractInputTask getInputTask(final CyclicBarrier gate, final long testStart, boolean result);
}

