package com.example.android.newsbox;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.ImageView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHighlights extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>> {

    private final static String REQUEST_URL_BASE = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&use-date=published";
    private final static String REQUEST_URL_KEY = "api-key=test";

    private final static String LOG_TAG = FragmentHighlights.class.getSimpleName();
    private final static int NEWS_REQUEST_ID = 0;
    private final static String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&use-date=published&api-key=test";
    private final static int CARD_TYPE = 0;

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private ArticleAdapter articleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    public FragmentHighlights() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_highlights, container, false);

        // RecyclerView
        RecyclerView articleRecyclerView = (RecyclerView) rootView.findViewById(R.id.highlights_recycler_view);
        articleRecyclerView.setHasFixedSize(false);
        final LinearLayoutManager articleLayoutManager = new LinearLayoutManager(getContext());
        articleRecyclerView.setLayoutManager(articleLayoutManager);
        articleAdapter = new ArticleAdapter(CARD_TYPE);
        articleRecyclerView.setAdapter(articleAdapter);

        getLoaderManager().initLoader(NEWS_REQUEST_ID, null, this).forceLoad();

        // Load more
        articleRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = articleLayoutManager.getChildCount();
                    totalItemCount = articleLayoutManager.getItemCount();
                    pastVisibleItems = articleLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        getLoaderManager().initLoader(NEWS_REQUEST_ID, null, FragmentHighlights.this).forceLoad();
                    }
                }
            }
        });

        // Refresh
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.news_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        getLoaderManager().initLoader(NEWS_REQUEST_ID, null, FragmentHighlights.this).forceLoad();
                    }
                },3000);
            }
        });
        return rootView;
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String pageSize = sharedPrefs.getString(getString(R.string.settings_page_size_key),getString(R.string.settings_page_size_default));

        Uri baseUri = Uri.parse(REQUEST_URL_BASE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("api-key", "test");
        return new ArticleLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        articleAdapter.addAll(data);
        ImageView splashScreen = (ImageView) getActivity().findViewById(R.id.splash_screen);
        splashScreen.setVisibility(View.GONE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
    }
}
