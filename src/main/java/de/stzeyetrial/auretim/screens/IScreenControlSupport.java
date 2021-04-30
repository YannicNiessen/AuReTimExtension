package de.stzeyetrial.auretim.screens;

/**
 * @author strasser
 */
public interface IScreenControlSupport {
	public void setScreenManager(final ScreenManager manager);
	public void enter();
	public void leave();
}