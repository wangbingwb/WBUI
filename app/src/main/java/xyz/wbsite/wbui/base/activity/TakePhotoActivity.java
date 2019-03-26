package xyz.wbsite.wbui.base.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;

import xyz.wbsite.wbui.base.Consant;
import xyz.wbsite.wbui.base.utils.StorageUtil;

public class TakePhotoActivity extends com.jph.takephoto.app.TakePhotoActivity {

    public static final String POSITION_PARAM = "position_param";
    public static final String POSITION_CAPTURE = "capture";
    public static final String POSITION_GALLERY = "gallery";
    public static final String POSITION_DOCUMENTS = "documents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TakePhoto takePhoto = getTakePhoto();
        configCompress(takePhoto);
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(POSITION_PARAM);
        if (POSITION_CAPTURE.equals(stringExtra)) {
            takePhoto.onPickFromCapture(getImageCropUri());
        } else if (POSITION_GALLERY.equals(stringExtra)) {
            takePhoto.onPickFromGallery();
        } else {
            takePhoto.onPickFromDocuments();
        }
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(StorageUtil.getOwnCacheDirectory(this, Consant.DIR_IMG), System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    private void configCompress(TakePhoto takePhoto) {//压缩配置
        CompressConfig config = new CompressConfig.Builder().setMaxSize(512 * 1024)
                .setMaxPixel(800)
                .enableReserveRaw(false)//拍照压缩后是否显示原图
                .create();
        takePhoto.onEnableCompress(config, false);//是否显示进度条
    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        Intent intent = new Intent();
        intent.putExtra("path", image.getCompressPath() != null ? image.getCompressPath() : image.getOriginalPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }
}
