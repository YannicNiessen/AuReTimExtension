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
public class SpatialWorkingMemoryUpdateTask extends Task<List<Result>> {
	private static final int THREAD_POOL_SIZE = 2;

	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();
	private final ReadOnlyObjectWrapper<Result> _currentResult = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<String> _currentResultString = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<Stimulus> _currentStimulus = new ReadOnlyObjectWrapper<>();

	protected final int _length;


	private final List<Result> _results;
	private boolean _dual;
	private int _frameCount;
	private int _initialDelay;
	private int _interval;



	private final ScheduledExecutorService _executor;
	public final CyclicBarrier gate = new CyclicBarrier(2);

	public SpatialWorkingMemoryUpdateTask(final List<Result> results, final int length, final int frameCount, int initialDelay, int interval) {
		_results				= results;
		_length					= length;
		_frameCount 			= frameCount;
		_initialDelay			= initialDelay;
		_interval				= interval;


		_executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE, r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() ->_executor.shutdown()));
	}

	public ReadOnlyObjectProperty<Result> currentResultProperty() {
		return _currentResult.getReadOnlyProperty();
	}
	public ReadOnlyObjectProperty<Stimulus> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}
	public ReadOnlyObjectProperty<String> currentResultStringProperty() {return _currentResultString.getReadOnlyProperty();}


	@Override
	protected List<Result> call() throws Exception {
		updateProgress(0, (long) (_length-1) * _frameCount );

		callImpl();

		return Collections.unmodifiableList(_results);
	}


	protected void callImpl() throws Exception {
		try {
			callImpl2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	protected void callImpl2() throws Exception {
		final long testStart = System.currentTimeMillis();
		_trigger.trigger(TriggerType.START_TEST);

		Integer[][] sequences = new Integer[_frameCount][];

		for (int i = 0; i < _frameCount; i++) {
			sequences[i] = RandomSequence.spatialWorkingMemoryUpdateSequence(_length);
		}

		//Initial presentation in all frames at the same time
		for (int i = 0; i < _frameCount; i++) {
			_currentStimulus.setValue(Stimulus.unrealStimulus());

			_currentStimulus.setValue(new Stimulus(1, new Integer[]{sequences[i % _frameCount][(int) i / _frameCount]}));
		}

		//Initial presentation time
		_executor.schedule(() -> {}, _initialDelay, TimeUnit.MILLISECONDS).get();

		//Round based presentation of one frame at a time
		for (int i = _frameCount; i < _length * _frameCount && !isCancelled(); i++) {

			_currentStimulus.setValue(Stimulus.unrealStimulus());

			_currentStimulus.setValue(new Stimulus(1, new Integer[]{sequences[i % _frameCount][(int) i / _frameCount]}));

			updateProgress(i+1 - _frameCount, (long)  (_length - 1) * _frameCount);

			final long wait = _interval;
			if (wait > 0) {
				_executor.schedule(() -> {}, wait, TimeUnit.MILLISECONDS).get();
			}

		}
		_trigger.trigger(TriggerType.END_TEST);

	}



	protected AbstractInputTask getInputTask(final CyclicBarrier gate, final long testStart, boolean result){
		return new NBackInputTask(gate, testStart, 0, 200, result);

	};
}

