package com.example.android.newsbox;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int CARD_TYPE = 1;
    private final static String SAVED_REQUEST_URL_1 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=4&tag=environment/series/global-warning&api-key=test";
    private final static String SAVED_REQUEST_URL_2 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=3&tag=science/mars&api-key=test";
    private final static String SAVED_REQUEST_URL_3 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=3&tag=world/africa&api-key=test";
    private final static String SAVED_REQUEST_URL_4 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=5&q=universal%20AND%20income&api-key=test";
    private final static String SHARED_REQUEST_URL_1 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=5&q=toys&api-key=test";
    private final static String SHARED_REQUEST_URL_2 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=3&q=fashion&api-key=test";
    private final static String SHARED_REQUEST_URL_3 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=6&q=tesla&api-key=test";
    private final static String SHARED_REQUEST_URL_4 = "http://content.guardianapis.com/search?show-tags=contributor&show-fields=headline,trailText,thumbnail,body&page-size=4&q=federer&api-key=test";
    private final static int SAVED_REQUEST_ID_1 = 0;
    private final static int SAVED_REQUEST_ID_2 = 1;
    private final static int SAVED_REQUEST_ID_3 = 2;
    private final static int SAVED_REQUEST_ID_4 = 3;
    private final static int SHARED_REQUEST_ID_1 = 4;
    private final static int SHARED_REQUEST_ID_2 = 5;
    private final static int SHARED_REQUEST_ID_3 = 6;
    private final static int SHARED_REQUEST_ID_4 = 7;
    private ArticleAdapter savedAdapter1, savedAdapter2, savedAdapter3, savedAdapter4, sharedAdapter1, sharedAdapter2, sharedAdapter3, sharedAdapter4;

    private int gsvFolderID = 0;
    private ViewPager generalViewNews;
    private ScrollView generalViewSaved, generalViewShared;
    private LinearLayout menuGeneralNews, menuGeneralSaved, menuGeneralShared;
    private ImageView menuGeneralNewsView, menuGeneralSavedView, menuGeneralSharedView;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //General View News
        generalViewNews = (ViewPager) findViewById(R.id.general_view_news);

        FragmentAdapter fragmentAdapter = new FragmentAdapter(this, getSupportFragmentManager());
        generalViewNews.setAdapter(fragmentAdapter);

        tabLayout = (TabLayout) findViewById(R.id.general_view_tab_layout);
        tabLayout.setupWithViewPager(generalViewNews);

        //General View Saved
        generalViewSaved = (ScrollView) findViewById(R.id.general_view_saved);
        generalViewSaved.setVisibility(View.GONE);

        RecyclerView recyclerViewSaved1 = (RecyclerView) findViewById(R.id.gvs_recycler_view_topic1);
        recyclerViewSaved1.setHasFixedSize(false);
        LinearLayoutManager savedLayoutManager1 = new LinearLayoutManager(this);
        recyclerViewSaved1.setLayoutManager(savedLayoutManager1);
        savedAdapter1 = new ArticleAdapter(CARD_TYPE);
        recyclerViewSaved1.setAdapter(savedAdapter1);

        RecyclerView recyclerViewSaved2 = (RecyclerView) findViewById(R.id.gvs_recycler_view_topic2);
        recyclerViewSaved2.setHasFixedSize(false);
        LinearLayoutManager savedLayoutManager2 = new LinearLayoutManager(this);
        recyclerViewSaved2.setLayoutManager(savedLayoutManager2);
        savedAdapter2 = new ArticleAdapter(CARD_TYPE);
        recyclerViewSaved2.setAdapter(savedAdapter2);

        RecyclerView recyclerViewSaved3 = (RecyclerView) findViewById(R.id.gvs_recycler_view_topic3);
        recyclerViewSaved3.setHasFixedSize(false);
        LinearLayoutManager savedLayoutManager3 = new LinearLayoutManager(this);
        recyclerViewSaved3.setLayoutManager(savedLayoutManager3);
        savedAdapter3 = new ArticleAdapter(CARD_TYPE);
        recyclerViewSaved3.setAdapter(savedAdapter3);

        RecyclerView recyclerViewSaved4 = (RecyclerView) findViewById(R.id.gvs_recycler_view_topic4);
        recyclerViewSaved4.setHasFixedSize(false);
        LinearLayoutManager savedLayoutManager4 = new LinearLayoutManager(this);
        recyclerViewSaved4.setLayoutManager(savedLayoutManager4);
        savedAdapter4 = new ArticleAdapter(CARD_TYPE);
        recyclerViewSaved4.setAdapter(savedAdapter4);

        ImageView gvsAdd = (ImageView) findViewById(R.id.gvs_add);
        gvsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout gsvLayout = (LinearLayout) findViewById(R.id.general_view_saved_add);
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View folderView = inflater.inflate(R.layout.general_view_saved_folder, (ViewGroup) findViewById(R.id.general_view_folder));
                gsvLayout.addView(folderView);

                gsvFolderID++;
                int newID = folderView.generateViewId() + gsvFolderID;
                folderView.setId(newID);
            }
        });

        //General View Shared
        generalViewShared = (ScrollView) findViewById(R.id.general_view_shared);
        generalViewShared.setVisibility(View.GONE);

        RecyclerView recyclerViewShared1 = (RecyclerView) findViewById(R.id.gvsh_recycler_view_topic1);
        recyclerViewShared1.setHasFixedSize(false);
        LinearLayoutManager SharedLayoutManager1 = new LinearLayoutManager(this);
        recyclerViewShared1.setLayoutManager(SharedLayoutManager1);
        sharedAdapter1 = new ArticleAdapter(CARD_TYPE);
        recyclerViewShared1.setAdapter(sharedAdapter1);

        RecyclerView recyclerViewShared2 = (RecyclerView) findViewById(R.id.gvsh_recycler_view_topic2);
        recyclerViewShared2.setHasFixedSize(false);
        LinearLayoutManager SharedLayoutManager2 = new LinearLayoutManager(this);
        recyclerViewShared2.setLayoutManager(SharedLayoutManager2);
        sharedAdapter2 = new ArticleAdapter(CARD_TYPE);
        recyclerViewShared2.setAdapter(sharedAdapter2);

        RecyclerView recyclerViewShared3 = (RecyclerView) findViewById(R.id.gvsh_recycler_view_topic3);
        recyclerViewShared3.setHasFixedSize(false);
        LinearLayoutManager SharedLayoutManager3 = new LinearLayoutManager(this);
        recyclerViewShared3.setLayoutManager(SharedLayoutManager3);
        sharedAdapter3 = new ArticleAdapter(CARD_TYPE);
        recyclerViewShared3.setAdapter(sharedAdapter3);

        RecyclerView recyclerViewShared4 = (RecyclerView) findViewById(R.id.gvsh_recycler_view_topic4);
        recyclerViewShared4.setHasFixedSize(false);
        LinearLayoutManager SharedLayoutManager4 = new LinearLayoutManager(this);
        recyclerViewShared4.setLayoutManager(SharedLayoutManager4);
        sharedAdapter4 = new ArticleAdapter(CARD_TYPE);
        recyclerViewShared4.setAdapter(sharedAdapter4);

        //Buttons
        menuGeneralNews = (LinearLayout) findViewById(R.id.menu_general_news);
        menuGeneralNewsView = (ImageView) findViewById(R.id.menu_general_news_image);
        menuGeneralNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.setVisibility(View.VISIBLE);
                generalViewNews.setVisibility(View.VISIBLE);
                generalViewSaved.setVisibility(View.GONE);
                generalViewShared.setVisibility(View.GONE);

                menuGeneralNewsView.setImageResource(R.drawable.ic_white_news);
                menuGeneralSavedView.setImageResource(R.drawable.ic_medium_saved);
                menuGeneralSharedView.setImageResource(R.drawable.ic_medium_shared);
            }
        });

        menuGeneralSaved = (LinearLayout) findViewById(R.id.menu_general_saved);
        menuGeneralSavedView = (ImageView) findViewById(R.id.menu_general_saved_image);
        menuGeneralSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.setVisibility(View.GONE);
                generalViewNews.setVisibility(View.GONE);
                generalViewSaved.setVisibility(View.VISIBLE);
                generalViewShared.setVisibility(View.GONE);

                menuGeneralNewsView.setImageResource(R.drawable.ic_medium_news);
                menuGeneralSavedView.setImageResource(R.drawable.ic_white_saved);
                menuGeneralSharedView.setImageResource(R.drawable.ic_medium_shared);
            }
        });

        menuGeneralShared = (LinearLayout) findViewById(R.id.menu_general_shared);
        menuGeneralSharedView = (ImageView) findViewById(R.id.menu_general_shared_image);
        menuGeneralShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.setVisibility(View.GONE);
                generalViewNews.setVisibility(View.GONE);
                generalViewSaved.setVisibility(View.GONE);
                generalViewShared.setVisibility(View.VISIBLE);

                menuGeneralNewsView.setImageResource(R.drawable.ic_medium_news);
                menuGeneralSavedView.setImageResource(R.drawable.ic_medium_saved);
                menuGeneralSharedView.setImageResource(R.drawable.ic_white_shared);
            }
        });

        getSupportLoaderManager().initLoader(SAVED_REQUEST_ID_1, null, this).forceLoad();
        getSupportLoaderManager().initLoader(SAVED_REQUEST_ID_2, null, this).forceLoad();
        getSupportLoaderManager().initLoader(SAVED_REQUEST_ID_3, null, this).forceLoad();
        getSupportLoaderManager().initLoader(SAVED_REQUEST_ID_4, null, this).forceLoad();
        getSupportLoaderManager().initLoader(SHARED_REQUEST_ID_1, null, this).forceLoad();
        getSupportLoaderManager().initLoader(SHARED_REQUEST_ID_2, null, this).forceLoad();
        getSupportLoaderManager().initLoader(SHARED_REQUEST_ID_3, null, this).forceLoad();
        getSupportLoaderManager().initLoader(SHARED_REQUEST_ID_4, null, this).forceLoad();

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
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Loader Callback Methods
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                return new ArticleLoader(this, SAVED_REQUEST_URL_1);
            case 1:
                return new ArticleLoader(this, SAVED_REQUEST_URL_2);
            case 2:
                return new ArticleLoader(this, SAVED_REQUEST_URL_3);
            case 3:
                return new ArticleLoader(this, SAVED_REQUEST_URL_4);
            case 4:
                return new ArticleLoader(this, SHARED_REQUEST_URL_1);
            case 5:
                return new ArticleLoader(this, SHARED_REQUEST_URL_2);
            case 6:
                return new ArticleLoader(this, SHARED_REQUEST_URL_3);
            case 7:
                return new ArticleLoader(this, SHARED_REQUEST_URL_4);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        switch (loader.getId()) {
            case 0:
                savedAdapter1.addAll(data);
                break;
            case 1:
                savedAdapter2.addAll(data);
                break;
            case 2:
                savedAdapter3.addAll(data);
                break;
            case 3:
                savedAdapter4.addAll(data);
                break;
            case 4:
                sharedAdapter1.addAll(data);
                break;
            case 5:
                sharedAdapter2.addAll(data);
                break;
            case 6:
                sharedAdapter3.addAll(data);
                break;
            case 7:
                sharedAdapter4.addAll(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

    }
}
