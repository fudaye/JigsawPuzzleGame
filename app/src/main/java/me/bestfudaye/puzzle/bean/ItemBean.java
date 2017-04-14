package me.bestfudaye.puzzle.bean;

import android.graphics.Bitmap;

/**
 * Created by Ford on 2016/10/20.
 */
public class ItemBean  {
    public  int  id;
    public  int  bitmapId;
    public Bitmap bitmap;

    public ItemBean() {

    }

    public ItemBean(int id, int bitmapId, Bitmap bitmap) {
        this.id = id;
        this.bitmapId = bitmapId;
        this.bitmap = bitmap;
    }
}
