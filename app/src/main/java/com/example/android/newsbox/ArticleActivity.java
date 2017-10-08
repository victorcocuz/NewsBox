package com.example.android.newsbox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {

    private static final String LOG_TAG = ArticleActivity.class.getSimpleName();

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        extras = getIntent().getExtras();

        ImageView thumbnailView = (ImageView) findViewById(R.id.article_thumbnail);
        byte[] byteArray = getIntent().getByteArrayExtra("thumbnail");
        Bitmap thumbnail = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        thumbnailView.setImageBitmap(thumbnail);

        TextView webTitleView = (TextView) findViewById(R.id.article_web_title);
        webTitleView.setText(extras.getString("webTitle"));

        TextView trailTextView = (TextView) findViewById(R.id.article_trail_text);
        trailTextView.setText(extras.getString("trailText"));

        TextView contributorView = (TextView) findViewById(R.id.article_contributor);
        contributorView.setText("Contributor: " + extras.getString("contributor"));

        TextView dateAndTimeView = (TextView) findViewById(R.id.article_date_and_time);
        dateAndTimeView.setText("Date: " + extras.getString("dateAndTime"));

        TextView bodyView = (TextView) findViewById(R.id.article_body);
        bodyView.setText(extras.getString("body"));

    }
}
