package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.input.InputFactory;
import de.stzeyetrial.auretim.input.SpeechDecoder;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.Result;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static de.stzeyetrial.auretim.util.Result.Type.*;

/**
 * @author niessen
 */
public class NBackSpeechInputTask implements Callable<Result> {
	private final boolean _positive;
	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();

	private final CountDownLatch _latch;
	private final CyclicBarrier _gate;

	private final long _testStart;
	private final int _minimumResponseTime;

	private final int _maximumTime;

	private static int _wordsSizePreAdd = 0;
	private int _expectedValue;
	private static List<String> _allWords = new ArrayList<>();
	private String _foundWord;

	public NBackSpeechInputTask(final CyclicBarrier gate, final long testStart, final int maximumTime, final int minimumResponseTime, final boolean positive, int expectedValue) {
		_gate = gate;
		_testStart = testStart;
		_maximumTime = maximumTime;
		_minimumResponseTime = minimumResponseTime;

		_latch = new CountDownLatch(1);
		_positive = positive;
		_expectedValue = expectedValue;
	}

	@Override
	public final Result call() throws Exception {
		_gate.await();

		final long start = System.currentTimeMillis();
		final boolean timeout = !_latch.await(_maximumTime, TimeUnit.SECONDS);

		final long now = System.currentTimeMillis();

		final long startStep = start - _testStart;
		final long duration = now - start;

		if (!timeout) {
			_trigger.trigger(TriggerType.RESPONSE);
		}
		List<String> words = new ArrayList<>();
		synchronized (SpeechDecoder.synList){
			for (int j = _wordsSizePreAdd; j < SpeechDecoder.synList.size(); j++) {
				words.add(SpeechDecoder.synList.get(j));
			}
			_wordsSizePreAdd = SpeechDecoder.synList.size();
		}

		boolean foundWord = false;
		for (int j = 0; j < words.size(); j++) {
			if (words.get(j).contains(intToGermanWord(_expectedValue))){
				_allWords.add(String.valueOf(_expectedValue));
				foundWord = true;
				_foundWord = String.valueOf(_expectedValue);
				break;
			}
			if (j == words.size()-1){
				_allWords.add(String.valueOf(germanWordToInt(words.get(j).split(" ")[0])));
				_foundWord = _allWords.get(_allWords.size()-1);
			}
		}

		return evaluate(startStep, duration, timeout, foundWord);
	}

	protected Result evaluate(long startStep, long duration, boolean timeout, boolean foundWord) {

			return new Result(startStep, duration, (foundWord) ? TRUE_POSITIVE : FALSE_NEGATIVE);

	}

	private int germanWordToInt(String word){
		switch(word) {
			case "null":
				return 0;
			case "eins":
				return 1;
			case "zwei":
				return 2;
			case "drei":
				return 3;
			case "vier":
				return 4;
			case "fünf":
				return 5;
			case "sechs":
				return 6;
			case "sieben":
				return 7;
			case "acht":
				return 8;
			case "neun":
				return 9;
		}
		return -1;
	}
	private String intToGermanWord(int number){
		switch(number){
			case 0:
				return "null";
			case 1:
				return "eins";
			case 2:
				return "zwei";
			case 3:
				return "drei";
			case 4:
				return "vier";
			case 5:
				return "fünf";
			case 6:
				return "sechs";
			case 7:
				return "sieben";
			case 8:
				return "acht";
			case 9:
				return "neun";
			default:
				return "-";
		}
	}

	public static List<String> get_allWords() {
		return _allWords;
	}

	public String get_foundWord() {
		return _foundWord;
	}

	public static void resetAllWords(){
		_allWords.clear();
	}
}