package com.enfin.ofabee3.ui.module.inactiveuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.pixplicity.easyprefs.library.Prefs;

public class InActiveUserActivity extends BaseActivity implements InActiveUserContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_in_active_user);
        findViewById(R.id.exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.get(InActiveUserActivity.this).clearMemory();
                Prefs.clear();
                OBDBHelper dbHelper = new OBDBHelper(InActiveUserActivity.this);
                dbHelper.deleteUserData();
                logoutAction("logout");
            }
        });
    }

    @Override
    public int getLayout() {
        /*setTheme(R.style.HomeScreenTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.homeNavigationBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.homeNavigationBarColor));
        }*/
        return R.layout.activity_in_active_user;
    }

    @Override
    public <T> void onSuccees(T type) {

    }

    @Override
    public <T> void onFailure(T type) {

    }

    @Override
    public void logoutAction(String message) {
        //Toast.makeText(InActiveUserActivity.this, "Your account has been temporarily deactivated by the Admin!", Toast.LENGTH_SHORT).show();
        Intent login = new Intent(InActiveUserActivity.this, LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(login);
        finish();
    }

    @Override
    public void setPresenter(InActiveUserContract.Presenter presenter) {

    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

    @Override
    public void onShowAlertDialog(String message) {

    }

    @Override
    public void onServerError(String message) {

    }
}
