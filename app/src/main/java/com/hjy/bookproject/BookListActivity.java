package com.hjy.bookproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BookListActivity.class);
        context.startActivity(intent);
    }
}
