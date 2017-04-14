package me.bestfudaye.puzzle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.ArrayList;

import me.bestfudaye.puzzle.R;
import me.bestfudaye.puzzle.bean.ItemBean;

/**
 * Created by Ford on 2016/10/20.
 */
public class ImageUtil {
    public ItemBean itemBean;
    public ArrayList<ItemBean> itemBeans = new ArrayList<>();


    public ArrayList<ItemBean> createInitBitmaps(int type, Bitmap target, Context context) {
        Bitmap bitmap;
        int itemWidth = target.getWidth() / type;
        int itemHeight = target.getHeight() / type;
        for (int i = 1; i <= type; i++) {
            for (int j = 1; j <= type; j++) {
                bitmap = Bitmap.createBitmap(
                        target,
                        (j - 1) * itemWidth,
                        (i - 1) * itemHeight,
                        itemWidth,
                        itemHeight
                );
                itemBean = new ItemBean(
                        (i - 1) * type + j,
                        (i - 1) * type + j,
                        bitmap
                );
                itemBeans.add(itemBean);
            }
        }
//        在拼图完成时 保存最后一个图片
        GameUtil.sLastBitmap = itemBeans.get(type * type - 1).bitmap;
        //支出到一个片段
//        bitmapItems.remove(type * type - 1);
        itemBeans.remove(type * type - 1);
        Bitmap blankBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_blank);
        Bitmap.createBitmap(blankBitmap,0,0,blankBitmap.getWidth(),blankBitmap.getHeight());
//        bitmapItems.add(blankBitmap);
        ItemBean blankItemBean = new ItemBean(type*type,0,blankBitmap);
        GameUtil.sBlankItemBean = blankItemBean;
        itemBeans.add(blankItemBean);
        return itemBeans;
    }

    public Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / bitmap.getWidth(), newHeight / bitmap.getHeight());
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
