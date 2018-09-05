package xyz.wbsite.wbsiteui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
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
import java.nio.file.Files;
import java.nio.file.Path;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseFragment;

public class TakePhotoFragment extends BaseFragment implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = com.jph.takephoto.app.TakePhotoFragment.class.getName();
    private InvokeParam invokeParam;

    //UIs
    @BindView(R.id.take_from_camera)
    Button takeFromCameraBtn;
    @BindView(R.id.take_from_galley)
    Button takeFromGalleyBtn;
    @BindView(R.id.image_view)
    ImageView imageView;

    //TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private Uri imageUri;  //图片保存路径

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_take_photo;
    }

    @Override
    protected void initView() {


        takeFromCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();  //设置压缩、裁剪参数
                imageUri = getImageCropUri();
                //拍照并裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                //仅仅拍照不裁剪
                //takePhoto.onPickFromCapture(imageUri);
            }
        });

        takeFromGalleyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = getImageCropUri();
                //从相册中选取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                //从相册中选取不裁剪
                //takePhoto.onPickFromGallery();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
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
        if (compressPath != null){
            File file = new File(compressPath);
            File file1 = new File(Environment.getExternalStorageDirectory(), "/temp/min_" + file.getName());
            copyFileUsingFileStreams(file,file1);
        }
//        String iconPath = result.getImage().getOriginalPath();
//        //Toast显示图片路径
//        Toast.makeText(getActivity(), "imagePath:" + iconPath, Toast.LENGTH_SHORT).show();
//        //Google Glide库 用于加载图片资源
        Glide.with(this).load(compressPath).into(imageView);
    }

    private static void copyFileUsingFileStreams(File source, File dest){
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

        Toast.makeText(getActivity(), "Error:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void initData() {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(500*1024).setMaxPixel(1000).create();
        takePhoto.onEnableCompress(compressConfig, true);  //设置为需要压缩
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
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

}
