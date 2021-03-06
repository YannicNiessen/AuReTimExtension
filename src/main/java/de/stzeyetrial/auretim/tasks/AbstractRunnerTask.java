package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.Result;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import de.stzeyetrial.auretim.util.Stimulus;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;

/**
 * @author strasser
 */
public abstract class AbstractRunnerTask extends Task<List<Result>> {
	private static final int THREAD_POOL_SIZE = 2;

	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();
	private final ReadOnlyObjectWrapper<Result> _currentResult = new ReadOnlyObjectWrapper<>();
	protected final ReadOnlyObjectWrapper<Stimulus> _currentStimulus = new ReadOnlyObjectWrapper<>();

	public ReadOnlyObjectProperty<Stimulus> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}


	protected final int _frequency;
	protected final int _pulseDuration;
	protected final int _delay;

	private final List<Result> _results;
	private final IntegerProperty _volumeProperty;
	private final int _timeout;
	private final int _minimumResponseTime;
	private final int _repetitions;
	private final boolean _visual;

	private final ScheduledExecutorService _executor;

	public final CyclicBarrier gate = new CyclicBarrier(2);


	protected AbstractRunnerTask(final List<Result> results, final int frequency, final IntegerProperty volumeProperty, final int pulseDuration, final int minimumResponseTime, final int delay, final int timeout, final int repetitions, boolean visual) {
		_results				= results;
		_frequency				= frequency;
		_volumeProperty			= volumeProperty;
		_pulseDuration			= pulseDuration;
		_minimumResponseTime	= minimumResponseTime;
		_delay					= delay;
		_timeout				= timeout;
		_repetitions			= repetitions;
		_visual 				= visual;

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

	@Override
	protected List<Result> call() throws Exception {
		updateProgress(0, _repetitions);

		callImpl();

		return Collections.unmodifiableList(_results);
	}

	protected abstract void callImpl() throws Exception;

	protected final void callImpl2() throws Exception {
		final long testStart = System.currentTimeMillis();
		_trigger.trigger(TriggerType.START_TEST);

		for (int i = 1; i <= _repetitions && !isCancelled(); i++) {

			final int delay = getDelay();

			Tone tone = null;
			Stimulus stimulus = null;

			if (_visual){
				stimulus = getStimulus();
			}else{
				tone = getTone();
			}
			final AbstractInputTask inputTask = getInputTask(gate, testStart, _timeout, _minimumResponseTime);

			final Result result;

			if (_visual){
				Stimulus finalStimulus = stimulus;
				_executor.schedule(() -> {_currentStimulus.setValue(finalStimulus);}, delay, TimeUnit.SECONDS).get();
				result = _executor.submit(inputTask).get();

				_currentStimulus.setValue(Stimulus.unrealStimulus());

			}else{

				final ScheduledFuture<?> futureTone = _executor.schedule(new ToneTask(tone, gate, _volumeProperty.get()), delay, TimeUnit.SECONDS);
				result = _executor.submit(inputTask).get();

				futureTone.get();

			}


			updateProgress(i, _repetitions);
			_results.add(result);
			Platform.runLater(() -> _currentResult.setValue(result));



			final long wait = _timeout - result.getDuration();
			if (wait > 0) {
				_executor.schedule(() -> {}, wait, TimeUnit.MILLISECONDS).get();
			}

		}
		_trigger.trigger(TriggerType.END_TEST);
	}

	protected abstract int getDelay();
	protected abstract Tone getTone();
	protected abstract Stimulus getStimulus();
	protected abstract AbstractInputTask getInputTask(final CyclicBarrier gate, final long testStart, final int maximumTime, final int minimumResponseTime);
}