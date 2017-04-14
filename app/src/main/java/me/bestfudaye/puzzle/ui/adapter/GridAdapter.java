package me.bestfudaye.puzzle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import me.bestfudaye.puzzle.R;

/**
 * Created by Ford on 2016/10/18.
 */
public class GridAdapter extends BaseAdapter {


    private Bitmap [] bitmaps ;
    private Context mContext;


    public GridAdapter(Context context) {
        this.mContext = context;
        int[] IMAGS = {
                R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4,
                R.drawable.pic5, R.drawable.pic6, R.drawable.pic7, R.drawable.pic8,
                R.drawable.pic9, R.drawable.pic10, R.drawable.pic11,R.drawable.ic_add
        };
        bitmaps = new Bitmap[IMAGS.length];
        for (int i = 0; i< IMAGS.length; i++){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), IMAGS[i]);
            bitmaps[i] =bitmap;
        }
    }

    @Override
    public int getCount() {
        return bitmaps.length;
    }

    @Override
    public Object getItem(int i) {
        return bitmaps[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view  = LayoutInflater.from(mContext).inflate(R.layout.item_grid,viewGroup,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv);
        imageView.setImageBitmap(bitmaps[i]);
        if(i ==  bitmaps.length-1){
            view.setTag("last");
        }else{
            view.setTag("");
        }
        return view;
    }

    public Bitmap [] getBitmaps(){
        return bitmaps;
    }
}
