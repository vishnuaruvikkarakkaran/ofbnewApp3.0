package com.enfin.ofabee3.ui.module.testpackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.utils.OBLogger;

public class TestActivity extends AppCompatActivity implements TestContract.View {

    TestContract.Presenter presenter;
    TestContract.Model model;
    Button btn1;
    boolean gone = false;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        model = new TestModel();
        presenter = new TestPresenter(this, model);
        //presenter.test();

        btn1 = (Button) findViewById(R.id.btn1);
        CoordinatorLayout ln = (CoordinatorLayout) findViewById(R.id.parent_ll);

        ln.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!gone) {
                        btn1.setVisibility(View.GONE);
                        gone = true;
                    } else {
                        btn1.setVisibility(View.VISIBLE);
                        gone = false;

                    }


                }
                return true;
            }
        });

        handler.post(runnableCode);
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            btn1.setVisibility(View.GONE);
            gone = true;
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    public void initView() {
    }

    @Override
    public void setViewData(String data) {
        OBLogger.e(data);
        OBLogger.e(model.getData());
    }
}
