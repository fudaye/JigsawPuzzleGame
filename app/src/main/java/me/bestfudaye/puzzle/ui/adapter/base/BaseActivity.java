package me.bestfudaye.puzzle.ui.adapter.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;

import me.bestfudaye.puzzle.config.AppConfig;

/**
 * Created by Ford on 2016/10/20.
 */
public class BaseActivity extends AppCompatActivity implements TakePhoto.TakeResultListener,InvokeListener {

    private static final String TAG = TakePhotoActivity.class.getName();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG,"takeSuccess：" + result.getImage().getPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }


    protected void takeAlbum() {
        Uri imageUri = createTempFile();
        configCompress(getTakePhoto());
        int limit = AppConfig.TAKE_PIC_CONFIG_SELECT_NUM;
        if (limit > 1) {
            if (AppConfig.TAKE_PIC_CONFIG_IS_CROP) {
                getTakePhoto().onPickMultipleWithCrop(limit, getCropOptions());
            } else {
                getTakePhoto().onPickMultiple(limit);
            }
            return;
        }
        if (AppConfig.TAKE_PIC_CONFIG_SELECT_IS_ALBUM) {
            if (AppConfig.TAKE_PIC_CONFIG_IS_CROP) {
                getTakePhoto().onPickFromDocumentsWithCrop(imageUri, getCropOptions());
            } else {
                getTakePhoto().onPickFromDocuments();
            }
        } else {
            if (AppConfig.TAKE_PIC_CONFIG_IS_CROP) {
                getTakePhoto().onPickFromGalleryWithCrop(imageUri, getCropOptions());
            } else {
                getTakePhoto().onPickFromGallery();
            }
        }
    }

    protected void takePhoto() {
        Uri imageUri = createTempFile();
        configCompress(getTakePhoto());
        if(AppConfig.TAKE_PIC_CONFIG_IS_CROP){
            getTakePhoto().onPickFromCaptureWithCrop(imageUri,getCropOptions());
        }else {
            getTakePhoto().onPickFromCapture(imageUri);
        }
    }

    /**
     * 创建临时文件夹
     */
    private Uri createTempFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    /**
     * 压缩配置
     */
    private void configCompress(TakePhoto takePhoto) {
        if (AppConfig.TAKE_PIC_CONFIG_IS_COMPRESS) {
            int maxSize = AppConfig.TAKE_PIC_CONFIG_PHOTO_MAX_SIZE;
            int maxPixel = AppConfig.TAKE_PIC_CONFIG_PHOTO_MAX_WIDTH_OR_HEIGHT;
            CompressConfig config = new CompressConfig.Builder().setMaxPixel(maxSize).setMaxPixel(maxPixel).create();
            takePhoto.onEnableCompress(config, AppConfig.TAKE_PIC_CONFIG_IS_SHOW_PROGRESS);
        } else {
            takePhoto.onEnableCompress(null, false);
        }
    }

    /**
     * 裁剪配置
     */
    private CropOptions getCropOptions() {
        if (AppConfig.TAKE_PIC_CONFIG_IS_CROP) {
            CropOptions.Builder builder = new CropOptions.Builder();
            if (AppConfig.TAKE_PIC_CONFIG_IS_RATIO_CROP) {
                builder.setAspectX(AppConfig.TAKE_PIC_CONFIG_CROP_WIDTH).setAspectY(AppConfig.TAKE_PIC_CONFIG_CROP_HEIGHT);
            } else {
                builder.setOutputX(AppConfig.TAKE_PIC_CONFIG_CROP_WIDTH).setOutputY(AppConfig.TAKE_PIC_CONFIG_CROP_HEIGHT);
            }
            builder.setWithOwnCrop(AppConfig.TAKE_PIC_CONFIG_CROP_TOOL_IS_CUSTOM);
            return builder.create();
        }
        return null;
    }
}
