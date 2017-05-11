/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cj.arcard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cj.arcard.Constant;
import com.cj.arcard.DemoHelper;
import com.cj.arcard.DemoModel;
import com.cj.arcard.R;
import com.cj.arcard.utils.PreferenceManager;
import com.cj.arcard.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseSwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * settings screen
 */
@SuppressWarnings({"FieldCanBeLocal"})
public class SettingsFragment extends Fragment implements OnClickListener {

    @BindView(R.id.my_head_avatar)
    ImageView myHeadAvatar;
    Unbinder unbinder;
    @BindView(R.id.my_username)
    TextView myUsername;
    @BindView(R.id.my_setting)
    ImageView mySetting;
    /**
     * new message notification
     */
    private RelativeLayout rl_switch_notification;
    /**
     * sound
     */
    private RelativeLayout rl_switch_sound;
    /**
     * vibration
     */
    private RelativeLayout rl_switch_vibrate;
    /**
     * speaker
     */


    /**
     * line between sound and vibration
     */
    private TextView textview1, textview2;


    private LinearLayout userProfileContainer;

    /**
     * logout
     */

    RelativeLayout rl_push_settings;

    /**
     * Diagnose
     */
    /**
     * display name for APNs
     */

    private EaseSwitchButton notifySwitch;
    private EaseSwitchButton soundSwitch;
    private EaseSwitchButton vibrateSwitch;

    private DemoModel settingsModel;
    private EMOptions chatOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.em_fragment_conversation_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        //获取当前用户头像
        String avator = PreferenceManager.getInstance().getCurrentUserAvatorUrl();
        Glide.with(getContext()).load(avator).placeholder(R.drawable.em_default_avatar).into(myHeadAvatar);
        //String username = EMClient.getInstance().getCurrentUser();
        //EaseUserUtils.setUserAvatar(getActivity(),username, myHeadAvatar);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String userNickname = PreferenceManager.getInstance().getCurrentBmobUserName();
        myUsername.setText(userNickname);
        String username = EMClient.getInstance().getCurrentUser();
        EaseUserUtils.setUserAvatar(getActivity(),username, myHeadAvatar);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        rl_switch_notification = (RelativeLayout) getView().findViewById(R.id.rl_switch_notification);
        rl_switch_sound = (RelativeLayout) getView().findViewById(R.id.rl_switch_sound);
        rl_switch_vibrate = (RelativeLayout) getView().findViewById(R.id.rl_switch_vibrate);
        rl_push_settings = (RelativeLayout) getView().findViewById(R.id.rl_push_settings);


        notifySwitch = (EaseSwitchButton) getView().findViewById(R.id.switch_notification);
        soundSwitch = (EaseSwitchButton) getView().findViewById(R.id.switch_sound);
        vibrateSwitch = (EaseSwitchButton) getView().findViewById(R.id.switch_vibrate);

        textview1 = (TextView) getView().findViewById(R.id.textview1);
        textview2 = (TextView) getView().findViewById(R.id.textview2);

        userProfileContainer = (LinearLayout) getView().findViewById(R.id.ll_user_profile);

        settingsModel = DemoHelper.getInstance().getModel();
        chatOptions = EMClient.getInstance().getOptions();

        userProfileContainer.setOnClickListener(this);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_sound.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
        rl_push_settings.setOnClickListener(this);
        String username = EMClient.getInstance().getCurrentUser();
        EaseUserUtils.setUserAvatar(getActivity(), username, myHeadAvatar);

        // the vibrate and sound notification are allowed or not?
        if (settingsModel.getSettingMsgNotification()) {
            notifySwitch.openSwitch();
        } else {
            notifySwitch.closeSwitch();
        }

        // sound notification is switched on or not?
        if (settingsModel.getSettingMsgSound()) {
            soundSwitch.openSwitch();
        } else {
            soundSwitch.closeSwitch();
        }

        // vibrate notification is switched on or not?
        if (settingsModel.getSettingMsgVibrate()) {
            vibrateSwitch.openSwitch();
        } else {
            vibrateSwitch.closeSwitch();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //end of red packet code
            case R.id.rl_switch_notification:
                if (notifySwitch.isSwitchOpen()) {
                    notifySwitch.closeSwitch();
                    rl_switch_sound.setVisibility(View.GONE);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    textview2.setVisibility(View.GONE);
                    settingsModel.setSettingMsgNotification(false);
                } else {
                    notifySwitch.openSwitch();
                    rl_switch_sound.setVisibility(View.VISIBLE);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }
                break;
            case R.id.rl_switch_sound:
                if (soundSwitch.isSwitchOpen()) {
                    soundSwitch.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    soundSwitch.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case R.id.rl_switch_vibrate:
                if (vibrateSwitch.isSwitchOpen()) {
                    vibrateSwitch.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                } else {
                    vibrateSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.ll_user_profile:
                ToastUtil.showShort(getContext(),"当前已是最新版本！");
                break;
            case R.id.rl_push_settings:
                startActivity(new Intent(getActivity(), OfflinePushSettingsActivity.class));
                break;
            default:
                break;
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (((MainActivity) getActivity()).isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (((MainActivity) getActivity()).getCurrentAccountRemoved()) {
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.my_head_avatar,R.id.my_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_head_avatar:
                startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("setting", true)
                        .putExtra("username", EMClient.getInstance().getCurrentUser()));
                break;
            case R.id.my_setting:
                startActivity(new Intent(getContext(),LogoutActivity.class));
                break;
        }
    }
}
