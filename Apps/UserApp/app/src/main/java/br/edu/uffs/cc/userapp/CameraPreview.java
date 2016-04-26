package br.edu.uffs.cc.userapp;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";
    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mContext = context;
        mCamera = camera;

        // supported preview sizes
        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        mPreviewSize = mSupportedPreviewSizes.get(0);
        for(int i=0;i<mSupportedPreviewSizes.size();i++)
        {
            if(mSupportedPreviewSizes.get(i).width > mPreviewSize.width)
                mPreviewSize = mSupportedPreviewSizes.get(i);
        }

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // empty. surfaceChanged will take care of stuff
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged => w=" + width + ", h=" + height);
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or reformatting changes here
        // start preview with new settings
        try {
            Camera.Parameters parameters = mCamera.getParameters();

            Display display = ((WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();

            if(display.getRotation() == Surface.ROTATION_0)
            {
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                parameters.setRotation(90);
                mCamera.setDisplayOrientation(90);

            }
            if(display.getRotation() == Surface.ROTATION_90)
            {
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            }

            if(display.getRotation() == Surface.ROTATION_180)
            {
                parameters.setPreviewSize(mPreviewSize.height, mPreviewSize.width);
                parameters.setRotation(90);
                mCamera.setDisplayOrientation(90);
            }

            if(display.getRotation() == Surface.ROTATION_270)
            {
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                parameters.setRotation(180);
                mCamera.setDisplayOrientation(180);
            }

            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}