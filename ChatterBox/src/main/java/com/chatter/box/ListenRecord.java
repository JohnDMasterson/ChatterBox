package com.chatter.box;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;


public class ListenRecord extends Activity {

    //private instance fields
    private ImageView mImage;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecord";
    private String mFileName;



    //Recording methods.
    //Toggle for start/stop recording
    public void onRecord(boolean start) {
        if(start)
            startRecording();
        else
            stopRecording();
    }

    //Start for recording
    private void startRecording() {
        mImage.setColorFilter(Color.argb(255, 255, 120, 100));
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    //Stops recording
    private void stopRecording() {
        mImage.setColorFilter(Color.argb(255, 255, 255, 255));
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        returnHome();
    }

    //Switches content view to record mode
    private void switchToRecord() {
        this.getActionBar().setTitle("Press To Start And Stop");
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        //creates recording
        LinearLayout ll = new LinearLayout(this);
        mImage = new MicImage(this);

        mImage.setBaselineAlignBottom(true);
        mImage.setColorFilter(Color.argb(255, 255, 255, 255));


        ll.addView(mImage,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);

        //sets where file is to be stored
        //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        //mFileName += "/record.3gp";
    }

    //Class for making a button for recording voice
    class MicImage extends ImageView {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        };

        public MicImage(Context ctx) {
            super (ctx);
            setOnClickListener(clicker);
            setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mic));

        }
    }









    public void onPlay(boolean start) {
        this.getActionBar().setDisplayHomeAsUpEnabled(false);
        if(start)
            startPlayback();
        else
            pausePlayback();
    }

    private void startPlayback() {
        mImage.setColorFilter(Color.argb(255, 120, 255, 120));
        mPlayer.start();
    }

    private void pausePlayback() {
        mImage.setColorFilter(Color.argb(255, 255, 255, 255));
        mPlayer.pause();
    }

    //Class for making a button for recording voice
    class PlayImage extends ImageView {
        boolean mPaused = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mPaused);
                mPaused = !mPaused;
            }
        };

        public PlayImage(Context ctx) {
            super(ctx);
            setOnClickListener(clicker);
            setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.triangle));
            setColorFilter(Color.argb(255, 255, 255, 255));
            mPlayer = new MediaPlayer();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                 public void onCompletion(MediaPlayer mp) {
                     switchToRecord();
            }});

            try {
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
        }

    }

    public void returnHome(){
        finish();
    }


    //Destructor
    //Stops current Recording or Playback
    protected void onDestroy() {
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer  = null;
        }
        if(mRecorder != null)
            stopRecording();
        super.onDestroy();
    }


    //Constructor
    //Looks for last recording
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getActionBar().setIcon(new ColorDrawable(0x0000));
        this.getActionBar().setTitle("Press Play And Listen");

        //sets where file is to be stored
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/test.3gp";


        LinearLayout ll = new LinearLayout(this);
        mImage = new PlayImage(this);

        mImage.setBaselineAlignBottom(true);

        ll.addView(mImage,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);
    }


}
