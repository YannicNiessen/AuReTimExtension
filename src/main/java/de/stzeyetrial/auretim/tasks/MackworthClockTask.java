package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.RandomSequence;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author strasser
 */
public class MackworthClockTask extends Task<List<Result>> {
	private static final int THREAD_POOL_SIZE = 2;

	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();
	private final ReadOnlyObjectWrapper<Result> _currentResult = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<String> _currentResultString = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<Boolean> _currentStimulus = new ReadOnlyObjectWrapper<>();

	protected final int _length;
	private final int[] _targetIndices;
	private final int _minimumResponseTime;


	private final List<Result> _results;
	private final int _timeout;



	private final ScheduledExecutorService _executor;
	public final CyclicBarrier gate = new CyclicBarrier(2);

	public MackworthClockTask(final List<Result> results, final int length, final int[] targetIndices, final int timeout, final int minimumResponseTime) {
		_results				= results;
		_length					= length;
		_targetIndices			= targetIndices;
		_timeout				= timeout;
		_minimumResponseTime 	= minimumResponseTime;


		_executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE, r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() ->_executor.shutdown()));
	}

	public ReadOnlyObjectProperty<Boolean> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}
	public ReadOnlyObjectProperty<String> currentResultStringProperty() {return _currentResultString.getReadOnlyProperty();}


	@Override
	protected List<Result> call() throws Exception {
		updateProgress(0, _length);

		final long testStart = System.currentTimeMillis();
		_trigger.trigger(TriggerType.START_TEST);

		Boolean[] sequence = RandomSequence.getMackworthClockSequenceFromTimings(_length, _targetIndices);
		System.out.println("length is " + _length);
		for (int i = 0; i < sequence.length && !isCancelled(); i++) {

			boolean positive = sequence[i];

			final AbstractInputTask inputTask = getInputTask(gate, testStart, positive);
			//_currentStimulus.setValue(null);
			System.out.println("task = " + _currentStimulus);
			_currentStimulus.setValue(null);

			_currentStimulus.setValue(positive);
			System.out.println(gate.getNumberWaiting());
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
		return new NBackInputTask(gate, testStart, _timeout, _minimumResponseTime, result);
	};
}

