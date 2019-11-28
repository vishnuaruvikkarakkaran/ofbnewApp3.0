package com.enfin.ofabee3.utils.errorhandler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.BaseActivity;

public class ServerIsBrokenActivity extends BaseActivity {

    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_server_is_broken;
    }
}
