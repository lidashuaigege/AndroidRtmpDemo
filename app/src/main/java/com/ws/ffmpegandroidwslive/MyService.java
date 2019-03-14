package com.ws.ffmpegandroidwslive;

import android.app.Service;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ws.ffmpegandroidcameralive.WSPlayer;

import java.io.IOException;

/**
 * Created by Lee on 2019/3/14.
 */

public class MyService extends Service implements SurfaceTexture.OnFrameAvailableListener{
    private Camera mCamera;
    private SurfaceTexture surfaceTexture;

    @Override
    public void onCreate() {
        super.onCreate();
        final Camera.PreviewCallback mPreviewCallback=new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] arg0, Camera arg1) {

               // WSPlayer.start(rotateYUVDegree90(arg0,mCamera.getParameters().getPreviewSize().width,mCamera.getParameters().getPreviewSize().height));
                Log.v("MyService","onPreviewFrame");
                WSPlayer.start(arg0);
            }
        };
        //WSPlayer.initialize(mCamera.getParameters().getPreviewSize().height,mCamera.getParameters().getPreviewSize().width,"rtmp://59.110.46.27:1935/srstest/teststream");
        if(surfaceTexture==null)
            surfaceTexture = new SurfaceTexture(0);
        surfaceTexture.setOnFrameAvailableListener(null);
        Log.v("MyService","open camera");
        openCamera();
        Log.v("MyService","end open camera");
        int x =mCamera.getParameters().getPreviewSize().height;
        int y =mCamera.getParameters().getPreviewSize().width;
        WSPlayer.initialize(y,x,"rtmp://59.110.46.27:1935/srstest/teststream");
        mCamera.setPreviewCallback(mPreviewCallback);
    }
    //initial camera
    public boolean openCamera() {
        try {
            mCamera = Camera.open(0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewFormat(ImageFormat.NV21);
        params.setZoom(0);//设置焦距为0
        params.setPreviewSize(640,480);
        params.setPictureSize(640,480);
        params.set("orientation", "portrait");
        mCamera.setParameters(params);
        if (mCamera == null) {
            return false;
        }
        try {
            //这一步是最关键的，使用surfaceTexture来承载相机的预览，而不需要设置一个可见的view
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    private byte[] rotateYUVDegree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //surfaceTexture.updateTexImage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WSPlayer.stop();
        WSPlayer.close();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
