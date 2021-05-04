package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.input.SpeechDecoder;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.RandomSequence;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author strasser
 */
public abstract class MonoNBackSpeechTask extends Task<List<Result>> {
	private static final int THREAD_POOL_SIZE = 2;

	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();
	private final ReadOnlyObjectWrapper<Result> _currentResult = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<String> _currentResultString = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<Stimulus> _currentStimulus = new ReadOnlyObjectWrapper<>();

	protected final int _length;
	protected final int _nRepeat;
	private final int _nMatch;
	private final int _nLures;
	private final int _nBackLevel;
	private final boolean _reUseElements;

	private final List<Result> _results;
	private final int _timeout;
	public final CyclicBarrier gate = new CyclicBarrier(2);



	private final ScheduledExecutorService _executor;

	protected MonoNBackSpeechTask(final List<Result> results, final int length, final int nRepeat, final int nMatch, final int nLures, final int nBackLevel, final int timeout, final boolean reUseElements, boolean dual){
		_results				= results;
		_length					= length;
		_nRepeat				= nRepeat;
		_nMatch					= nMatch;
		_nLures					= nLures;
		_timeout				= timeout;
		_nBackLevel 			= nBackLevel;
		_reUseElements			= reUseElements;


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
	public ReadOnlyObjectProperty<Stimulus> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}
	public ReadOnlyObjectProperty<String> currentResultStringProperty() {return _currentResultString.getReadOnlyProperty();}


	@Override
	protected List<Result> call() throws Exception {
		updateProgress(0, _length + _nBackLevel);

		callImpl();

		return Collections.unmodifiableList(_results);
	}


	protected abstract void callImpl() throws Exception;

	protected final void callImpl2() throws Exception {
		final long testStart = System.currentTimeMillis();
		_trigger.trigger(TriggerType.START_TEST);

		Integer[] sequence = RandomSequence.getRandomSequenceNBack(_length, 10, _nRepeat, _nMatch, _nLures, _nBackLevel, true);
		Integer[] sequence2 = RandomSequence.getRandomSequenceNBack(_length, 10, _nRepeat, _nMatch, _nLures, _nBackLevel, true);

		for (int i = 0; i < sequence.length; i++) {
			System.out.print(sequence[i] + " ");
		}
		long ts = System.currentTimeMillis();

		new SpeechDecoder().initialize();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					SpeechDecoder.getInstance().startRecording();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		Thread t = new Thread(r);
		t.start();

		List<String> allWords = new ArrayList<>();
		int wordsSizePreAdd = 0;


		List<Integer> sequenceCopy = new ArrayList<>();
		sequenceCopy.addAll(Arrays.asList(sequence));
		sequenceCopy.add(4);
		sequenceCopy.add(4);
		sequenceCopy.toArray();
		NBackSpeechInputTask.resetAllWords();




		for (int i = 0; i < (sequence.length + _nBackLevel); i++) {

			boolean positive = false;
			if (i >= _nBackLevel)
				positive = (sequenceCopy.get(i - _nBackLevel) == sequenceCopy.get(i));

			int expectedValue = i >= _nBackLevel ? sequenceCopy.get(i - _nBackLevel) : -1;

			final NBackSpeechInputTask inputTask = getSpeechInputTask(gate, testStart, positive, expectedValue);
			_currentStimulus.setValue(Stimulus.unrealStimulus());
			_currentStimulus.setValue(new Stimulus(1, new Integer[]{sequenceCopy.get(i)}));

			final Result result = _executor.submit(inputTask).get();


			updateProgress(i+1, _length);
			if (allWords.size() > 0){
				_currentResultString.setValue(inputTask.get_foundWord());
			}

			_results.add(result);
			final long wait = _timeout * 1000 - result.getDuration();
			if (wait > 0) {
				_executor.schedule(() -> {}, wait, TimeUnit.MILLISECONDS).get();
			}

			long endTs = System.currentTimeMillis();


		}
		_trigger.trigger(TriggerType.END_TEST);

		System.out.println("Real sequence was: " + Arrays.toString(sequence));
		System.out.println("Spoken sequence was: " + Arrays.toString(NBackSpeechInputTask.get_allWords().toArray()));

		SpeechDecoder.getInstance().stopRecording();




	}



	protected abstract int getDelay();
	protected abstract Tone getTone();
	protected abstract NBackSpeechInputTask getSpeechInputTask(final CyclicBarrier gate, final long testStart, boolean result, int expectedValue);
}

