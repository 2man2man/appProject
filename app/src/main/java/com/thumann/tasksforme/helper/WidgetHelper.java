package com.thumann.tasksforme.helper;

import android.widget.TextView;

public class WidgetHelper {

    public static String getTextFromWidget(TextView textWidget) {
        String result = null;
        if (textWidget != null && textWidget.getText() != null) {
            result = textWidget.getText().toString();
        }
        return result;
    }

}
