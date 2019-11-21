package com.hjy.bookproject.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hjy.bookproject.R;
import com.hjy.bookproject.view.BookPageBezierHelper;
import com.hjy.bookproject.view.BookPageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjy on 2019/11/21
 */
public class BookActivity extends AppCompatActivity {

    public static final String FILE_PATH = "file_path";

    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.book_page_view)
    BookPageView bookPageView;

    private BookPageBezierHelper bookPageBezierHelper;

    public static void startActivity(Context context, String filePath) {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra(FILE_PATH, filePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式4.4以上有用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //设置 book helper
        bookPageBezierHelper = new BookPageBezierHelper(width, height);
        bookPageView.setBookPageBezierHelper(bookPageBezierHelper);

        //设置 book 当前页和下一页的Bitmap
        Bitmap currentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap nextBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bookPageView.setBitmaps(currentBitmap, nextBitmap);

        //设置book 的背景
        bookPageBezierHelper.setBackground(this, R.drawable.book_bg);

        //设置book 的进度
        bookPageBezierHelper.setOnProgressChangedListener(new BookPageBezierHelper.OnProgressChangedListener() {
            @Override
            public void setProgress(int currentLength, int totalLength) {
                tvProgress.setText(currentLength * 100 / totalLength + "%");
            }
        });

        if (getIntent() != null) {
            String filePath = getIntent().getStringExtra(FILE_PATH);
            if (!TextUtils.isEmpty(filePath)) {
                try {
                    bookPageBezierHelper.openBook(filePath);
                    bookPageBezierHelper.draw(new Canvas(currentBitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        } else {

        }
    }


}
