package com.hjy.bookproject.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hjy.bookproject.R;
import com.hjy.bookproject.bean.BookListBean;
import com.hjy.bookproject.net.Api;
import com.hjy.bookproject.ui.adapter.BookListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static androidx.recyclerview.widget.RecyclerView.ItemAnimator.FLAG_INVALIDATED;

public class BookListActivity extends AppCompatActivity {

    private static final String TAG = "BookListActivity";

    @BindView(R.id.recycler_books)
    RecyclerView recyclerBooks;

    private Context mContext;
    private BookListAdapter bookListAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BookListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        mContext = BookListActivity.this;
        initView();
        loadData();
    }

    private void initView() {
        recyclerBooks.setLayoutManager(new LinearLayoutManager(mContext));
        bookListAdapter = new BookListAdapter(mContext);
        recyclerBooks.setAdapter(bookListAdapter);
        recyclerBooks.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Api.getBookLists(), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                //todo:可以显示progressbar
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Gson gson = new Gson();
                BookListBean bookListBean = gson.fromJson(result, BookListBean.class);
                if (bookListBean != null) {
                    List<BookListBean.DataBean> dataBeans = bookListBean.getData();
                    if (dataBeans != null && dataBeans.size() > 0 && bookListAdapter != null) {
                        bookListAdapter.setDataSource(dataBeans);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "Cause:" + error.getCause()+"  Message:" + error.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //todo：可以关闭progressbar
            }
        });
    }

}
