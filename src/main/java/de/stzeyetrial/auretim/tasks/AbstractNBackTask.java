package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledExecutorService;

public abstract class AbstractNBackTask extends Task<List<Result>>
{
    protected static final int THREAD_POOL_SIZE = 2;

    public ReadOnlyObjectProperty<Stimulus> currentStimulusProperty() {return _currentStimulus.getReadOnlyProperty();}
    public ReadOnlyObjectProperty<String> currentResultStringProperty() {return _currentResultString.getReadOnlyProperty();}

    protected final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();
    protected final ReadOnlyObjectWrapper<String> _currentResultString = new ReadOnlyObjectWrapper<>();
    protected final ReadOnlyObjectWrapper<Stimulus> _currentStimulus = new ReadOnlyObjectWrapper<>();

    protected ScheduledExecutorService _executor;
    public final CyclicBarrier gate = new CyclicBarrier(2);

    protected List<Result> _results;
    protected int _timeout = 0;
    protected int _length = 0;
    protected int _nBackLevel = 0;


    public AbstractNBackTask(final List<Result> results, final int length, final int nBackLevel, final int timeout){
        super();
        _results = results;
        _length = length;
        _nBackLevel = nBackLevel;
        _timeout = timeout;
    }


    @Override
    protected abstract List<Result> call() throws Exception;

    protected AbstractInputTask getInputTask(final CyclicBarrier gate, final long testStart, boolean result){
        return new NBackInputTask(gate, testStart, _timeout,0 , result);
    };


}
