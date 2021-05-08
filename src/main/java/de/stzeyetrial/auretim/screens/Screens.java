package de.stzeyetrial.auretim.screens;

/**
 * @author strasser
 */
public enum Screens {
	MAIN("/fxml/Main.fxml"),
	SETTINGS("/fxml/SettingsLegacy.fxml"),
	TEST("/fxml/pvtAuditory.fxml"),
	VISUAL_PVT("/fxml/pvtVisual.fxml"),
	RESULT("/fxml/Result.fxml"),
	DIRECTORY_CHOOSER("/fxml/DirectoryChooser.fxml"),
	PVT_AUDITORY("/fxml/pvtAuditory.fxml"),
	PVT_VISUAL("/fxml/pvtVisual.fxml"),
	N_BACK_VISUAL_STIMULUS_IDENTITY("/fxml/nBackVisualStimulusIdentity.fxml"),
	N_BACK_VISUAL_LOCATION_IDENTITY("/fxml/nBackVisualLocationIdentity.fxml"),
	N_BACK_AUDITORY_STIMULUS_IDENTITY("/fxml/nBackAuditoryStimulusIdentity.fxml"),
	N_BACK_DUAL_AUDITORY_VISUAL_LOCATION_IDENTITY("/fxml/nBackDualAuditoryVisualLocationIdentity.fxml"),
	N_BACK_DUAL_AUDITORY_VISUAL_STIMULUS_IDENTITY("/fxml/nBackDualAuditoryVisualStimulusIdentity.fxml"),
	N_BACK_DUAL_VISUAL_VISUAL_STIMULUS_IDENTITY_LOCATION_IDENTITY("/fxml/nBackDualVisualVisualStimulusIdentityLocationIdentity.fxml"),
	MACKWORTH_CLOCK("/fxml/mackworthClock.fxml"),
	SPATIAL_WORKING_MEMORY("/fxml/spatialWorkingMemory.fxml");

	private final String _fxml;

	private Screens(final String fxml) {
		_fxml = fxml;
	}

	public String getFile() {
		return _fxml;
	}
}