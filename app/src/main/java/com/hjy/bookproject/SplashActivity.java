package com.hjy.bookproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends Activity {

    public static final int CODE = 1001;
    public static final int TOTAL_TIME = 3000;
    public static final int INTERVAL_TIME = 1000;

    @BindView(R.id.tv_time)
    TextView tvTime;
    private MyHandle myHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        myHandle = new MyHandle(this);
        Message message = Message.obtain();
        message.what = CODE;
        message.arg1 = TOTAL_TIME;
        myHandle.sendMessage(message);
    }


    @OnClick(R.id.tv_time)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                BookListActivity.startActivity(SplashActivity.this);
                SplashActivity.this.finish();
                if (myHandle != null) {
                    myHandle.removeMessages(CODE);
                }
                break;
            default:
                break;
        }
    }

    public static class MyHandle extends Handler {

        public final WeakReference<SplashActivity> mWeakReference;

        public MyHandle(SplashActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            SplashActivity activity = mWeakReference.get();

            if (msg.what == CODE) {
                if (activity != null) {
                    //设置textview，更新ui；
                    int time = msg.arg1;
                    activity.tvTime.setText(time / INTERVAL_TIME + "秒，点击跳过");
                    //发送倒计时
                    Message message = Message.obtain();
                    message.what = CODE;
                    message.arg1 = time - INTERVAL_TIME;

                    if (time > 0) {
                        sendMessageDelayed(message, INTERVAL_TIME);
                    } else {
                        BookListActivity.startActivity(activity);
                        activity.finish();
                    }
                }
            }
        }
    }


}
