package me.bestfudaye.puzzle;

import android.app.Application;

import me.bestfudaye.puzzle.utils.ToastUtil;

/**
 * Created by Ford on 2016/10/18.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.register(this);
    }
}
