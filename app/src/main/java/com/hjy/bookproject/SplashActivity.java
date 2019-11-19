package com.hjy.bookproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    public static final int CODE = 1001;
    public static final int TOTAL_TIME = 3000;
    public static final int INTEVAL_TIME = 1000;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        MyHandle myHandle = new MyHandle(this);
        Message message = Message.obtain();
        message.what = CODE;
        message.arg1 = TOTAL_TIME;
        myHandle.sendMessage(message);
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
                    activity.tvTime.setText(time / INTEVAL_TIME + "秒，点击跳过");
                    //发送倒计时
                    Message message = Message.obtain();
                    message.what = CODE;
                    message.arg1 = time - INTEVAL_TIME;

                    if (time > 0) {
                        sendMessageDelayed(message, INTEVAL_TIME);
                    } else {
                        //todo：跳转到下一页
                    }
                }
            }
        }
    }

}
