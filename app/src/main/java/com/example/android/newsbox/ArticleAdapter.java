package com.example.android.newsbox;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by victo on 9/28/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;
    private int cardType;
    private String dateAndTime;


    public ArticleAdapter(int cardType) {
        this.cardType = cardType;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (cardType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card_large, parent, false);
                return new ViewHolder(v);
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card_small, parent, false);
                return new ViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, final int position) {
        holder.articleCardThumbnailView.setImageBitmap(articles.get(position).getArticleThumbnail());
        holder.articleCardWebTitleView.setText(articles.get(position).getArticleWebTitle());

        if (cardType == 0) {
            holder.articleCardTrailTextView.setText(articles.get(position).getArticleTrailText());
            holder.articleCardContributorView.setText("Contributor: " + articles.get(position).getArticleContributor());

            String date = articles.get(position).getArticleWebPublicationDate();
            String[] dateSplit = date.split("T");
            String time = dateSplit[1];
            dateAndTime = dateSplit[0] + " " + time.substring(0, time.length() - 1);

            holder.articleCardWebPublicationDate.setText(dateAndTime);
        }

        holder.articleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap thumbnail = articles.get(position).getArticleThumbnail();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                String body = articles.get(position).getArticleBody();

                Intent goToArticleActivity = new Intent(v.getContext(), ArticleActivity.class);
                goToArticleActivity.putExtra("thumbnail", byteArray);
                goToArticleActivity.putExtra("body", body);
                goToArticleActivity.putExtra("webTitle", articles.get(position).getArticleWebTitle());
                goToArticleActivity.putExtra("trailText", articles.get(position).getArticleTrailText());
                goToArticleActivity.putExtra("contributor", articles.get(position).getArticleContributor());
                goToArticleActivity.putExtra("dateAndTime", dateAndTime);
                v.getContext().startActivity(goToArticleActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articles != null) {
            return articles.size();
        }
        return 0;
    }

    public void addAll(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView articleCardThumbnailView;
        private TextView articleCardWebTitleView;
        private TextView articleCardTrailTextView;
        private TextView articleCardContributorView;
        private TextView articleCardWebPublicationDate;
        private CardView articleCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            articleCardThumbnailView = (ImageView) itemView.findViewById(R.id.article_card_thumbnail);
            articleCardWebTitleView = (TextView) itemView.findViewById(R.id.article_card_web_title);
            articleCardView = (CardView) itemView.findViewById(R.id.article_card_view);

            if(cardType == 0) {
                articleCardTrailTextView = (TextView) itemView.findViewById(R.id.article_card_trail_text);
                articleCardContributorView = (TextView) itemView.findViewById(R.id.article_card_contributor);
                articleCardWebPublicationDate = (TextView) itemView.findViewById(R.id.article_card_web_publication_date);
            }
        }
    }
}
