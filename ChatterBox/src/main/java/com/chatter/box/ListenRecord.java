package com.chatter.box;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;


public class ListenRecord extends Activity {

    //private instance fields
    private Button mButton;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecordTest";
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
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        returnHome();
    }

    //Switches content view to record mode
    private void switchToRecord() {
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        //creates recording
        LinearLayout ll = new LinearLayout(this);
        mButton = new RecordButton(this);
        ll.addView(mButton,
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
    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if( mStartRecording) {
                    setText("Stop Recording");
                }else {
                    setText("Start Recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super (ctx);
            setText("Start Recording");
            setOnClickListener(clicker);
        }
    }









    public void onPlay(boolean start) {
        if(start)
            startPlayback();
        else
            pausePlayback();
    }

    private void startPlayback() {
            mPlayer.start();
    }

    private void pausePlayback() {
            mPlayer.pause();
    }

    //Class for making a button for recording voice
    class PlayButton extends Button {
        boolean mPaused = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mPaused);
                if(mPaused) {
                    setText("Press to pause");
                }else {
                    setText("Press to listen");
                }
                mPaused = !mPaused;
            }
        };

        public PlayButton(Context ctx) {
            super (ctx);
            setText("Press to listen");
            setOnClickListener(clicker);

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

        //sets where file is to be stored
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/test.3gp";

        LinearLayout ll = new LinearLayout(this);
        mButton = new PlayButton(this);
        ll.addView(mButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);

    }



}
