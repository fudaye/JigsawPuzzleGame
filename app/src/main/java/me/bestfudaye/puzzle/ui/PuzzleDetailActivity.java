package me.bestfudaye.puzzle.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.bestfudaye.puzzle.R;
import me.bestfudaye.puzzle.bean.ItemBean;
import me.bestfudaye.puzzle.utils.GameUtil;
import me.bestfudaye.puzzle.utils.ImageUtil;
import me.bestfudaye.puzzle.utils.ToastUtil;
import me.bestfudaye.puzzle.widget.MyRecyclerView;

/**
 * Created by Ford on 2016/10/20.
 */
public class PuzzleDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = PuzzleDetailActivity.class.getSimpleName();

    ArrayList<ItemBean> bitmaps;

    private int rvWidth = 0;
    private int rvHeight = 0;
    private MyRecyclerView mRv;
    private boolean isFirst = true;
    private int step = 0;
    private TextView tvTextView;
    private Chronometer chronometer;
    private boolean isSuccess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_detail);
        mRv = (MyRecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(new GridLayoutManager(this,GameUtil.sDifficultyType));
        findViewById(R.id.btn_original).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        tvTextView = (TextView) findViewById(R.id.tv_step);
        tvTextView.setText("步数:"+step+"步");
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        int hour = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
        chronometer.setFormat("0"+String.valueOf(hour)+":%s");
        chronometer.start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_original:
                // 显示原图
                AlertDialog.Builder builder = new AlertDialog.Builder(PuzzleDetailActivity.this);
                ImageView imageView = new ImageView(PuzzleDetailActivity.this);
                imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setImageBitmap(GameUtil.sTargetBitmap);
                builder.setView(imageView);
                builder.show();
                break;
            case R.id.btn_reset:
                if(!isSuccess){
                    isFirst = true;
                    onWindowFocusChanged(true);
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(PuzzleDetailActivity.this);
            int width = rvWidth/GameUtil.sDifficultyType;
            int height = rvHeight/GameUtil.sDifficultyType;
//            Log.d(TAG,"width-->"+width+"|"+"height-->"+height);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(width,height);
            layoutParams.setMargins(0,0,0,0);
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            return new ViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.imageView.setImageBitmap(bitmaps.get(position).bitmap);
        }

        @Override
        public int getItemCount() {
            return bitmaps.size();
        }

        class  ViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isMove(getAdapterPosition()) && !isSuccess){
                            step++;
                            tvTextView.setText("步数:"+step+"步");
                            GameUtil.swapItems(bitmaps.get(getAdapterPosition()),GameUtil.sBlankItemBean);
                            notifyDataSetChanged();
                            if(isSuccess()){
                                chronometer.stop();
                                GameUtil.sBlankItemBean.bitmap = GameUtil.sLastBitmap;
                                notifyDataSetChanged();
                                ToastUtil.showShort("成功");
                                isSuccess = true;
                            }
                        }
                    }
                });
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && isFirst){
            rvWidth  = mRv.getWidth();
            rvHeight = mRv.getHeight();
            ImageUtil imageUtil = new ImageUtil();
            bitmaps =  imageUtil.createInitBitmaps(GameUtil.sDifficultyType, GameUtil.sTargetBitmap,PuzzleDetailActivity.this);
            getPuzzleGenerator();
            mRv.setAdapter(new MyAdapter());
            isFirst = false;
        }
    }


    /**
     * 打乱拼图,并且判断拼图是有解的
     * */
    private  void getPuzzleGenerator() {
        int index ;
        for (int i = 0; i < bitmaps.size(); i++) {
            index = (int) (Math.random() * GameUtil.sDifficultyType * GameUtil.sDifficultyType);
            GameUtil.swapItems(bitmaps.get(index),GameUtil.sBlankItemBean);
        }
        ArrayList<Integer> data = new ArrayList<>();
        for (int i =0;i<bitmaps.size();i++){
            data.add(bitmaps.get(i).bitmapId);
        }
        //判断生成是否有解
        if (GameUtil.canSolve(data)){
            return;
        }else{
            getPuzzleGenerator();
        }
    }

    /**
     * 是否能够移动
     *
     * */
    private boolean  isMove(int position){
        int type = GameUtil.sDifficultyType;
        //获取空白格id
        int blankId = GameUtil.sBlankItemBean.id-1;
        //不同行相差为type
        if(Math.abs(blankId-position) == GameUtil.sDifficultyType){
            return true;
        }
        int i1= blankId/type;
        int i2 = position/type;
        Log.d(TAG,"i1-->"+i1+"i2-->"+i2);
        if((blankId/type == position/type)
                && Math.abs(blankId-position) == 1){
            return true;
        }
        return false;
    }

    /**
     * 判断拼图是否成功
     *
     * */
    private boolean  isSuccess(){
        for (ItemBean tempBean : bitmaps){
            if(tempBean.bitmapId !=0 && tempBean.bitmapId == tempBean.id){
            }else if(tempBean.bitmapId == 0 && tempBean.id == GameUtil.sDifficultyType*GameUtil.sDifficultyType){
            }else{
                return false;
            }
        }
        return true;
    }
}
