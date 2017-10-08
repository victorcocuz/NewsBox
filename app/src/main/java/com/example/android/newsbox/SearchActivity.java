package com.example.android.newsbox;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import static android.os.Build.VERSION_CODES.M;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>{

    private static final int CARD_TYPE = 0;
    private final static int REQUEST_ID = 0;
    private final static String REQUEST_URL_BASE = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&q=";
    private final static String REQUEST_URL_KEY = "&api-key=test";

    private ArticleAdapter articleAdapter;
    private String urlRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Search item
        Bundle extras = getIntent().getExtras();
        urlRequest = REQUEST_URL_BASE + extras.getString("search") + REQUEST_URL_KEY;

        //Recycler View
        RecyclerView searchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        searchRecyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(CARD_TYPE);
        searchRecyclerView.setAdapter(articleAdapter);

        getSupportLoaderManager().initLoader(REQUEST_ID, null, this).forceLoad();
    }

    //Search Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent (SearchActivity.this, SearchActivity.class);
                intent.putExtra("search", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, urlRequest);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        articleAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

    }
}
