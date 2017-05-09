package com.cj.arcard.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.cj.arcard.R;
import com.cj.arcard.adapter.SelectAdapter;
import com.cj.arcard.bean.Card;
import com.cj.arcard.utils.LogUtil;
import com.cj.arcard.utils.MyVideoHelper;
import com.cj.arcard.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MsgEditActivity extends Activity {

    private static final int TAKE_VIDEO = 1;
    private static final int CHOICE_VIDEO = 2;

    @BindView(R.id.edit_back)
    RelativeLayout editBack;
    @BindView(R.id.receiver_name)
    EditText edtReceiverName;
    @BindView(R.id.ar_video)
    VideoView arVideo;
    @BindView(R.id.add_video)
    Button addVideo;
    @BindView(R.id.placeholder)
    FrameLayout placeholder;
    @BindView(R.id.video_upload)
    RelativeLayout videoUpload;

    private View view;
    private String videoUrl;
    private String cardId;
    private String cardReceiverName;
    private Uri videoUri;
    private String path;
    private String receiverName;
    private LinearLayout mPopupView;
    private ArrayList<String> messageList;
    private ListView listViewSelect;
    private PopupWindow popupWindow;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_edit);
        ButterKnife.bind(this);

        videoUrl = getIntent().getStringExtra("videoUrl");
        cardId = getIntent().getStringExtra("cardId");
        cardReceiverName = getIntent().getStringExtra("receiverName");
        if (cardReceiverName != null){
            edtReceiverName.setText(cardReceiverName);
        }
        if (videoUrl != null) {
            arVideo.setMediaController(new MediaController(this));
            arVideo.setVideoURI(Uri.parse(videoUrl));

            //视频加载动画
            arVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    arVideo.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            placeholder.setVisibility(View.GONE);
                        }
                    }, 1000);

                }
            });

        } else {
            arVideo.setVisibility(View.GONE);
            placeholder.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.edit_back, R.id.add_video,R.id.video_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_back:
                finish();
                break;
            case R.id.add_video:
                if (videoUrl != null) {
                    showDialog();
                } else {
                    showWindow(view);
                }
                break;
            case R.id.video_upload:
                videoUpload();
                break;
        }
    }


    private void showDialog() {
        DialogInterface.OnClickListener dialogOnclickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        showWindow(getWindow().getDecorView());
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("这会删除原有视频，确定要继续？"); //设置内容
        builder.setPositiveButton("确认", dialogOnclickListener);
        builder.setNegativeButton("取消", dialogOnclickListener);

        builder.setCancelable(false);
        builder.create().show();
    }

    private void showWindow(View parent) {

        view = LayoutInflater.from(MsgEditActivity.this).inflate(R.layout.message_select_list, null);

        //设置popWindow从底部向上弹出动画
        view.setAnimation(AnimationUtils.loadAnimation(MsgEditActivity.this, R.anim.anim_alpha));
        mPopupView = (LinearLayout) view.findViewById(R.id.ll_popup);
        mPopupView.startAnimation(AnimationUtils.loadAnimation(MsgEditActivity.this, R.anim.anim_translate_popup));

        initPopWindowData();
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        listViewSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (messageList.get(position).equals("小视频")) {
                    popupWindow.dismiss();
                    arVideo.setVisibility(View.GONE);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String currentTime = formatter.format(curDate);

                    File outputVideo = new File(Environment.getExternalStorageDirectory(), currentTime + "takeVideo.MP4");
                    try {
                        if (outputVideo.exists()) {
                            outputVideo.delete();
                        }
                        outputVideo.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    videoUri = Uri.fromFile(outputVideo);
                    Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                    startActivityForResult(intent, TAKE_VIDEO);
                }

                if (messageList.get(position).equals("从手机视频选择")) {
                    popupWindow.dismiss();
                    arVideo.setVisibility(View.GONE);
                    File outputVideo = new File(Environment.getExternalStorageDirectory(), "choiceVideo.MP4");
                    try {
                        if (outputVideo.exists()) {
                            outputVideo.delete();
                        }
                        outputVideo.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    videoUri = Uri.fromFile(outputVideo);
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("video/*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                    startActivityForResult(intent, CHOICE_VIDEO);
                }
                if (messageList.get(position).equals("取消")) {
                    popupWindow.dismiss();
                }

                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = bgAlpha;
        getWindow().setAttributes(layoutParams);
    }

    private void initPopWindowData() {
        listViewSelect = (ListView) view.findViewById(R.id.listView_select);
        messageList = new ArrayList<>();
        messageList.add("小视频");
        messageList.add("从手机视频选择");
        messageList.add("取消");

        SelectAdapter selectAdapter = new SelectAdapter(MsgEditActivity.this, messageList);
        listViewSelect.setAdapter(selectAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data.getData());
                    sendBroadcast(intent);
                    Uri uri = data.getData();
                    path = MyVideoHelper.getPath(MsgEditActivity.this, uri);
                    Log.i("MessageEditActivity", "pathT:" + path);
                    arVideo.setVisibility(View.VISIBLE);
                    arVideo.setVideoURI(Uri.parse(String.valueOf(data.getData())));
                    arVideo.setMediaController(new MediaController(this));
                    arVideo.start();
                }
                break;
            case CHOICE_VIDEO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    path = MyVideoHelper.getPath(MsgEditActivity.this, uri);  // 获取图片
                    Log.i("MessageEditActivity", "pathC" + path);
                    arVideo.setVisibility(View.VISIBLE);
                    arVideo.setVideoURI(Uri.parse(String.valueOf(data.getData())));
                    arVideo.setMediaController(new MediaController(this));
                    arVideo.start();
                }
                break;
            default:
                break;
        }
    }

    private void videoUpload() {
        if (edtReceiverName.getText().toString().equals("")){
            ToastUtil.showShort(MsgEditActivity.this,"请填写收件人昵称");
        }else {
            //上传进度条
            progressDialog = new ProgressDialog(MsgEditActivity.this);
            progressDialog.setTitle("上传中..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);

            receiverName = edtReceiverName.getText().toString();

            final BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        progressDialog.dismiss();
                        AlertDialog.Builder builder=new AlertDialog.Builder(MsgEditActivity.this);
                        builder.setMessage("上传成功！");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Card card = new Card();
                                card.setCardVideo(bmobFile);
                                card.setReceiverName(receiverName);
                                card.update(cardId,new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            LogUtil.d("suss");
                                        }else {
                                            LogUtil.d(e.getMessage()+e.getErrorCode());
                                        }

                                    }
                                });
                                finish();
                            }
                        });
                        builder.create().show();
                    }else {
                        LogUtil.d(e.getMessage()+e.getErrorCode());
                    }
                }

                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                    progressDialog.show();
                    progressDialog.setProgress(value);
                }
            });
        }
    }

}
