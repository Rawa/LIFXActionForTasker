package com.rawa.tasker.lifxplugin.ui;

import android.app.Fragment;

import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;

/**
 * Created by rawa on 2016-03-05.
 */
public abstract class AbstractSettingsFragment extends Fragment {

    public abstract AAPICall getCallConfig();

}
