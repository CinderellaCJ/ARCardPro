package com.cj.arcard.ui.ar;

import android.os.Bundle;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class ARVideoActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_arvideo);
        String videoUrl = getIntent().getStringExtra("videoUrl");
        UnityPlayer.UnitySendMessage("Video","AndroidMsg",videoUrl);

    }
}
