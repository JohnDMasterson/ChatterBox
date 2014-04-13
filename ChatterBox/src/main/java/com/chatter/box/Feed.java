package com.chatter.box;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;




public class Feed extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

    }

    public void goToPlayback(View view) {
        Intent intent = new Intent(this, ListenRecord.class);
        startActivity(intent);
    }
}
