package com.hjy.bookproject.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjy.bookproject.R;
import com.hjy.bookproject.ui.adapter.HorizontalAdapter;
import com.hjy.bookproject.utils.SPUtils;
import com.hjy.bookproject.view.BookPageBezierHelper;
import com.hjy.bookproject.view.BookPageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjy on 2019/11/21
 */
public class BookActivity extends AppCompatActivity {

    public static final String FILE_PATH = "file_path";
    public static final String BOOKMARK = "bookmark";

    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.book_page_view)
    BookPageView bookPageView;
    @BindView(R.id.rl_setting_view)
    RelativeLayout rlSettingView;
    @BindView(R.id.recycler_setting)
    RecyclerView recyclerSetting;
    @BindView(R.id.seekbar)
    SeekBar seekbar;

    private Bitmap currentBitmap;
    private Bitmap nextBitmap;
    private BookPageBezierHelper bookPageBezierHelper;
    private HorizontalAdapter adapter;
    private int mcurrentLength;
    private String filePath;
    private int width;
    private int height;
    private int lastLength;
    private TextToSpeech mTTs;
    private int mTotalLenght;

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
        loadData();
    }

    private void initView() {
        if (getIntent() != null) {
            filePath = getIntent().getStringExtra(FILE_PATH);
            if (!TextUtils.isEmpty(filePath)) {
                mTotalLenght = (int) new File(filePath).length();
            }
        } else {
            Log.d("hjy", "没有发现这本书籍");
        }

        //获取宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        openBookByProgress(R.drawable.book_bg, 0);
        //设置进度
        bookPageBezierHelper.setOnProgressChangedListener(new BookPageBezierHelper.OnProgressChangedListener() {
            @Override
            public void setProgress(int currentLength, int totalLength) {
                mcurrentLength = currentLength;
                tvProgress.setText(String.format("%d%%", currentLength * 100 / totalLength));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(BookActivity.this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerSetting.setLayoutManager(layoutManager);
        adapter = new HorizontalAdapter(BookActivity.this);
        recyclerSetting.setAdapter(adapter);
        adapter.setmOnItemClickListener(new HorizontalAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(String result) {
                switch (result) {
                    case "添加书签":
                        SPUtils.putInt(BOOKMARK, mcurrentLength);
                        break;
                    case "读取书签":
                        lastLength = SPUtils.getInt(BOOKMARK, 0);
                        openBookByProgress(R.drawable.book_bg, lastLength);
                        break;
                    case "设置背景":
                        openBookByProgress(R.drawable.book_bg2, lastLength);
                        break;
                    case "语音朗读":
                        if (mTTs == null) {
                            mTTs = new TextToSpeech(BookActivity.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = mTTs.setLanguage(Locale.CHINA);
                                        if (result == TextToSpeech.LANG_MISSING_DATA
                                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("hjy", "onInit: 语言不支持");
                                            Uri uri = Uri.parse("http//acj2.pc6.com/pc6_soure/2017-6/com.iflytek.vflynote_208.apk");
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        } else {
                                            Log.e("hjy", "onInit: 初始化成功");
                                            mTTs.speak(bookPageBezierHelper.getCurrentPageContent(), TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    } else {
                                        Log.e("hjy", "onInit: 初始化错误");
                                    }
                                }
                            });
                        } else {
                            if (mTTs.isSpeaking()) {
                                mTTs.stop();
                            } else {
                                mTTs.speak(bookPageBezierHelper.getCurrentPageContent(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                        break;
                    case "跳转进度":
                        seekbar.setVisibility(View.VISIBLE);
                        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                openBookByProgress(R.drawable.book_bg, seekBar.getProgress() * mTotalLenght / 100);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void openBookByProgress(int background, int progress) {
        //设置 book helper
        bookPageBezierHelper = new BookPageBezierHelper(width, height, progress);
        bookPageView.setBookPageBezierHelper(bookPageBezierHelper);

        //设置 book 当前页和下一页的Bitmap
        nextBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        currentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bookPageView.setBitmaps(currentBitmap, nextBitmap);

        //设置 user setting view listener
        bookPageView.setOnUserNeedSettingListener(new BookPageView.OnUserNeedSettingListener() {
            @Override
            public void onUserNeedSetting() {
                rlSettingView.setVisibility(rlSettingView.getVisibility() == View.VISIBLE ?
                        View.GONE : View.VISIBLE);
            }
        });

        //设置book 的背景
        bookPageBezierHelper.setBackground(this, background);

        //打开书籍
        if (!TextUtils.isEmpty(filePath)) {
            try {
                bookPageBezierHelper.openBook(filePath);
                bookPageBezierHelper.draw(new Canvas(currentBitmap));
                bookPageView.invalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("hjy", "打开书籍失败 ");
        }
    }

    private void loadData() {
        //添加设置数据
        List<String> settings = new ArrayList<>();
        settings.add("添加书签");
        settings.add("读取书签");
        settings.add("设置背景");
        settings.add("语音朗读");
        settings.add("跳转进度");
        adapter.setDataSource(settings);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTTs != null) {
            mTTs.shutdown();
        }
    }
}
