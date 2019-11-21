package com.hjy.bookproject.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjy.bookproject.R;
import com.hjy.bookproject.bean.BookListBean;
import com.hjy.bookproject.net.Api;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder> {

    private Context mContext;
    private List<BookListBean.DataBean> mDataSource;

    public BookListAdapter(Context context) {
        mContext = context;
    }

    public void setDataSource(List<BookListBean.DataBean> dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataSource != null ? mDataSource.size() : 0;
    }

    @NonNull
    @Override
    public BookListAdapter.BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_list, parent, false);
        return new BookListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.BookListViewHolder holder, int position) {
        if (mDataSource != null && mDataSource.size() > 0) {
            BookListBean.DataBean dataBean = mDataSource.get(position);
            holder.tvName.setText(dataBean.getBookname());

            //获取文件名称
            String fileName = getFileName(dataBean.getBookname(), dataBean.getBookfile());
            File file = new File(Api.getFileRootPath() + fileName);
            holder.btnDownload.setText(file.exists() ? "打开" : "下载");
            holder.btnDownload.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ("下载".equals(holder.btnDownload.getText().toString())) {
                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        httpClient.addHeader("Accept-Encoding", "identity");
                        httpClient.get(dataBean.getBookfile(), new FileAsyncHttpResponseHandler(file) {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                                holder.btnDownload.setText("下载失败");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, File file) {
                                holder.btnDownload.setText("打开");
                            }

                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                super.onProgress(bytesWritten, totalSize);
                                holder.btnDownload.setText(String.valueOf(bytesWritten * 100 / totalSize) + "%");
                            }
                        });
                    } else {
                        //todo：打开书籍
                    }
                }
            });
        }
    }

    /**
     * 获取文件名称
     *
     * @param bookname
     * @param bookfile
     * @return
     */
    private String getFileName(String bookname, String bookfile) {
        String suffix = bookfile.substring(bookfile.lastIndexOf("."));
        return bookname + suffix;
    }

    class BookListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.btn_download)
        Button btnDownload;

        BookListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
