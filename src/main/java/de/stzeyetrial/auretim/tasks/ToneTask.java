package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

/**
 * @author strasser
 */
final class ToneTask implements Runnable, LineListener {
	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();

	private final Tone _tone;
	private final CyclicBarrier _gate;
	private final int _volume;

	ToneTask(final Tone tone, final CyclicBarrier gate, final int volume) {
		_gate = gate;
		_tone = tone;
		_volume = volume;
		_tone.addLineListener(this);

	}

	@Override
	public final void run() {
		_trigger.trigger(TriggerType.TONE);

		_tone.play(_volume);
		_tone.removeLineListener(this);
	}

	@Override
	public void update(final LineEvent event) {

		if (Type.START == event.getType()) {
			try {
				_gate.await();
			} catch (InterruptedException | BrokenBarrierException ex) {
				ex.printStackTrace(System.err);
			}			
		}
	}
}