package com.example.android.newsbox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by victo on 9/30/2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Article> extractArticle(String urlRequest) {

        URL url = getURL(urlRequest);
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<Article> articles = extractDataFromJSON(jsonResponse);
        return articles;
    }

    public static URL getURL(String urlRequest) {
        URL url = null;
        try {
            url = new URL(urlRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "URL is not valid", e);
        }
        return url;
    }

    public static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code" + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not retreive JSON results", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Article> extractDataFromJSON(String jsonResponse) {

        List<Article> articles = new ArrayList<>();

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        try {
            JSONObject jsonArticle = new JSONObject(jsonResponse);
            JSONObject jsonObjectResponse = jsonArticle.getJSONObject("response");
            JSONArray jsonResults = jsonObjectResponse.getJSONArray("results");

            for (int i = 0; i < jsonResults.length(); i++) {
                JSONObject jsonItem = jsonResults.getJSONObject(i);

                //Get webTitle
                String webTitle = jsonItem.getString("webTitle");

                //Get webPublicationDate
                String webPublicationDate = jsonItem.getString("webPublicationDate");

                //Get trailText
                JSONObject jsonFields = jsonItem.getJSONObject("fields");
                String trailText = Html.fromHtml(jsonFields.getString("trailText")).toString();

                //Get thumbnail
                Bitmap thumbnail = null;
                if (jsonFields.has("thumbnail")) {
                    String thumbnailRequest = jsonFields.getString("thumbnail");
                    thumbnail = BitmapFactory.decodeStream(getURL(thumbnailRequest).openConnection().getInputStream());
                }
                else {
                    continue;
                }

                //Get body
                String body = Html.fromHtml(jsonFields.getString("body")).toString();


                //Get author
                JSONArray jsonTags = jsonItem.getJSONArray("tags");
                StringBuilder authorBuilder = new StringBuilder();
                int k = 0;

                for (int j = 0; j < jsonTags.length(); j++) {
                    JSONObject jsonContributor = jsonTags.getJSONObject(j);

                    if (jsonContributor.has("webTitle")) {
                        k++;
                        String contributorWebTitle = jsonContributor.getString("webTitle");
                        if (j != 0) {
                            authorBuilder.append(" , ");
                        }
                        if (contributorWebTitle != null) {
                            authorBuilder.append(contributorWebTitle);
                        }
                    }
                }

                String contributor = authorBuilder.toString();

                if (k == 0) {
                    contributor = "Anonymous";
                }


                //Add all
                articles.add(new Article(thumbnail, webTitle, trailText, contributor, body, webPublicationDate));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON results", e);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not receive thumbnail", e);
        }

        return articles;
    }
}


