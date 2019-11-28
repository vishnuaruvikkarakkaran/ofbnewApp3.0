package com.enfin.ofabee3.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    private ProgressDialog progressDialog;
    private static boolean exitStatus = false;
    public static DialogActionListener dialogActionListener;

    public interface DialogActionListener {

        public void onPositiveButtonClick();

        public void onNegativeButtonClick();

    }

    /**
     * Method for validating the email pattern
     *
     * @param email Email address
     * @return True-valid address else return false.
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Method for showing Alert dialog.
     *
     * @param context Application context
     * @param message Alert message
     */
    public static void onShowAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setIcon(R.drawable.ic_logo)
                .setTitle(R.string.text_error_message)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.text_ok, (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void onShowSnackbar(View parentView, String message) {
        Snackbar snackbar = Snackbar
                .make(parentView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showAlertDialog(Context context, String message) {
        final AlertDialog alert = new AlertDialog.Builder(context,R.style.MyDialogTheme).create();
        alert.setMessage(message);
        alert.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> alert.dismiss());
        alert.show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static Dialog showProgressDialog(Context context) {
        Dialog mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.custom_progress_view);
        mDialog.setCancelable(false);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return mDialog;
    }

    public static Dialog showProgressDialogCancelable(Context context) {
        Dialog mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.custom_progress_view);
        mDialog.setCancelable(true);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return mDialog;
    }


    public static boolean isValidName(final String name) {
        Pattern pattern;
        Matcher matcher;
        final String LASTNAME_PATTERN = "^[A-Za-z\\s]+$";
        pattern = Pattern.compile(LASTNAME_PATTERN);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean showExitDialog(Context context, String message, DialogActionListener listener) {
        dialogActionListener = listener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("Message");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                exitStatus = true;
                dialogActionListener.onPositiveButtonClick();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                exitStatus = false;
            }
        });
        builder.show();
        return exitStatus;
    }

    public static void showDiscountDialog(Context context, LayoutInflater mLayoutInflater, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog;
        LayoutInflater layoutInflater = mLayoutInflater;
        View couponView = layoutInflater.inflate(R.layout.coupon_appy_layout, null);
        ImageView close = couponView.findViewById(R.id.close_btn);
        builder.setView(couponView);
        builder.create();
        dialog = builder.show();

        close.setOnClickListener(view -> dialog.dismiss());

    }


    /**
     * Method for showing Alert dialog.
     *
     * @param context Application context
     * @param message Alert message
     */
    public static void onShowCustomAlertDialog(Activity context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (context).getLayoutInflater();
        //builder.setTitle(title);
        builder.setCancelable(false);
        builder.setView(inflater.inflate(R.layout.custom_alert_dialog, null))
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }
}
