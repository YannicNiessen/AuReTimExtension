package de.stzeyetrial.auretim.util;

import java.util.Objects;

/**
 * @author strasser
 */
public class Result {
	private final long _runtime;
	private final Long _value;
	private final Type _type;

	public Result(final long runtime, final long value, final Type type) {
		_runtime	= runtime;
		_value		= value;
		_type		= type;
	}

	public Type getType() {
		return _type;
	}

	public long getDuration() {
		return _value;
	}

	public long getRuntime() {
		return _runtime;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 23 * hash + Objects.hashCode(_value);
		hash = 23 * hash + Objects.hashCode(_type);
		hash = 23 * hash + Objects.hashCode(_runtime);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Result other = (Result) obj;
		if (!Objects.equals(_value, other._value)) {
			return false;
		}
		if (!Objects.equals(_runtime, other._runtime)) {
			return false;
		}
		return _type == other._type;
	}

	@Override
	public String toString() {
		return String.format("[%s] %d ms (@ %d)", _type, _value, _runtime);
	}

	public static enum Type {
		FALSE_NEGATIVE,
		FALSE_POSITIVE,
		TRUE_NEGATIVE,
		TRUE_POSITIVE
	}
}