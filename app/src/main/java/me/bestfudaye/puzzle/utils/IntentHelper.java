package me.bestfudaye.puzzle.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import me.bestfudaye.puzzle.constants.Constants;
import me.bestfudaye.puzzle.ui.PuzzleDetailActivity;

/**
 * Created by Ford on 2016/10/24.
 */
public class IntentHelper {

    public static void gotoPuzzleDetail(Context context){
        Intent intent = new Intent(context, PuzzleDetailActivity.class);
        context.startActivity(intent);
    }
}
