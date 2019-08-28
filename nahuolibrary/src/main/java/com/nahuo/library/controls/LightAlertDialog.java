package com.nahuo.library.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;


/**
 * Alert dialog using the Holo Light theme
 */
public class LightAlertDialog extends AlertDialog {

    /**
     * Create alert dialog
     *
     * @param context
     * @return dialog
     */
    public static AlertDialog create(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return new LightAlertDialog(context, THEME_HOLO_LIGHT);
        else
            return new LightAlertDialog(context);
    }

    private LightAlertDialog(final Context context, final int theme) {
        super(context, theme);
    }

    private LightAlertDialog(final Context context) {
        super(context);
    }

    /**
     * Alert dialog builder using the Holo Light theme
     */
    public static class Builder extends AlertDialog.Builder {

        /**
         * Create alert dialog builder
         *
         * @param context
         * @return dialog builder
         */
        public static LightAlertDialog.Builder create(final Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                return new LightAlertDialog.Builder(context, THEME_HOLO_LIGHT);
            else
                return new LightAlertDialog.Builder(context);
        }

        private Builder(Context context) {
            super(context);
        }

        private Builder(Context context, int theme) {
           super(context, theme);
        }
    }
}

