package de.stzeyetrial.auretim.io;

import com.pi4j.io.gpio.GpioController;

/**
 * @author strasser
 */
public final class GpioFactory {
	private static GpioController _instance;

	private GpioFactory() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> _instance.shutdown()));
	}

	public static synchronized GpioController getInstance() {
		if (_instance == null) {
			_instance = com.pi4j.io.gpio.GpioFactory.getInstance();
		}

		return _instance;
	}
}