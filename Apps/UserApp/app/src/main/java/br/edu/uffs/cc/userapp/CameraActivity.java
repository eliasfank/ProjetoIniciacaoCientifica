package br.edu.uffs.cc.userapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraActivity extends Activity {
    private Camera mCamera;
    private Imagem im;
    private GPSTracker gps;
    private ProgressDialog pDialog;
    private Context aplicationactivity;
    private File pictureFile;
    private TextToSpeech tts;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private List<Camera.Size> finalSize;

    private String FILE_UPLOAD_URL;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aplicationactivity = this;
        im = new Imagem(aplicationactivity);
        setContentView(R.layout.activity_camera);
        mCamera = getCameraInstance();
        CameraPreview mCameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);

        SharedPreferences sharedPreferences = getSharedPreferences("PrefsFile", 0);
        FILE_UPLOAD_URL = "http://"+sharedPreferences.getString("MEM1", "")+"/Recognizer/receber_imagem2.php";

        finalSize = mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size size = finalSize.get(0);
        for(int i=0;i<finalSize.size();i++)
        {
            if(finalSize.get(i).width > size.width)
                size = finalSize.get(i);
        }

        Log.e("SIZE", size.width + "/" + size.height);

        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();
        // Set the picture size
        params.setPictureSize(size.width,size.height);
        // Aditional params
        params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        params.setExposureCompensation(0);
        params.setJpegQuality(100);
        params.setRotation(90);
        // set Camera parameters
        mCamera.setParameters(params);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                   // tts.setLanguage(Locale.ITALY);
                }
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(aplicationactivity);
                if(gps.canGetLocation()){
                    im.setLat(gps.getLatitude());
                    im.setLon(gps.getLongitude());
                }else{
                    gps.showSettingsAlert();
                }
                gps.stopUsingGPS();
                mCamera.takePicture(null, null, mPicture);
            }
        });
    }

    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     *
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            Log.d("Local do arquivo:", ""+pictureFile);
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("ERRO", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("ERRO", "Error accessing file: " + e.getMessage());
            }

            //new mandarImagem().execute();
            new UploadFileToServer().execute();
        }
    };

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "UserApp");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("ERRO", "Oops! Failed create "
                        + "UserApp" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void reproduz(){
        String toSpeak = im.getResultado();
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CameraActivity.this);
            pDialog.setMessage("Enviando Imagem");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {
            mCamera.startPreview();
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity();

                // Adding file data to http body
                entity.addPart("imagem", new FileBody(pictureFile));

                // Extra parameters if you want to pass to server
                entity.addPart("latitude", new StringBody(Double.toString(im.getLat())));
                entity.addPart("longitude", new StringBody(Double.toString(im.getLon())));

                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("", "Response from server: " + result);
            // showing the server response in an alert dialog
            //showAlert(result);
            //im.setResultado(result);
            try {
                im.setResultado(URLDecoder.decode(result, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
            reproduz();
            super.onPostExecute(result);
        }
    }

}