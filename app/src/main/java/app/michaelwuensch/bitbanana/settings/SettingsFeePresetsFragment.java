package app.michaelwuensch.bitbanana.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import app.michaelwuensch.bitbanana.R;


public class SettingsFeePresetsFragment extends PreferenceFragmentCompat {

    private static final String LOG_TAG = SettingsFeePresetsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the settings from an XML resource
        setPreferencesFromResource(R.xml.settings_on_chain_fees, rootKey);
    }
}
