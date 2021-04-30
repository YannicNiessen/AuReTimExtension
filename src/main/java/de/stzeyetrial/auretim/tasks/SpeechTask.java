package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author strasser
 */
final class SpeechTask implements Runnable {
	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();

	private final CyclicBarrier _gate;
	private final int _volume;
	private final String _speech;
	private static SpeechSynthesizer synthesizer;

	SpeechTask(String speech,  final CyclicBarrier gate, final int volume) {
		_gate = gate;
		_volume = volume;
		_speech = speech;
	}

	@Override
	public final void run() {
		_trigger.trigger(TriggerType.TONE);
		System.out.println("here");

		SpeechSynthesizer.speak(_speech);


		try{
			_gate.await();
		}catch (InterruptedException | BrokenBarrierException ex){
			ex.printStackTrace();
		}


	}

}