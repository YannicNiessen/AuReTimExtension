package de.stzeyetrial.auretim.session;

import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.TestType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author strasser
 */
public class Session {
	private static Session _instance;

	private final List<Result> _results;
	private final String _subjectId;
	private final String _testId;
	private final TestType _testType;

	private Session(final String subjectId, final String testId, TestType testType) {
		_subjectId = subjectId;
		_testId = testId;
		_results = new ArrayList<>();
		_testType = testType;
	}

	public static Session newSession(final String subjectId, final String testId, TestType testType) {
		_instance = new Session(subjectId, testId, testType);
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

	public void clearResults() {
		_results.clear();
	}
}