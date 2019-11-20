package com.hjy.bookproject.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hjy.bookproject.R;
import com.hjy.bookproject.bean.BookListBean;
import com.hjy.bookproject.net.Api;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class BookListActivity extends AppCompatActivity {

    private static final String TAG = "BookListActivity";

    @BindView(R.id.rv_books)
    RecyclerView rvBooks;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BookListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Api.getBookLists(), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                //todo:可以显示progressbar
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: " + new String(responseBody));
                String result = new String(responseBody);
                Gson gson = new Gson();
                BookListBean bookListBean = gson.fromJson(result, BookListBean.class);
                List<BookListBean.DataBean> dataBeans = bookListBean.getData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                //todo：可以关闭progressbar
            }
        });
    }

}
