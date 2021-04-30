package de.stzeyetrial.auretim.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.StringConverter;

/**
 * @author strasser
 */
public class NullSafeNumberStringConverter extends StringConverter<Number> {
	private final NumberFormat _format;

	public NullSafeNumberStringConverter(final NumberFormat format) {
		_format = format;
	}

	@Override
	public String toString(final Number object) {
		return _format.format(object);
	}

	@Override
	public Number fromString(final String string) {
		if (string != null && !string.isEmpty()) {
			try {
				return _format.parse(string);
			} catch (ParseException ex) {
				return null;
			}
		} else {
			return null;
		}
	}	
}