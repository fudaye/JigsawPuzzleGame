package me.bestfudaye.puzzle.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Ford on 2016/10/21.
 */
public class MyRecyclerView extends RecyclerView {

    public static final String TAG = MyRecyclerView.class.getSimpleName();

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(onInitLayoutListener != null){
            onInitLayoutListener.init();
        }
    }

    public interface onInitLayoutListener{
        void init();
    }

    private onInitLayoutListener onInitLayoutListener;

    public void setOnInitLayoutListener(onInitLayoutListener l ) {
        this.onInitLayoutListener = l;
    }
}
