package com.cj.arcard.ui.ar;

import android.os.Bundle;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class ARActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ar2);
        UnityPlayer.UnitySendMessage("Video","AndroidMsg","0");
    }
}
