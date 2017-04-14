package me.bestfudaye.puzzle.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ford on 2016/10/18.
 */
public class ToastUtil {

    private static Context sContext;

    private ToastUtil() {
    }

    public static void register(Context context){
        sContext = context.getApplicationContext();
    }

    public static void showShort(String s){
        Toast.makeText(sContext,s,Toast.LENGTH_SHORT).show();
    }
}
