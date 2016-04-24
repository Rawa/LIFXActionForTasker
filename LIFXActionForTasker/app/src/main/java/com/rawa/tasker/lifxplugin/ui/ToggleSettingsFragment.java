package com.rawa.tasker.lifxplugin.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rawa.tasker.lifxplugin.R;
import com.rawa.tasker.lifxplugin.bundle.PluginBundleManager;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.Toggle;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Duration;

public class ToggleSettingsFragment extends AbstractSettingsFragment {
    private final String tag = "StateSettingsFragment";

    private EditText ET_duration;

    public ToggleSettingsFragment() {
        // Required empty public constructor
    }

    public static ToggleSettingsFragment newInstance() {
        ToggleSettingsFragment fragment = new ToggleSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_toggle_settings, container, false);
        ET_duration = (EditText) v.findViewById(R.id.duration);
        ET_duration.setFilters(new InputFilter[]{ new InputFilterMinMax(Duration.MIN, Duration.MAX)});
        if (getArguments() != null) {

            if(getArguments().containsKey(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_DURATION)) {
                final Double duration =
                        getArguments().getDouble(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_DURATION);
                ET_duration.setText(String.valueOf(duration));
            }
        }
        return v;
    }

    @Override
    public AAPICall getCallConfig() {
        return new Toggle(getDuration());
    }

    private Duration getDuration() {
        String durationString = ET_duration.getText().toString();
        if(durationString.isEmpty()){
            return null;
        }
        return new Duration(Double.parseDouble(durationString));
    }
}
