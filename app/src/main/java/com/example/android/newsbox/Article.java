package com.example.android.newsbox;

import android.graphics.Bitmap;

/**
 * Created by victo on 9/28/2017.
 */

public class Article {
    private Bitmap articleThumbnail;
    private String articleWebTitle;
    private String articleTrailText;
    private String articleContributor;
    private String articleBody;
    private String articleWebPublicationDate;

    public Article(Bitmap articleThumbnail, String articleTitle, String articleTrailText, String articleContributor, String articleBody, String articleWebPublicationDate) {
        this.articleThumbnail = articleThumbnail;
        this.articleWebTitle = articleTitle;
        this.articleTrailText = articleTrailText;
        this.articleContributor = articleContributor;
        this.articleBody = articleBody;
        this.articleWebPublicationDate = articleWebPublicationDate;
    }

    public Bitmap getArticleThumbnail() {
        return articleThumbnail;
    }

    public String getArticleWebTitle() {
        return articleWebTitle;
    }

    public String getArticleTrailText() {
        return articleTrailText;
    }

    public String getArticleContributor() {
        return articleContributor;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public String getArticleWebPublicationDate() {
        return articleWebPublicationDate;
    }


}
