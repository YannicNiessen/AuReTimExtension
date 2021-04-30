package de.stzeyetrial.auretim.input.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import de.stzeyetrial.auretim.input.AbstractInputSource;
import de.stzeyetrial.auretim.io.GpioFactory;

/**
 * @author strasser
 */
public final class GPIOButtonInput extends AbstractInputSource {
	private static GPIOButtonInput INSTANCE;

	private GPIOButtonInput() {
		final GpioController gpio = GpioFactory.getInstance();

		final GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, PinState.HIGH);
		output.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

		final GpioPinDigitalInput input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_DOWN);
		input.addListener((GpioPinListenerDigital) (final GpioPinDigitalStateChangeEvent event) -> {
			if (PinState.HIGH == event.getState()) {
				fire();
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread(() -> gpio.unprovisionPin(input, output)));
	}

	public static synchronized GPIOButtonInput getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GPIOButtonInput();
		}

		return INSTANCE;
	}
}