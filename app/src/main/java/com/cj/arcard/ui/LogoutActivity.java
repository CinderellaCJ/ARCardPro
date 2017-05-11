package com.cj.arcard.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.cj.arcard.DemoHelper;
import com.cj.arcard.R;
import com.hyphenate.EMCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogoutActivity extends Activity {

    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_logout)
    public void onViewClicked() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // show login screen
                        startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
                        finish();

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
               runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(LogoutActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
