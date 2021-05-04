package de.stzeyetrial.auretim.screens;

/**
 * @author strasser
 */
public enum Screens {
	MAIN("/fxml/Main.fxml"),
	SETTINGS("/fxml/SettingsLegacy.fxml"),
	TEST("/fxml/Test.fxml"),
	VISUAL_PVT("/fxml/VisualPVT.fxml"),
	RESULT("/fxml/Result.fxml"),
	DIRECTORY_CHOOSER("/fxml/DirectoryChooser.fxml"),
	N_BACK_TEST_VISUAL_IDENTITY("/fxml/nBackTestVisualIdentity.fxml"),
	N_BACK_TEST_VISUAL_LOCATION("/fxml/nBackTestVisualLocation.fxml"),
	N_BACK_TEST_AUDITIVE("/fxml/nBackTestAuditive.fxml"),
	MACKWORTH_CLOCK_TEST("/fxml/MackworthClockTest.fxml"),
	N_BACK_TEST_VISUAL_IDENTITY_AUDITIVE_DUAL("/fxml/nBackTestVisualIdentityDual.fxml"),
	N_BACK_TEST_VISUAL_LOCATION_AUDITIVE_DUAL("/fxml/nBackTestVisualLocationDual.fxml"),
	N_BACK_TEST_VISUAL_LOCATION_IDENTITY_DUAL("/fxml/nBackTestVisualLocationIdentityDual.fxml"),
	SPATIAL_WORKING_MEMORY_UPDATE_TEST("/fxml/spatialWorkingMemoryUpdate.fxml");

	private final String _fxml;

	private Screens(final String fxml) {
		_fxml = fxml;
	}

	public String getFile() {
		return _fxml;
	}
}