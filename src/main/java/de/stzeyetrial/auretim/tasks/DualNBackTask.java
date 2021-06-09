package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.RandomSequence;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author strasser
 */
public class DualNBackTask extends AbstractNBackTask{

	private Integer[] _firstSequence;
	private Integer[] _secondSequence;

	public DualNBackTask(final List<Result> results,
							final int length,
							final int nOptionsFirstSequence,
							final int nRepeatFirstSequence,
							final int nMatchFirstSequence,
							final int nLuresFirstSequence,
							final boolean reUseElementsFirstSequence,
							final int nOptionsSecondSequence,
							final int nRepeatSecondSequence,
							final int nMatchSecondSequence,
							final int nLuresSecondSequence,
							final boolean reUseElementsSecondSequence,
							final int nBackLevel,
							final int timeout
							) throws Exception {
		super(results, length, nBackLevel, timeout);

		_executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE, r -> {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() ->_executor.shutdown()));

		_firstSequence = RandomSequence.getRandomSequenceNBack(length, nOptionsFirstSequence, nRepeatFirstSequence, nMatchFirstSequence, nLuresFirstSequence, nBackLevel, reUseElementsFirstSequence);
		_secondSequence = RandomSequence.getRandomSequenceNBack(length, nOptionsSecondSequence, nRepeatSecondSequence, nMatchSecondSequence, nLuresSecondSequence, nBackLevel, reUseElementsSecondSequence);

	}

	@Override
	protected List<Result> call() throws Exception {

		_trigger.trigger(TriggerType.START_TEST);

		final long testStart = System.currentTimeMillis();


		updateProgress(0, _length + _nBackLevel);

		for (int i = 0; i < _firstSequence.length && !isCancelled(); i++) {

			boolean positive = false;
			if (i >= _nBackLevel)
				positive = _firstSequence[i - _nBackLevel].equals(_firstSequence[i]) || _secondSequence[i - _nBackLevel].equals(_secondSequence[i]) ;

			final AbstractInputTask inputTask = getInputTask(gate, testStart, positive);

			_currentStimulus.setValue(Stimulus.unrealStimulus());

			_currentStimulus.setValue(new Stimulus(2, new Integer[]{_firstSequence[i], _secondSequence[i]}));

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

}

