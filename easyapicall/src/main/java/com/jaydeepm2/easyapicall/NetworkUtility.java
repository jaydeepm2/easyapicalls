package com.jaydeepm2.easyapicall;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.util.Objects;

public class NetworkUtility {
    static ProgressDialog dialog;

    /*ToDo: To display a progress dialog with text message*/
    public static void OpenLoadingDialog(Context context, String message) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*ToDo: To hide an open progress dialog*/
    public static void HideLoading() {
        if (dialog != null)
            dialog.hide();
    }
    /*ToDo: Check network state */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Objects.requireNonNull(connectivityManager).getActiveNetworkInfo() != null){
            return true;
        }
        else {
            return false;
        }
    }

    public static void Toast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
