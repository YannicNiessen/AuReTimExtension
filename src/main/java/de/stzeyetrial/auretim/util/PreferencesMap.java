package de.stzeyetrial.auretim.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author strasser
 */
public class PreferencesMap extends LinkedHashMap<String, Integer> {
	private final static String KEY				= "value";
	private final static String DEFAULT_VALUE	= "1";

	private final Preferences _p;
	private final int _capacity;

	public PreferencesMap(final int capacity, final String node) {
		super(capacity);
		_capacity = capacity;
		_p = Preferences.userNodeForPackage(PreferencesMap.class).node(node);

		init();
	}

	public void save() {
		try {
			for (final String name : _p.childrenNames()) {
				_p.node(name).removeNode();
			}
			entrySet().stream().forEach((entry) -> {
				_p.node(entry.getKey()).put(KEY, Integer.toString(entry.getValue() + 1));
			});
			_p.sync();
		} catch (BackingStoreException ex) {
			Logger.getLogger(PreferencesMap.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected boolean removeEldestEntry(final Entry<String, Integer> eldest) {
		return size() > _capacity;
	}

	private void init() {
		try {
			for (final String name : _p.childrenNames()) {
				put(name, Integer.valueOf(_p.node(name).get(KEY, DEFAULT_VALUE)));
			}
		} catch (BackingStoreException ex) {
			Logger.getLogger(PreferencesMap.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}