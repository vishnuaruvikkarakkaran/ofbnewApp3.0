package com.enfin.ofabee3.ui.module.editprofile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.editprofile.response.EditProfileResponse;
import com.enfin.ofabee3.data.remote.model.editprofile.response.ImageUploadResponse;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.ui.module.editprofile.corethread.ImageCompressTask;
import com.enfin.ofabee3.ui.module.editprofile.listener.IImageCompressTaskListener;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.AvenirTextView;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.FragmentActionListener;
import com.enfin.ofabee3.utils.MessageEvent;
import com.enfin.ofabee3.utils.MultiTextWatcher;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.errorhandler.ServerIsBrokenActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hk.ids.gws.android.sclick.SClick;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends BaseFragment implements EditProfileContract.View, IImageCompressTaskListener {

    private static final int PICK_IMAGE = 100;
    private EditProfileContract.Presenter mPresenter;
    private EditText userName, userEmail, userPhone;
    private TextView nameTV, nameTVError;
    private ImageView userAvatar;
    private FragmentActionListener listener;
    private FrameLayout profileFL;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    public static LoginResponseModel.DataBean.UserBean loginResponseModel;

    //single thread pool to image compression class.
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    private ImageCompressTask imageCompressTask;
    private String imageURL = "";
    private Dialog progressDialog;

    public EditProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mPresenter = new EditProfilePresenter(getActivity(), this);
        nameTV = rootview.findViewById(R.id.user_name);
        nameTVError = rootview.findViewById(R.id.name_error_tv);
        userName = rootview.findViewById(R.id.name_ti_et);
        userPhone = rootview.findViewById(R.id.phone_ti_et);
        userEmail = rootview.findViewById(R.id.email_ti_et);
        userAvatar = rootview.findViewById(R.id.profile_image);
        profileFL = rootview.findViewById(R.id.fl_profile_image);
        progressDialog = AppUtils.showProgressDialog(getActivity());
        clickActions();
        mPresenter.getUserDetails(getActivity());
        nameValidationTextwatcher();
        //EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
        return rootview;
    }

    private void nameValidationTextwatcher() {
        new MultiTextWatcher()
                .registerEditText(userName)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        switch (editText.getId()) {
                            case R.id.name_ti_et:
                                if (!TextUtils.isEmpty(userName.getText()) && !userName.getText().toString().trim().isEmpty()) {
                                    nameTVError.setVisibility(View.GONE);
                                } else {
                                    nameTVError.setVisibility(View.GONE);
                                }
                        }
                    }
                });
    }

    @Override
    public <T> void onSuccess(T type) {
        if (type instanceof LoginResponseModel.DataBean.UserBean) {
            loginResponseModel = (LoginResponseModel.DataBean.UserBean) type;
            updateProfile((LoginResponseModel.DataBean.UserBean) type);
        } else if (type instanceof Boolean) {
            if ((Boolean) type) {
                mPresenter.getUserDetails(getActivity());
            }
        } else if (type instanceof ImageUploadResponse) {
            imageURL = ((ImageUploadResponse) type).getData().getUpload_image();
        } else if (type instanceof EditProfileResponse) {
            mPresenter.updateuserdetails(getActivity(), loginResponseModel);
            saveAction();
        } else {
            onShowAlertDialog(getActivity().getString(R.string.something_went_wrong_text));
        }
    }

    @Override
    public <T> void onFailure(T type) {
        if (type instanceof Boolean) {
            if ((Boolean) type) {
                onShowAlertDialog(getActivity().getResources().getString(R.string.something_went_wrong_text));
            }
        }
    }

    @Override
    public void setPresenter(EditProfileContract.Presenter presenter) {
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.search);
        MenuItem saveItem = menu.findItem(R.id.save);
        MenuItem settingsItem = menu.findItem(R.id.settings);
        if (item != null)
            item.setVisible(false);

        if (saveItem != null)
            saveItem.setVisible(true);

        if (settingsItem != null)
            settingsItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.save: {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return true;
                if (!TextUtils.isEmpty(userName.getText()) && !userName.getText().toString().trim().isEmpty()) {
                    nameTVError.setVisibility(View.GONE);
                    OBDBHelper obdbHelper = new OBDBHelper(getActivity());
                    String userID = obdbHelper.getUserID();
                    if (!TextUtils.isEmpty(imageURL)) {
                        loginResponseModel.setId(userID);
                        loginResponseModel.setUs_name(userName.getText().toString());
                        loginResponseModel.setUs_email(userEmail.getText().toString());
                        loginResponseModel.setUs_phone(userPhone.getText().toString());
                        loginResponseModel.setUs_image(imageURL);
                        mPresenter.updateuserdetailsRemote(getActivity(), loginResponseModel);
                    }
                } else {
                    nameTVError.setVisibility(View.VISIBLE);
                }
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAction() {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
            ((HomeActivity) getActivity()).onFragmentChanged(Constants.MY_ACCOUNT_FRAGMENT_TITLE);
            /*Fragment fragment = fragmentManager.findFragmentByTag(Constants.EDIT_PROFILE_FRAGMENT_TAG);
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit();
                fragmentManager.popBackStack();
            }*/
            //if (fragmentManager.getBackStackEntryCount() > 0) {
            //}
            /*Fragment fragment = fragmentManager.findFragmentByTag(Constants.EDIT_PROFILE_FRAGMENT_TAG);
            if (fragment != null) {
                OBLogger.e("TITLEs");
                fragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit();
            }*/
            //fragmentManager.beginTransaction()
            // .remove(this)
            //.commit();
            //fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
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
    public void onServerError(String message) {
        Intent errorOccured = new Intent(getActivity(), ServerIsBrokenActivity.class);
        startActivity(errorOccured);
    }


    @Override
    public void setFragmentChangeListener(FragmentActionListener mListener) {
        listener = mListener;
    }

    private void updateProfile(LoginResponseModel.DataBean.UserBean user) {

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getActivity());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(getActivity())
                .load(user.getUs_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .error(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userAvatar);

        nameTV.setText("Hi " + user.getUs_name() + " !");
        userName.setText(user.getUs_name());
        userEmail.setText(user.getUs_email());
        userPhone.setText(user.getUs_phone());
        userName.setSelection(userName.getText().length());
        userPhone.setSelection(userPhone.getText().length());
        userEmail.setSelection(userEmail.getText().length());
        imageURL = user.getUs_image();
    }

    private void clickActions() {
        profileFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                if (checkPermission()) {
                    //Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                    openGallery();
                } else {
                    //Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
                    requestPermission();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writePermission && cameraPermission)
                        Snackbar.make(view, "Permission Granted, Now you can access external storage and camera.", Snackbar.LENGTH_LONG).show();
                    else {
                        Snackbar.make(view, "Permission Denied, You cannot access external storage and camera.", Snackbar.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need both permission ",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }

                break;

            default:
                OBLogger.e("Nothing to do");
                break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri returnUri = data.getData();
                    Bitmap bitmapImage = null;
                    //try {
                    if (getActivity() != null) {
                        try {
                            bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                            userAvatar.setImageBitmap(bitmapImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //extract absolute image path from Uri
                        Uri uri = data.getData();
                        String path = null;
                        InputStream inStream = null;
                        try {
                            inStream = getActivity().getContentResolver().openInputStream(uri);
                            //Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //get image path
                        // Will return "image:x*"
                        path = getRealPathFromURI(uri);
                        /*String wholeID = DocumentsContract.getDocumentId(uri);
                        OBLogger.e("IMAGE " + wholeID);
                        // Split at colon, use second item in the array
                        if (wholeID.contains(":")) {
                            String id = wholeID.split(":")[1];
                            String[] column = {MediaStore.Images.Media.DATA};
                            // where id is equal to
                            String sel = MediaStore.Images.Media._ID + "=?";
                            Cursor cursor = getActivity().getContentResolver().
                                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            column, sel, new String[]{id}, null);

                            int columnIndex = cursor.getColumnIndex(column[0]);
                            if (cursor.moveToFirst()) {
                                path = cursor.getString(columnIndex);
                            }
                            cursor.close();
                        } else {
                            path = getRealPathFromURI(uri);
                        }*/
                        imageCompressTask = new ImageCompressTask(getActivity(), path, this);
                        mExecutorService.execute(imageCompressTask);
                        /*if (uri != null && "content".equals(uri.getScheme())) {
                            Cursor cursor = getActivity().getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                            cursor.moveToFirst();
                            path = cursor.getString(0);
                            cursor.close();
                        } else {
                            path = uri.getPath();
                            OBLogger.e(" path " + path);
                            imageCompressTask = new ImageCompressTask(getActivity(), path, this);
                            mExecutorService.execute(imageCompressTask);
                        }*/

                    /*Cursor cursor = MediaStore.Images.Media.query(getActivity().getContentResolver(), uri, new String[]{MediaStore.Images.Media.DATA});
                        if (cursor != null && cursor.moveToFirst()) {
                             =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            OBLogger.e(" path " + path);
                            //Create ImageCompressTask and execute with Executor.
                            imageCompressTask = new ImageCompressTask(getActivity(), path, this);
                            mExecutorService.execute(imageCompressTask);*/
                    }
                }
                    /*} catch (IOException e) {
                        Toast.makeText(getActivity(), "Selected image is corrupted!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }*/
        }
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
        }
        return result;
    }


    @Override
    public void onComplete(List<File> compressed) {
        //photo compressed. Yay!
        File file = compressed.get(0);
        OBLogger.d("ImageCompressor" + "New photo size ==> " + file.length() + " path " + file.getAbsolutePath()); //log new file size.
        userAvatar.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        mPresenter.imageUpload(getActivity(), file);
    }

    @Override
    public void onError(Throwable error) {
        //very unlikely, but it might happen on a device with extremely low storage.
        OBLogger.e("ImageCompressor" + "Error occurred", error);
    }
}
