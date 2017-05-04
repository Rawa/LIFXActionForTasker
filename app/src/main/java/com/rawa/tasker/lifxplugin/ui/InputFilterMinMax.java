package com.rawa.tasker.lifxplugin.ui;

/**
 * Created by rawa on 2016-04-23.
 */
import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {
    private final String tag = "InputFilterMinMax";

    private final double min, max;

    public InputFilterMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            double input = Double.parseDouble(dest.toString() + source.toString());
            if(min <= input && input <= max)
                return null;
        } catch (NumberFormatException ignored) { }
        return "";
    }
}
