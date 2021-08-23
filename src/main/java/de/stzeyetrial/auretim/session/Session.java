package de.stzeyetrial.auretim.session;

import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Test;
import de.stzeyetrial.auretim.util.TestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author strasser
 */
public class Session {
	private static Session _instance;

	private final List<Result> _results;
	private final String _subjectId;
	private final String _testId;
	private final Queue<TestType> _testQueue;
	private final String _sequenceId;

	private TestType _testType;


	private Session(final String subjectId, final String testId, TestType testType, Queue<TestType> testQueue, String sequenceId) {
		_subjectId = subjectId;
		_testId = testId;
		_testType = testType;
		_testQueue = testQueue;
		_results = new ArrayList<>();
		_sequenceId = sequenceId;

	}

	public static Session newSession(final String subjectId, final String testId, TestType testType, Queue<TestType> testQueue, String sequenceId) {
		_instance = new Session(subjectId, testId, testType, testQueue, sequenceId);
		return _instance;
	}

	public static Session getCurrentSession() throws IllegalStateException {
		if (_instance == null) {
			throw new IllegalStateException("no session availabe.");
		} else {
			return _instance;
		}
	}

	public String getSubjectId() {
		return _subjectId;
	}

	public String getTestId() {
		return _testId;
	}

	public List<Result> getResults() {
		return _results;
	}

	public TestType getTestType(){return _testType;}

	public Queue<TestType> getTestQueue() {
		return _testQueue;
	}


	public void setTestType(TestType testType){
		_testType = testType;
	}

	public void clearResults() {
		_results.clear();
	}

	public String get_sequenceId() {
		return _sequenceId;
	}
}