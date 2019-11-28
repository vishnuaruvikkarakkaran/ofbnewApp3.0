package com.enfin.ofabee3.ui.module.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.explorecourse.response.ExploreCoursesResponse;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.ui.module.editprofile.EditProfileFragment;
import com.enfin.ofabee3.ui.module.explore.ExploreFragment;
import com.enfin.ofabee3.ui.module.inactiveuser.InActiveUserActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.userprofile.UserProfileFragment;
import com.enfin.ofabee3.ui.module.mycourses.MyCoursesFragment;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.FragmentActionListener;
import com.enfin.ofabee3.utils.MessageEvent;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;
import com.enfin.ofabee3.utils.errorhandler.ServerIsBrokenActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import hk.ids.gws.android.sclick.SClick;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener, RecyclerViewItemClickListener, FragmentActionListener,
        AppUtils.DialogActionListener, HomeContract.View {

    private Toolbar mToolbar;
    private AppCompatImageView toolbarMenu;
    private BottomNavigationView navigationView;
    private TextView toolbarTitle;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private BaseFragment fragment;
    private ArrayList<String> categoryIDList;
    private View dialogView;
    private Dialog dialog;
    private RecyclerView menuListRecyclerView;
    private ArrayList<String> menuList;
    private HomeMenuListAdapter menuListAdapter;
    private View viewSwipeMenu;
    private ImageView userAvatar;
    public HomeResponse responseData;
    public MyCoursesResponseModel myCoursesResponseModel;
    public ExploreCoursesResponse exploreCoursesResponse;
    private HomeContract.Presenter mPresenter;
    private Dialog progressDialog;
    private int currentItemID;
    private String destination, purchasedCourseName;
    private ShimmerFrameLayout mShimmerViewContainer;

    private int mInterval = 1000;
    private Handler mHandler;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = findViewById(R.id.toolbar);
        viewSwipeMenu = findViewById(R.id.view_swipe_menu);
        setSupportActionBar(mToolbar);
        toolbarMenu = findViewById(R.id.toolbar_menu);
        toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        toolbarMenu.setOnClickListener(v -> {
            toolbarMenu.setVisibility(View.INVISIBLE);
            initiatePopupWindow();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        destination = getIntent().getStringExtra(Constants.DESTINATION);
        purchasedCourseName = getIntent().getStringExtra(Constants.COURSE_NAME);
        currentItemID = R.id.navigation_home;
        progressDialog = AppUtils.showProgressDialog(this);
        mPresenter = new HomePresenter(getApplicationContext(), this);
        //hideNavUI();
        if (destination != null) {
            if (destination.equalsIgnoreCase("mycourse")) {
                navigationView.setVisibility(View.VISIBLE);
                loadMyCourse();
            }
        } else {
            mPresenter.getHomeData(getApplicationContext());
        }
        /*categoryIDList = (ArrayList<String>) getIntent().getSerializableExtra(Constants.CATEGORY_ID_LIST);
        for (int i = 0; i < categoryIDList.size(); i++) {
            OBLogger.e("IDS " + categoryIDList.get(i));
        }*/
        mHandler = new Handler();
    }

    private boolean loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1)
                getSupportFragmentManager().popBackStack();
            navigationView.setItemIconTintList(null);

            /*
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left)*/

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
            return true;
        } else {
            navigationView.setItemIconTintList(null);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
            return true;
        }
    }

    @Override
    public int getLayout() {
        setTheme(R.style.HomeScreenTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.homeNavigationBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.homeNavigationBarColor));
        }
        return R.layout.activity_home;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String tag = Constants.DEFAULT_FRAGMENT_TAG;
        if (item.getItemId() != currentItemID) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    invalidateOptionsMenu();
                    Bundle homeData = new Bundle();
                    homeData.putSerializable(Constants.HOME_DATA, responseData);
                    fragment = new HomeFragment();
                    fragment.setArguments(homeData);
                    setToolbarTitle("");
                    tag = Constants.HOME_FRAGMENT_TAG;
                    toolbarMenu.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    currentItemID = R.id.navigation_home;
                    break;

                case R.id.navigation_course:
                    searchView.onActionViewCollapsed();
                    invalidateOptionsMenu();
                    fragment = new MyCoursesFragment();
                    Bundle myCourseData = new Bundle();
                    myCourseData.putSerializable(Constants.MYCOURSE_DATA, myCoursesResponseModel);
                    fragment.setArguments(myCourseData);
                    setToolbarTitle("My Courses");
                    tag = Constants.MY_COURSE_FRAGMENT_TAG;
                    //navigationView.getMenu().getItem(0).setChecked(true);
                    toolbarMenu.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    currentItemID = R.id.navigation_course;
                    break;

                case R.id.navigation_explore:
                    searchView.onActionViewCollapsed();
                    invalidateOptionsMenu();
                    fragment = new ExploreFragment();
                    setToolbarTitle("Explore");
                    tag = Constants.EXPLORE_FRAGMENT_TAG;
                    navigationView.getMenu().getItem(0).setChecked(true);
                    toolbarMenu.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    currentItemID = R.id.navigation_explore;
                    break;

                case R.id.navigation_profile:
                    searchView.onActionViewCollapsed();
                    invalidateOptionsMenu();
                    fragment = new UserProfileFragment();
                    fragment.setFragmentChangeListener(this::onFragmentChanged);
                    setToolbarTitle("My Account");
                    tag = Constants.MY_ACCOUNT_FRAGMENT_TAG;
                    navigationView.getMenu().getItem(0).setChecked(true);
                    toolbarMenu.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    currentItemID = R.id.navigation_profile;
                    break;
            }

            return loadFragment(fragment, tag);
        } else
            return false;
    }

    private int getFragmentInstance(Fragment f) {
        if (f instanceof HomeFragment)
            return 1;
        else if (f instanceof HomeFragment)
            return 2;
        else if (f instanceof HomeFragment)
            return 3;
        else
            return 4;
    }

    private void initiatePopupWindow() {
        dialogView = View.inflate(this, R.layout.popup, null);
        menuListRecyclerView = dialogView.findViewById(R.id.menu_list_rv);
        userAvatar = dialogView.findViewById(R.id.user_avatar_iv);
        populateMenuData();
        menuListAdapter = new HomeMenuListAdapter(this, menuList);
        menuListAdapter.setItemClickListener(this::onItemSelected);
        menuListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuListRecyclerView.setAdapter(menuListAdapter);
        dialog = new Dialog(this, R.style.MyAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        ImageView imageView = dialog.findViewById(R.id.menu_close_btn);
        imageView.setOnClickListener(v -> {
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            revealShow(dialogView, false, dialog);
        });
        TextView home = dialog.findViewById(R.id.homeMenuDialog);
        home.setOnClickListener(v -> {
            revealShow(dialogView, false, dialog);
            //navigationView.setSelectedItemId(R.id.navigation_course);
        });

        dialog.setOnShowListener(dialogInterface -> revealShow(dialogView, true, null));

        dialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return true;
                currentItemID = R.id.navigation_home;
                revealShow(dialogView, false, dialog);
                return true;
            }
            return false;
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void populateMenuData() {
        OBDBHelper obdbHelper = new OBDBHelper(HomeActivity.this);
        LoginResponseModel.DataBean.UserBean user = obdbHelper.getUserDetails();

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(HomeActivity.this)
                .load(user.getUs_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userAvatar);
        menuList = new ArrayList<>();
        menuList.add("Home");
        //menuList.add("Notifications");
        //menuList.add("Messages");
        //menuList.add("Support Chat");
        //menuList.add("Bookmark");
        // menuList.add("Wishlist");
        // menuList.add("News & Events");
        ///menuList.add("FAQ");
        menuList.add("Invite Friend");
    }

    private void revealShow(View dialogView, boolean b, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        int cx = (int) (toolbarMenu.getX() + (toolbarMenu.getWidth() / 2));
        int cy = (int) (toolbarMenu.getY()) + toolbarMenu.getHeight() + 56;
        if (b) {
            if( ViewCompat.isAttachedToWindow(view)) {
                Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);
                view.setVisibility(View.VISIBLE);
                revealAnimator.setDuration(0);
                revealAnimator.start();
            }
        } else {
            if( ViewCompat.isAttachedToWindow(view)){
                Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        dialog.dismiss();
                        view.setVisibility(View.INVISIBLE);
                        toolbarMenu.setVisibility(View.VISIBLE);
                    }
                });
                anim.setDuration(0);
                anim.start();
            }
        }
    }

    public void setToolbarTitle(String title) {
        if (toolbarTitle != null)
            toolbarTitle.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        MenuItem settingsItem = menu.findItem(R.id.settings);
        if (settingsItem != null)
            settingsItem.setVisible(false);
        MenuItem saveItem = menu.findItem(R.id.save);
        if (saveItem != null)
            saveItem.setVisible(false);

        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Search Courses");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    OBLogger.e("OPENED");
                else
                    OBLogger.e("CLOSED");
            }
        });
        searchMenuItem.setVisible(false);

        /*searchView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        return false;
                    default:
                        break;
                }
            } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        return false;
                    default:
                        break;
                }
            }
            return false;
        });*/


        //EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        /*editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyAction, KeyEvent keyEvent) {
                if (
                    //Soft keyboard search
                        keyAction == EditorInfo.IME_ACTION_SEARCH ||
                                //Physical keyboard enter key
                                (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()
                                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                    return true;
                }
                return false;
            }
        });*/

        /*editText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    OBLogger.e("event", "captured");
                    return false;
                } else if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    OBLogger.e("Back event Trigered", "Back event");
                }
                return false;
            }
        });*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search:
                loadExplore();
                break;
        }
        return false;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (!isConnected) {
                    checkConnectivity();
                } else {
                    isConnected = true;
                    mPresenter.getHomeData(getApplicationContext());
                }

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        //mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    /**
     * Method for checking internet connectivity is present or not.
     */
    private void checkConnectivity() {
        if (NetworkUtil.isConnected(this)) {
            isConnected = true;
            mPresenter.getHomeData(getApplicationContext());
        }
        //else
        //splashPresenter.showNoInternetDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
        EventBus.getDefault().register(this);
        /*int fragmentSize = getSupportFragmentManager().getFragments().size();
        if (fragmentSize > 0) {
            Fragment fragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
            loadFragment(fragment, null);
            navigationView.setItemIconTintList(null);
        } else {
            fragment = new HomeFragment();
            loadFragment(fragment, Constants.HOME_FRAGMENT_TAG);
            setToolbarTitle("Home");
            navigationView.setItemIconTintList(null);
        }*/
        //startRepeatingTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
        EventBus.getDefault().unregister(this);
        onHideProgress();
        stopRepeatingTask();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        OBLogger.e("Submitted Search string " + s);
        if (fragment != null) {
            fragment.onSearchQuery(s);
        }
        hideKeyboard(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        OBLogger.e("Change Search strings " + s);
        if (fragment != null) {
            //if (s.length() > 0)
            fragment.onSearchQuery(s);
            return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(int position, boolean isSelected) {
        switch (position) {
            case 0:
                if (currentItemID != R.id.navigation_home)
                    loadHome();
                revealShow(dialogView, false, dialog);
                break;
            case 1:
                shareApp();
                revealShow(dialogView, false, dialog);
                break;
            case 2:
                revealShow(dialogView, false, dialog);
                break;
            case 3:
                revealShow(dialogView, false, dialog);
                break;
            case 4:
                revealShow(dialogView, false, dialog);
                break;
            case 5:
                revealShow(dialogView, false, dialog);
                break;
            case 6:
                revealShow(dialogView, false, dialog);
                break;
            case 7:
                revealShow(dialogView, false, dialog);
                break;
            case 8:
                shareApp();
                revealShow(dialogView, false, dialog);
                break;
            default:
                revealShow(dialogView, false, dialog);
                break;
        }
    }

    private void loadHome() {
        currentItemID = R.id.navigation_home;
        super.onBackPressed();
        setToolbarTitle("");
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void loadHomeAfterPurchase() {
        currentItemID = R.id.navigation_home;
        super.onBackPressed();
        setToolbarTitle("");
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).setChecked(true);
        mPresenter.getHomeData(getApplicationContext());
    }

    private void loadMyCourse() {
        currentItemID = R.id.navigation_course;
        setToolbarTitle("MyCourse");
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(1).setChecked(true);
        if (searchView != null)
            searchView.onActionViewCollapsed();
        invalidateOptionsMenu();
        fragment = new MyCoursesFragment();
        Bundle myCourseData = new Bundle();
        myCourseData.putSerializable(Constants.MYCOURSE_DATA, myCoursesResponseModel);
        fragment.setArguments(myCourseData);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        //fragmentTransaction.addToBackStack(Constants.MY_COURSE_FRAGMENT_TAG);
        fragmentTransaction.commit();
        //navigationView.getMenu().getItem(0).setChecked(true);
        toolbarMenu.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (purchasedCourseName != null)
            Toast.makeText(getApplicationContext(), "Thank You for purchasing " + purchasedCourseName + " Start Learning!", Toast.LENGTH_LONG).show();
    }

    private void loadExplore() {
        /*String tag = Constants.DEFAULT_FRAGMENT_TAG;
        fragment = new ExploreFragment();
        setToolbarTitle("Explore");
        tag = Constants.EXPLORE_FRAGMENT_TAG;
        navigationView.getMenu().getItem(0).setChecked(true);
        toolbarMenu.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        currentItemID = R.id.navigation_explore;
        loadFragment(fragment, tag);
        *//*searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.requestFocusFromTouch();*/
        //String tag = Constants.DEFAULT_FRAGMENT_TAG;
        //searchView.onActionViewCollapsed();
        //invalidateOptionsMenu();
        //fragment = new ExploreFragment();
        //setToolbarTitle("Explore");
        //tag = Constants.EXPLORE_FRAGMENT_TAG;
        //navigationView.getMenu().getItem(0).setChecked(true);
        //toolbarMenu.setVisibility(View.VISIBLE);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //currentItemID = R.id.navigation_explore;
        //loadFragment(fragment, tag);
        //navigationView.setSelectedItemId(R.id.navigation_explore);
        //onNavigationItemSelected(navigationView.getMenu().findItem(R.id.navigation_explore));
    }

    private void shareApp() {
        /*Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out ofabee app at: https://play.google.com/store/apps/details?id=com.enfin.ofabee3");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);*/
        ShareCompat.IntentBuilder.from(HomeActivity.this)
                .setType("text/plain")
                .setChooserTitle("Share App")
                .setText("http://play.google.com/store/apps/details?id=" + getPackageName())
                .startChooser();
    }

    public void updateHomeData(HomeResponse data) {
        responseData = data;
    }

    public HomeResponse getHomeData() {
        return responseData;
    }

    public void updateMyCourseData(MyCoursesResponseModel data) {
        myCoursesResponseModel = data;
    }

    public MyCoursesResponseModel getMyCourseData() {
        return myCoursesResponseModel;
    }

    public void updateExploreCourseData(ExploreCoursesResponse data) {
        exploreCoursesResponse = data;
    }

    public ExploreCoursesResponse getExploreCourseData() {
        return exploreCoursesResponse;
    }

    @Override
    public void onFragmentChanged(String fragmentTitle) {
        setToolbarTitle(fragmentTitle);
        if (fragmentTitle.equals(Constants.EDIT_PROFILE_FRAGMENT_TITLE)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new EditProfileFragment());
            fragmentTransaction.addToBackStack(Constants.EDIT_PROFILE_FRAGMENT_TAG);
            fragmentTransaction.commit();
            toolbarMenu.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            toolbarMenu.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        OBLogger.e("SIZE " + getSupportFragmentManager().getBackStackEntryCount());
        if (index >= 0) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            OBLogger.e("TAG " + tag);
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (tag.equals(Constants.EDIT_PROFILE_FRAGMENT_TAG)) {
                currentItemID = R.id.navigation_profile;
                setToolbarTitle("My Account");
                onFragmentChanged(Constants.MY_ACCOUNT_FRAGMENT_TITLE);
                super.onBackPressed();
            } else if (tag.equals(Constants.HOME_FRAGMENT_TAG)) {
                currentItemID = R.id.navigation_home;
                AppUtils.showExitDialog(this, "Do you want to exit application ?", this);
            } else {
                if (destination != null) {
                    OBLogger.e("BACK");
                    //finish();
                    //startActivity(getIntent());
                    destination = null;
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    navigationView.setSelectedItemId(R.id.navigation_home);
                    onNavigationItemSelected(navigationView.getMenu().findItem(R.id.navigation_home));
                    //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //super.onBackPressed();
                    //loadHomeAfterPurchase();
                    //destination=null;
                } else {
                    currentItemID = R.id.navigation_home;
                    OBLogger.e("INSTANCE ");
                    super.onBackPressed();
                    setToolbarTitle("");
                    navigationView.setItemIconTintList(null);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        } else {
            AppUtils.showExitDialog(this, "Do you want to exit application ?", this);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPositiveButtonClick() {
        getSupportFragmentManager().popBackStack(Constants.HOME_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        super.onBackPressed();
    }

    @Override
    public void onNegativeButtonClick() {
    }

    public static boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> void onSuccees(T type) {
        stopRepeatingTask();
        if (type instanceof HomeResponse) {
            //mShimmerViewContainer.stopShimmerAnimation();
            //mShimmerViewContainer.setVisibility(View.GONE);
            updateHomeData((HomeResponse) type);
            Bundle homeData = new Bundle();
            homeData.putSerializable(Constants.HOME_DATA, responseData);
            fragment = new HomeFragment();
            fragment.setArguments(homeData);
            loadFragment(fragment, Constants.HOME_FRAGMENT_TAG);
            setToolbarTitle("");
            mPresenter.getmycourses(getApplicationContext());
        } else if (type instanceof MyCoursesResponseModel) {
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            myCoursesResponseModel = (MyCoursesResponseModel) type;
        } else {
        }
        navigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public <T> void onFailure(T type) {
        //stopRepeatingTask();
        myCoursesResponseModel = new MyCoursesResponseModel();
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        OBLogger.e("FAILED");
        navigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onShowProgress() {
        /*if (progressDialog != null)
            progressDialog.show();*/
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowAlertDialog(String message) {
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(this, NoInternetActivity.class);
            startActivity(noInternet);
        } else
            onServerError(message);
    }

    @Override
    public void onServerError(String message) {
        //stopRepeatingTask();
        Intent errorOccured = new Intent(HomeActivity.this, ServerIsBrokenActivity.class);
        startActivity(errorOccured);
    }

    @Override
    public void noDataFound() {
        updateHomeData(new HomeResponse());
        Bundle homeData = new Bundle();
        homeData.putSerializable(Constants.HOME_DATA, responseData);
        fragment = new HomeFragment();
        fragment.setArguments(homeData);
        loadFragment(fragment, Constants.HOME_FRAGMENT_TAG);
        setToolbarTitle("");
        mPresenter.getmycourses(getApplicationContext());
    }

    @Override
    public void logoutAction(String message) {
        Intent userDeactivated = new Intent(HomeActivity.this, InActiveUserActivity.class);
        userDeactivated.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userDeactivated);
        finish();
    }

    public void getActiveFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        if (menuListAdapter != null)
            menuListAdapter.setCanStart(true);
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    /*// This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(SomeOtherEvent event) {
        doSomethingWith(event);
    }*/

}


