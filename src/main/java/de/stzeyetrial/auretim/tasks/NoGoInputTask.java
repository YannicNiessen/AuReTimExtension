package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.util.Result;
import java.util.concurrent.CyclicBarrier;
import static de.stzeyetrial.auretim.util.Result.Type.*;

/**
 * @author strasser
 */
public class NoGoInputTask extends AbstractInputTask {
	private final boolean _positive;

	public NoGoInputTask(final CyclicBarrier gate, final long testStart, final int maximumTime, final int minimumResponseTime, final boolean positive) {
		super(gate, testStart, maximumTime, minimumResponseTime);
		_positive = positive;
	}

	@Override
	protected Result evaluate(long startStep, long duration, boolean timeout, int minimumResponseTime) {
		if (timeout) {
			return new Result(startStep, duration, (_positive) ? FALSE_NEGATIVE : TRUE_NEGATIVE);
		} else {
			return new Result(startStep, duration, (_positive) ? TRUE_POSITIVE : FALSE_POSITIVE);
		}
	}	
}
