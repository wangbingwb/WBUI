package xyz.wbsite.wbsiteui.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.jph.takephoto.app.TakePhoto;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xyz.wbsite.wbsiteui.base.utils.StorageUtil;

import static xyz.wbsite.wbsiteui.base.Content.DIR_IMG;


public abstract class BaseSPATakePhotoFragment extends BaseSPAFragment implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = BaseSPATakePhotoFragment.class.getName();

    //TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private InvokeParam invokeParam;

    private TakePhtotListener listener;

    public void requestTakePhoto(TakePhtotListener listener) {
        this.listener = listener;

        //获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置压缩、裁剪参数
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setWithOwnCrop(true).create();
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(512 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);  //设置为需要压缩
        //拍照并裁剪
        takePhoto.onPickFromCapture(getImageCropUri());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
        String compressPath = result.getImage().getCompressPath();
        if (compressPath != null) {
            File file = new File(compressPath);
            File file1 = new File(StorageUtil.getOwnCacheDirectory(getContext(), DIR_IMG), "min_" + file.getName());
            copyFileUsingFileStreams(file, file1);

            if (listener != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                listener.takeSuccess(bitmap);
                listener = null;
            }
        }
    }

    private static void copyFileUsingFileStreams(File source, File dest) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);

        if (listener != null) {
            listener.takeFail();
            listener = null;
        }
    }

    @Override
    public void takeCancel() {
        if (listener != null) {
            listener.takeCancel();
            listener = null;
        }
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(StorageUtil.getOwnCacheDirectory(getContext(), DIR_IMG), System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    public interface TakePhtotListener {
        void takeSuccess(Bitmap bitmap);

        void takeFail();

        void takeCancel();
    }
}
