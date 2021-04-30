package de.stzeyetrial.auretim.output;

import de.stzeyetrial.auretim.output.gpio.GPIOTrigger;

/**
 * @author strasser
 */
public final class TriggerFactory {
	private static TriggerFactory INSTANCE;

	private TriggerFactory() {
	}

	public static synchronized TriggerFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TriggerFactory();
		}
		return INSTANCE;
	}

	public ITrigger createTrigger() {
		try {
			return GPIOTrigger.getInstance();
		} catch (Throwable t) {
			return new NoOpTrigger();
		}
	}
}