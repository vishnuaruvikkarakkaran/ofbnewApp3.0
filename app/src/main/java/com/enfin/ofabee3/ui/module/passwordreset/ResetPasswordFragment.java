package com.enfin.ofabee3.ui.module.passwordreset;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.forgotpassword.response.ResetPasswordResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.AvenirEditText;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.MultiTextWatcher;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.OtpEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.ids.gws.android.sclick.SClick;

public final class ResetPasswordFragment extends BaseFragment implements ResetPasswordContract.View {

    private ResetPasswordContract.Presenter mPresenter;
    private OtpEditText otpEditText;
    Button continueButton;
    private AvenirEditText newPasswordET, confirmPasswordET;
    private String userNumber, userPassword, userEmail;
    TextView otpDetailText, passwordError, otpError;
    private Dialog progressDialog;

    public ResetPasswordFragment() {
    }

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public void setPresenter(ResetPasswordContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password_layout, container, false);
        ButterKnife.bind(getActivity(), view);
        continueButton = view.findViewById(R.id.continueButton);
        newPasswordET = view.findViewById(R.id.newPasswordEditText);
        confirmPasswordET = view.findViewById(R.id.confirmPasswordEditText);
        otpDetailText = view.findViewById(R.id.otpDetailTextView);
        otpEditText = view.findViewById(R.id.otpEditText);
        passwordError = view.findViewById(R.id.password_error_tv);
        otpError = view.findViewById(R.id.otp_error_tv);
        getInitilValues();
        progressDialog = AppUtils.showProgressDialog(getActivity());
        //Email field edit text listener for paste event.
        confirmPasswordET.addListener(() -> validatePassword(Objects.requireNonNull(confirmPasswordET.getText()).toString().trim()));

        //Text watcher for validation.
        new MultiTextWatcher()
                .registerEditText(confirmPasswordET)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {
                        //validatePassword(editText.getText().toString().trim());
                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        if (editText.getId() == R.id.confirmPasswordEditText) {
                            validatePassword(editable.toString().trim());
                        }
                    }
                });

        /*temporary code */
        continueButton.setOnClickListener(view1 -> {
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            updatePasswordAPI();
        });

        otpEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                //updatePasswordAPI();
                ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                return true;
            }
            return false;
        });
        return view;
    }

    private void updatePasswordAPI() {
        if (NetworkUtil.isConnected(getActivity())) {
            if (!TextUtils.isEmpty(userEmail)) {
                if (!TextUtils.isEmpty(newPasswordET.getText().toString().trim())
                        && newPasswordET.getText().toString().trim().length() > 5 && otpEditText.getText().length() == 5) {
                    passwordError.setVisibility(View.GONE);
                    otpError.setVisibility(View.GONE);
                    mPresenter.updatePassword(getActivity(), "name", "code", "number", userEmail, newPasswordET.getText().toString().trim(), otpEditText.getText().toString(), "source");
                } else if (newPasswordET.getText().toString().trim().length() > 0 && newPasswordET.getText().toString().trim().length() < 6)
                    onShowError(getActivity().getString(R.string.password_format_error_text));
                else if (otpEditText.getText().length() != 5)
                {
                    otpError.setVisibility(View.VISIBLE);
                    otpError.setText(R.string.otp_invalid_text);
                }
                else
                    onShowError(getActivity().getString(R.string.password_invalid_text));
            } else
                onShowError(getActivity().getString(R.string.something_went_wrong_text));
        } else {
            AppUtils.onShowAlertDialog(getActivity(), "Please check your internet connection!!");
            //onShowError(getActivity().getString(R.string.check_internet_text));
        }
    }

    private void validatePassword(String userConfirmPassword) {
        if (userConfirmPassword.equalsIgnoreCase(newPasswordET.getText().toString().trim())) {
            confirmPasswordET.setError(null);
            confirmPasswordET.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        } else {
            confirmPasswordET.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void getInitilValues() {
        Bundle extras = this.getArguments();
        if (extras != null) {
            userNumber = extras.getString(Constants.USER_NUMBER);
            userEmail = extras.getString(Constants.USER_EMAIL);
            otpDetailText.setText(getActivity().getString(R.string.otp_detail_text).concat(" " + userEmail + "."));
        }
    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (confirmPasswordET.getText().toString().trim().length() > 0)
            validatePassword(confirmPasswordET.getText().toString());
    }

    @Override
    public void onShowAlertDialog(String message) {
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(getActivity(), NoInternetActivity.class);
            startActivity(noInternet);
        } else
            AppUtils.onShowAlertDialog(getActivity(), message);
    }

    @Override
    public <T> void onSuccees(Context context, T type) {
        if (type instanceof ResetPasswordResponse) {
            ResetPasswordResponse responseModel = (ResetPasswordResponse) type;
            if (responseModel.getMetadata().getStatus_code().equalsIgnoreCase(Constants.SUCCESS_)) {
                OBLogger.d("Success Response " + responseModel.getMetadata().getMessage());
                if (context != null)
                    Toast.makeText(context, responseModel.getMetadata().getMessage(), Toast.LENGTH_SHORT).show();
                Intent intentHome = new Intent(getActivity(), LoginActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
            } else if (responseModel.getMetadata().getStatus_code().equalsIgnoreCase(Constants.ERROR_401_)) {
                if (context != null) {
                    Toast.makeText(context, responseModel.getMetadata().getMessage(), Toast.LENGTH_SHORT).show();
                    otpError.setVisibility(View.VISIBLE);
                    otpError.setText(R.string.otp_invalid_text);
                }
            }
        }
    }

    @Override
    public <T> void onFailure(Context context, T type) {
        if (type instanceof String) {
            String responseError = (String) type;
            OBLogger.e("RESPONSE ERROR " + responseError);
            if (context != null)
                Toast.makeText(context, responseError, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShowError(String message) {
        passwordError.setVisibility(View.VISIBLE);
        passwordError.setText(message);
    }

    @Override
    public void onServerError(String message) {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnResetPasswordFragmentInteractionListener {
        // TODO: Update argument type and name
        void onResetPasswordFragmentInteraction();
    }
}
