/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.rawa.tasker.lifxplugin.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rawa.tasker.lifxplugin.R;

import com.rawa.tasker.lifxplugin.bundle.BundleScrubber;
import com.rawa.tasker.lifxplugin.bundle.PluginBundleManager;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.State;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.Toggle;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.LifxConfig;
import com.rawa.tasker.lifxplugin.lifx.LifxCloudController;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Token;

import static com.rawa.tasker.lifxplugin.bundle.PluginBundleManager.generateBundle;

public final class EditActivity extends AbstractPluginActivity {
    private final String tag = "EditActivity";

    private EditText ET_apiToken;
    private Button B_getToken;
    private Button B_Test;

    private RadioGroup  RG_action;
    private RadioButton RB_actionState;
    private RadioButton RB_actionToggle;

    private CheckBox CB_Notifications;

    private final String settingsFragmentTag = "EDIT_ACTIIVTY_SETTINGINSFRAGMENT_TAG";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);
        // Fetch gui parts
        ET_apiToken    = (EditText) findViewById(R.id.api_token);
        B_getToken = (Button) findViewById(R.id.get_api_token);
        B_Test = (Button) findViewById(R.id.test);

        RG_action = (RadioGroup) findViewById(R.id.action_group);
        RB_actionState = (RadioButton) findViewById(R.id.state);
        RB_actionToggle = (RadioButton) findViewById(R.id.toggle);
        CB_Notifications = (CheckBox) findViewById(R.id.notificaitons);

        initOnClickListeners();

        // Restore if there is a saved instance
        if (null != savedInstanceState)
            return;

        if (PluginBundleManager.isBundleValid(localeBundle)) {
            final String apitoken =
                    localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_API_TOKEN);
            ET_apiToken.setText(apitoken);

            final String action = localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_ACTION);

            switch (action) {
                case State.ACTION:
                    RB_actionState.setChecked(true);
                    Log.d(tag, "restoreState");
                    restoreState(localeBundle);
                    break;
                case Toggle.ACTION:
                    RB_actionToggle.setChecked(true);
                    Log.d(tag, "restoreToggle");
                    restoreToggle(localeBundle);
                    break;
            }

            final boolean debug =
                    localeBundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_DEBUG);
            CB_Notifications.setChecked(debug);
        } else {
            Log.d("LIFXPlugin", "Forced statefragment");
            switchSettingsFragment(new StateSettingsFragment());
        }
    }

    // Initiate OnClickListeners
    private void initOnClickListeners(){
        B_getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://cloud.lifx.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        RB_actionState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment stateFragment = new StateSettingsFragment();
                stateFragment.setArguments(getIntent().getExtras());
                switchSettingsFragment(stateFragment);
            }
        });

        RB_actionToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment toggleFragment = new ToggleSettingsFragment();
                toggleFragment.setArguments(getIntent().getExtras());
                switchSettingsFragment(toggleFragment);
            }
        });

        B_Test.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LifxCloudController lifxCloudController =
                        new LifxCloudController(getApplicationContext());
                LifxConfig lifxConfig = getLifxConfig();
                lifxConfig.setDebugEnabled(true);
                lifxCloudController.performLIFXAction(lifxConfig);
            }
        });
    }

    private void switchSettingsFragment(Fragment fragment){
        fragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, settingsFragmentTag).commit();

    }

    private void restoreState(Bundle localeBundle) {
        Fragment stateFragment = new StateSettingsFragment();
        stateFragment.setArguments(localeBundle);
        switchSettingsFragment(stateFragment);
    }

    private void restoreToggle(Bundle localeBundle) {
        Fragment toggleFragment = new ToggleSettingsFragment();
        toggleFragment.setArguments(localeBundle);
        switchSettingsFragment(toggleFragment);
    }

    private LifxConfig getLifxConfig(){
        AAPICall callConfig = getFragmentCallConfig();

        final String tokenString = ET_apiToken.getText().toString();
        final Token token = new Token(tokenString);

        String selector = getSelector();

        final boolean debug = CB_Notifications.isChecked();

        return new LifxConfig(token, callConfig, debug);
    }

    @Override
    public void finish() {
        Log.d("LIFXPlugin", "FINSHED");
        if (!isCanceled()) {
            Log.d("LIFXPlugin", "NOT CANCELD");
            final String tokenString = ET_apiToken.getText().toString();
            final Token token = new Token(tokenString);
            final boolean debug = CB_Notifications.isChecked();

            final Intent resultIntent = new Intent();

            Log.d("LIFXPlugin", "OK TOKEN");
            /*
             * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note
             * that anything placed in this Bundle must be available to Locale's class loader. So storing
             * String, int, and other standard objects will work just fine. Parcelable objects are not
             * acceptable, unless they also implement Serializable. Serializable objects must be standard
             * Android platform objects (A Serializable class private to this plug-in's APK cannot be
             * stored in the Bundle, as Locale's classloader will not recognize it).
             */
            AAPICall callConfig = getFragmentCallConfig();
            LifxConfig lifxConfig = new LifxConfig(token, callConfig, debug);

            final Bundle resultBundle = generateBundle(this, lifxConfig);
            resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

            /*
             * The blurb is concise status text to be displayed in the host's UI.
             */
            final String blurb = generateBlurb(this, lifxConfig);
            resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

            setResult(RESULT_OK, resultIntent);
        }

        super.finish();
    }


    /* package */
    static String generateBlurb(final Context context, LifxConfig lifxConfig) {
        String message = lifxConfig.toString();
        // What is shown as a summery of all settings
        final int maxBlurbLength =
                context.getResources().getInteger(R.integer.twofortyfouram_locale_maximum_blurb_length);

        if (message.length() > maxBlurbLength)
        {
            return message.substring(0, maxBlurbLength);
        }
        return message;
    }

    private AAPICall getFragmentCallConfig() {
        AbstractSettingsFragment settingsFragment = (AbstractSettingsFragment) getFragmentManager().findFragmentByTag(settingsFragmentTag);
        return settingsFragment == null ? null : settingsFragment.getCallConfig();
    }

    public String getSelector() {
        return "all";
    }
}
