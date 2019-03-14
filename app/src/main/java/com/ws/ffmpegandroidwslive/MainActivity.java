package com.ws.ffmpegandroidwslive;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ws.ffmpegandroidcameralive.WSPlayer;

import java.io.IOException;


public class MainActivity extends Activity{


    private static final String TAG= "MainActivity";
    private Button mTakeButton;
    private Camera mCamera;
    private SurfaceTexture surfaceTexture;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}


