package de.stzeyetrial.auretim.output.gpio;

import de.stzeyetrial.auretim.output.ITrigger;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import de.stzeyetrial.auretim.io.GpioFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import java.util.Arrays;

/**
 * @author strasser
 */
public final class GPIOTrigger implements ITrigger {
	private static final int TRIGGER_DURATION_MS = 2;

	private static GPIOTrigger INSTANCE;

	private final GpioPinDigitalOutput[] _pins = new GpioPinDigitalOutput[TriggerType.values().length];

	private GPIOTrigger() {
		final GpioController gpio = GpioFactory.getInstance();

		_pins[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "START_TEST", PinState.LOW);
		_pins[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "END_TEST", PinState.LOW);
		_pins[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "TONE", PinState.LOW);
		_pins[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "RESPONSE", PinState.LOW);

		Arrays.stream(_pins).forEach(pin -> pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF));
		Runtime.getRuntime().addShutdownHook(new Thread(() -> gpio.unprovisionPin(_pins)));
	}

	public static synchronized GPIOTrigger getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GPIOTrigger();
		}

		return INSTANCE;
	}

	@Override
	public void trigger(final TriggerType type) {
		final GpioPinDigitalOutput pin;
		switch(type) {
			case START_TEST:
				pin = _pins[0];
				break;
			case END_TEST:
				pin = _pins[1];
				break;
			case TONE:
				pin = _pins[2];
				break;
			case RESPONSE:
				pin = _pins[3];
				break;
			default:
				return;
		}
		pin.pulse(TRIGGER_DURATION_MS, PinState.HIGH, false);
	}
}