package me.bestfudaye.puzzle.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.util.ArrayList;

import me.bestfudaye.puzzle.R;
import me.bestfudaye.puzzle.ui.adapter.GridAdapter;
import me.bestfudaye.puzzle.ui.adapter.base.BaseActivity;
import me.bestfudaye.puzzle.utils.GameUtil;
import me.bestfudaye.puzzle.utils.IntentHelper;
import me.bestfudaye.puzzle.utils.ToastUtil;
import me.shaohui.bottomdialog.BottomDialog;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GridView gridView = (GridView) findViewById(R.id.gv);
        mAdapter = new GridAdapter(this);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String tagStr = (String) view.getTag();
                if ("last".equals(tagStr)) {
                    final BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());
                    bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                        @Override
                        public void bindView(View v) {
                            v.findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bottomDialog.dismiss();
                                    takePhoto();
                                }
                            });
                            v.findViewById(R.id.btn_album).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bottomDialog.dismiss();
                                    takeAlbum();
                                }
                            });
                        }
                    })
                            .setLayoutRes(R.layout.dialog_photo_select)
                            .show();
                } else {
                    GameUtil.sTargetBitmap = mAdapter.getBitmaps()[position];
                    IntentHelper.gotoPuzzleDetail(MainActivity.this);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_2:
                ToastUtil.showShort("难度2*2");
                GameUtil.sDifficultyType = 2;
                break;
            case R.id.action_3:
                ToastUtil.showShort("难度3*3");
                GameUtil.sDifficultyType = 3;
                break;
            case R.id.action_4:
                ToastUtil.showShort("难度4*4");
                GameUtil.sDifficultyType = 4;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        TImage tImage = result.getImage();
        ArrayList<TImage> tImages = result.getImages();
        Log.d(TAG, "tImage-->" + tImage.toString() + "tImages-->" + tImages.size());
        GameUtil.sTargetBitmap = BitmapFactory.decodeFile(tImage.getPath());
        IntentHelper.gotoPuzzleDetail(MainActivity.this);
    }
}
