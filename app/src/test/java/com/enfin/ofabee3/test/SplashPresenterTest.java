package com.enfin.ofabee3.test;

import android.content.Context;

import com.enfin.ofabee3.ui.module.splash.SplashContract;
import com.enfin.ofabee3.ui.module.splash.SplashPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created By Athul on 14-07-2019.
 * local unit test for the splash Presenter.
 */
public class SplashPresenterTest {

    @Mock
    private Context mockContext;

    @Mock
    private SplashContract.MvpView mvpView;

    private SplashPresenter mPresenter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new SplashPresenter(mvpView);
    }

    @Test
    public void getUserCountry() {
        mPresenter.getUserCountry(mockContext);
        verify(mvpView).openNextActivity();
    }

    @Test
    public void showNoInternetDialog() {
    }

    @Test
    public void startActivityByLoginStatus() {
    }

    @Test
    public void onRetry() {
        mPresenter.onRetry();
        verify(mvpView).reloadPage();
    }
}