package com.enfin.ofabee3.ui.module.userprofile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.BuildConfig;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.editprofile.response.MyProfileResponse;
import com.enfin.ofabee3.data.remote.model.logout.response.LogoutBean;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.ui.module.coursecategories.MyCourseCategoryListActivity;
import com.enfin.ofabee3.ui.module.explore.ExploreFragment;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.inactiveuser.InActiveUserActivity;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.FragmentActionListener;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.OpenSansTextView;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;
import com.enfin.ofabee3.utils.errorhandler.ServerIsBrokenActivity;
import com.pixplicity.easyprefs.library.Prefs;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;

public class UserProfileFragment extends BaseFragment implements UserProfileContract.View, RecyclerViewItemClickListener {

    UserProfileContract.Presenter mPresenter;
    ImageView userAvatar;
    private TextView userName, userEmail, userContactNumber, rewardValueTV;
    private LinearLayout rewrardsLL;
    //private LinearLayout userDetailsLL, settingsLL;
    private ArrayList<String> settingsList;
    private RecyclerView settingsRecyclerView;
    private FragmentActionListener listener;
    private Dialog progressDialog;
    private UserSettingsListAdapter settingsListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, null);
        mPresenter = new UserProfilePresenter(getActivity(), this);
        progressDialog = AppUtils.showProgressDialog(getActivity());
        userAvatar = rootview.findViewById(R.id.profile_image);
        userName = rootview.findViewById(R.id.user_name);
        userEmail = rootview.findViewById(R.id.user_email);
        userContactNumber = rootview.findViewById(R.id.user_phone);
        rewrardsLL = rootview.findViewById(R.id.ll_rewards);
        rewardValueTV = rootview.findViewById(R.id.reward_text_value);
        //userDetailsLL = rootview.findViewById(R.id.user_details_ll);
        //settingsLL = rootview.findViewById(R.id.settings_ll);
        settingsRecyclerView = rootview.findViewById(R.id.settings_list_rv);
        mPresenter.getUserDetails(getActivity());
        mPresenter.getProfileDeatails(getActivity());
        return rootview;
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setFragmentChangeListener(FragmentActionListener mListener) {
        super.setFragmentChangeListener(mListener);
        this.listener = mListener;
    }

    @Override
    public <T> void onSuccess(T type) {
        if (type instanceof LoginResponseModel.DataBean.UserBean) {
            updateProfile((LoginResponseModel.DataBean.UserBean) type);
        } else if (type instanceof LogoutBean) {
            Toast.makeText(getActivity(), "Logout successfully", Toast.LENGTH_SHORT).show();
            mPresenter.clearLocalDB(getActivity());
            Intent login = new Intent(getActivity(), LoginActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(login);
            getActivity().finish();
        } else if (type instanceof MyProfileResponse) {
            if (getActivity() != null) {
                rewrardsLL.setVisibility(View.VISIBLE);
                rewardValueTV.setText(((MyProfileResponse) type).getData().getScore());
                /*Glide.with(getActivity())
                        .load(((MyProfileResponse) type).getData().getUs_image())
                        .centerCrop()
                        .placeholder(R.drawable-mdpi.man)
                        .into(userAvatar);*/
                //userName.setText(((MyProfileResponse) type).getData().getUs_name());
                //userEmail.setText(((MyProfileResponse) type).getData().getUs_email());
                //userContactNumber.setText(((MyProfileResponse) type).getData().getUs_phone());
            }
        } else if (type instanceof String) {
            if (((String) type).equalsIgnoreCase(Constants.DB_CLEAR)) {
                Toast.makeText(getActivity(), "Logout successfully", Toast.LENGTH_SHORT).show();
                Intent login = new Intent(getActivity(), LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
                getActivity().finish();
            }
        }
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
        userName.setText("Hi " + user.getUs_name() + " !");
        userEmail.setText(user.getUs_email());
        userContactNumber.setText(user.getUs_phone());
        /*Remove this line of code in future while adding data in my account*/
        updateLayouts();
    }

    @Override
    public <T> void onFailure(T type) {
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
        } else {
            onServerError(message);
            //AppUtils.onShowAlertDialog(getActivity(), message);
        }
    }

    @Override
    public void onServerError(String message) {
        Intent errorOccured = new Intent(getActivity(), ServerIsBrokenActivity.class);
        startActivity(errorOccured);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        MenuItem saveItem = menu.findItem(R.id.save);
        MenuItem settingsItem = menu.findItem(R.id.settings);

        if (item != null)
            item.setVisible(false);

        if (saveItem != null)
            saveItem.setVisible(false);

        if (settingsItem != null)
            settingsItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                updateLayouts();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateLayouts() {
        //rewrardsLL.setVisibility(View.GONE);

        LinearLayout.LayoutParams paramsUser = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);

        LinearLayout.LayoutParams paramsBody = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);

        paramsUser.weight = 0.7f;
        paramsBody.weight = 1.3f;

        //userDetailsLL.setLayoutParams(paramsUser);
        //settingsLL.setLayoutParams(paramsBody);
        populateSettingsData();
        settingsListAdapter = new UserSettingsListAdapter(getActivity(), settingsList);
        settingsListAdapter.setItemClickListener(this::onItemSelected);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        settingsRecyclerView.setAdapter(settingsListAdapter);

    }

    private void populateSettingsData() {
        settingsList = new ArrayList<>();
        settingsList.add("Edit Profile");
        settingsList.add("Change Category");
        //settingsList.add("Notifications");
        //settingsList.add("Support");
        settingsList.add("Privacy Policy");
        settingsList.add("Terms of Use");
        settingsList.add("Invite Friend");
        settingsList.add("Sign Out");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(int position, boolean isSelected) {
        switch (position) {
            case 0:
                editProfile();
                break;
            case 1:
                changeCategory();
                break;
            case 2:
                showPrivacyPolicy();
                break;
            case 3:
                showTermsAndCondition();
                break;
            case 4:
                shareApp();
                break;
            case 5:
                signout();
                break;
        }
    }

    private void showPrivacyPolicy() {
        new FinestWebView.Builder(getActivity()).show(BuildConfig.PRIVACY_POLICY_URL);
    }

    private void changeCategory() {
        Intent courseCategory = new Intent(getActivity(), MyCourseCategoryListActivity.class);
        courseCategory.putExtra(Constants.USER_SOURCE, "PROFILE");
        startActivity(courseCategory);
        //getActivity().finish();
    }

    private void showTermsAndCondition() {
        new FinestWebView.Builder(getActivity()).show(BuildConfig.TERMS_CONDITION_URL);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settingsListAdapter != null)
            settingsListAdapter.setCanStart(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
        onHideProgress();
    }

    private void editProfile() {
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new EditProfileFragment());
        fragmentTransaction.addToBackStack(Constants.EDIT_PROFILE_FRAGMENT_TAG);
        fragmentTransaction.commit();*/
        listener.onFragmentChanged(Constants.EDIT_PROFILE_FRAGMENT_TITLE);
    }

    private void shareApp() {
        /*Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out ofabee app at: https://play.google.com/store/apps/details?id=com.enfin.ofabee3");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);*/
        ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setChooserTitle("Share App")
                .setText("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())
                .startChooser();
    }

    private void signout() {
        Glide.get(getActivity()).clearMemory();
        Prefs.clear();
        mPresenter.clearLocalDB(getActivity());
        //ExploreFragment.re
        //mPresenter.logout(getActivity());
        /*Intent login = new Intent(getActivity(), LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(login);
        getActivity().finish();*/
    }

    @Override
    public void logoutAction(String message) {
        Intent userDeactivated = new Intent(getActivity(), InActiveUserActivity.class);
        userDeactivated.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userDeactivated);
        getActivity().finish();
    }
}
