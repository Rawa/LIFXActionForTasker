package com.rawa.tasker.lifxplugin.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rawa.tasker.lifxplugin.R;
import com.rawa.tasker.lifxplugin.bundle.PluginBundleManager;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.State;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Brightness;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Color;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Duration;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Power;

public class StateSettingsFragment extends AbstractSettingsFragment {
    private final String tag = "StateSettingsFragment";

    private EditText ET_duration;
    private EditText ET_brightness;
    private EditText ET_color;

    private RadioGroup  RG_power;
    private RadioButton RB_powerOn;
    private RadioButton RB_powerOff;
    private RadioButton RB_powerUnchanged;

    private Button B_colorFormat;

    public StateSettingsFragment() {
        // Required empty public constructor
    }

    public static StateSettingsFragment newInstance(String power) {
        StateSettingsFragment fragment = new StateSettingsFragment();
        Bundle args = new Bundle();
        args.putString(PluginBundleManager.BUNDLE_EXTRA_STRING_POWER, power);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_state_settings, container, false);

        ET_duration    = (EditText) v.findViewById(R.id.duration);
        ET_brightness    = (EditText) v.findViewById(R.id.brightness);
        ET_color       = (EditText) v.findViewById(R.id.color);
        RG_power    = (RadioGroup) v.findViewById(R.id.power_group);
        RB_powerOn  = (RadioButton) v.findViewById(R.id.power_on);
        RB_powerOff = (RadioButton) v.findViewById(R.id.power_off);
        RB_powerUnchanged = (RadioButton) v.findViewById(R.id.power_unchanged);

        ET_duration.setFilters(new InputFilter[]{ new InputFilterMinMax(Duration.MIN, Duration.MAX)});
        ET_brightness.setFilters(new InputFilter[]{ new InputFilterMinMax(Brightness.MIN, Brightness.MAX)});

        B_colorFormat = (Button) v.findViewById(R.id.color_format);

        B_colorFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://api.developer.lifx.com/v1/docs/colors";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if (getArguments() != null) {

            final String power = getArguments().getString(PluginBundleManager.BUNDLE_EXTRA_STRING_POWER, "on");
            switch (power){
                case "on":
                    RB_powerOn.setChecked(true);
                    break;
                case "off":
                    RB_powerOff.setChecked(true);
                    break;
                case "unchanged":
                    RB_powerUnchanged.setChecked(true);
                    break;
            }

            if(getArguments().containsKey(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_DURATION)) {
                final Double duration =
                        getArguments().getDouble(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_DURATION);
                ET_duration.setText(String.valueOf(duration));
            }

            if(getArguments().containsKey(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_BRIGHTNESS)) {
                final Double brightness =
                        getArguments().getDouble(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_BRIGHTNESS);
                ET_brightness.setText(String.valueOf(brightness));
            }

            final String color =
                    getArguments().getString(PluginBundleManager.BUNDLE_EXTRA_STRING_COLOR);
            ET_color.setText(color);
        }
        return v;
    }

    private Power getPower(){
        int selectedPowerId = RG_power.getCheckedRadioButtonId();
        Power power = null;
        switch(selectedPowerId){
            case R.id.power_on:
                power = Power.ON;
                break;
            case R.id.power_off:
                power = Power.OFF;
                break;
            case R.id.power_unchanged:
                power = null;
        }
        return power;
    }

    @Override
    public AAPICall getCallConfig() {
        return new State(getDuration(), getBrightness(), getPower(), getColor());
    }

    private Duration getDuration() {
        String durationString = ET_duration.getText().toString();
        if(durationString.isEmpty())
            return null;

        return new Duration(Double.parseDouble(durationString));
    }

    private Brightness getBrightness() {
        String brightnessString = ET_brightness.getText().toString();
        if(brightnessString.isEmpty())
            return null;

        return new Brightness(Double.parseDouble(brightnessString));
    }

    private Color getColor() {
        String colorString = ET_brightness.getText().toString();
        if(colorString.isEmpty())
            return null;

        return new Color(colorString);
    }
}
