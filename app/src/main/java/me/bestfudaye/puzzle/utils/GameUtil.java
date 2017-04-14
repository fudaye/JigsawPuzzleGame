package me.bestfudaye.puzzle.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;

import me.bestfudaye.puzzle.bean.ItemBean;

/**
 * Created by Ford on 2016/10/24.
 */
public class GameUtil {

    /**
     * 原始没有分割的bitmap
     */
    public static Bitmap sTargetBitmap = null;

    /**
     * 拼图缺省的一块
     */
    public static Bitmap sLastBitmap = null;//


    /**
     * 空的item
     */
    public static ItemBean sBlankItemBean;

    /**
     * 难度
     */
    public static int sDifficultyType = 3;




    /**
     * 交换位置
     * */
    public static void swapItems(ItemBean from, ItemBean blank) {
        ItemBean tempItem = new ItemBean();
        //交换bitmapId
        tempItem.bitmapId = from.bitmapId;
        from.bitmapId = blank.bitmapId;
        blank.bitmapId = tempItem.bitmapId;
        //交换 bitmap
        tempItem.bitmap = from.bitmap;
        from.bitmap = blank.bitmap;
        blank.bitmap = tempItem.bitmap;
        GameUtil.sBlankItemBean = from;
    }

    /**
     * 判断拼图是否有解
     * */
    public static boolean canSolve(ArrayList<Integer> data){
        int blankId = GameUtil.sBlankItemBean.id;
        if(data.size() % 2 == 1 ){
            return getInversions(data) % 2 == 0;
        }else{
            if(((blankId-1)/GameUtil.sDifficultyType)%2 == 1){
                return getInversions(data)%2 == 0;
            }else{
                return getInversions(data)%2 ==1;
            }
        }
    }


    /**
     * 计算倒置和算法
     *
     * ∑（Ｆ（Ｘ））＝Ｙ，其中Ｆ（Ｘ）
     * 就是一个数他前面比这个数小的数的个数，Ｙ为奇数和偶数个有一种解法。
     *
     *
     *
     * */
    public static int getInversions(ArrayList<Integer> data){
        int inversions = 0;
        int inversionsCount = 0;
        for(int i=0;i<data.size();i++){
            for(int j=i+1;j<data.size();j++){
                int index = data.get(i);
                if(data.get(j)!=0 && data.get(j)<index){
                    inversionsCount++;
                }
            }
            inversions += inversionsCount;
            inversionsCount = 0;
        }
        return inversions;
    }



}
