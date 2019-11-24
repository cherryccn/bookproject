package com.hjy.bookproject.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjy.bookproject.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjy on 2019/11/23
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Activity mContext;
    private List<String> mSettings;
    private OnItemClickListener mOnItemClickListener;
    private DisplayMetrics displayMetrics = new DisplayMetrics();

    public HorizontalAdapter(Activity context) {
        mContext = context;
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDataSource(List<String> settings) {
        mSettings = settings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalAdapter.ViewHolder holder, int position) {
        if (mSettings != null && mSettings.size() > 0) {
            int width = displayMetrics.widthPixels / 4;
            holder.tvSettingName.setWidth(width);
            holder.tvSettingName.setText(mSettings.get(position));
            holder.tvSettingName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.setOnItemClickListener(mSettings.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSettings != null && mSettings.size() > 0 ? mSettings.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_setting_name)
        TextView tvSettingName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void setOnItemClickListener(String result);
    }
}
