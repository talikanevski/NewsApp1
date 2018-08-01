package com.example.com.newsapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static com.example.com.newsapp1.MainActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving news articles from "The Guardian".
 */
public final class QueryUtils {

    private QueryUtils() {
    }

    private static List<News>   extractFeatureFromJson(String jsonResponse) {

        String section;
        String title;
        String time;
        String url;
        String author;
        String thumbnail;

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news articles to
        List<News> news = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponce = new JSONObject(jsonResponse);

            // Extract the JSONObject associated with the key called "response",
            JSONObject response = baseJsonResponce.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of news articles.
            JSONArray newsArray = response.getJSONArray("results");

            // For each news article in the newsArray, create an news object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news article at position i within the list of news articles.
                JSONObject currentArticle = newsArray.getJSONObject(i);

                /**Extract the value for the key called "sectionName"**/
                section = currentArticle.getString("sectionName");

                /**Extract the value for the key called "webTitle"**/
                title = currentArticle.getString("webTitle");

                /** Extract the value for the key called "webPublicationDate" **/
                time = currentArticle.getString("webPublicationDate");

                /** Extract the value for the key called "webUrl"**/
                url = currentArticle.getString("webUrl");

                /** Extract the value for the key called "fields"**/
                JSONObject fields = currentArticle.getJSONObject("fields");
                thumbnail = fields.getString("thumbnail");//if I am not sure that it will be a string there
                //I can use fields.optString("thumbnail")...

                /** Extract the value for the key called "tags" --> "webTitle",
                 * which represents author or "contributor"**/
                JSONArray tagsArray = currentArticle.getJSONArray("tags");
                //  in the tagsArray:
                author = "";
                for (int j = 0; j < tagsArray.length(); j++) {

                    JSONObject tag = tagsArray.getJSONObject(j);
                    author = author + tag.getString("webTitle");
                }

                News article = new News(section, title, author, time, url, convertToBitmapImage(thumbnail));
                news.add(article);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;
    }
    /** method to convert String thumbnail (which holds URL link of the image of the current article)
     * to Bitmap of this image
     * https://stackoverflow.com/questions/6932369/inputstream-from-a-url
     * https://www.codota.com/code/java/methods/android.graphics.BitmapFactory/decodeStream**/
    private static Bitmap convertToBitmapImage(String thumbnail) {
        Bitmap bitmap = null;
        try {
            InputStream stream = new  URL(thumbnail).openStream();
            bitmap = BitmapFactory.decodeStream(stream);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("QueryUtils", "Failed to create bitmap", e);

        }
        return bitmap;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /** the fetchNewsData() helper method that ties all the steps together -
     * creating a URL, sending the request, processing the response.
     * Since this is the only “public” QueryUtils method that the NewsAsyncTask needs to interact with,
     * make all other helper methods in QueryUtils “private”.**/

    /**
     * Query the The Guardian dataset and return a list of News objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        Log.i(LOG_TAG, "Test:  fetchNewsData called");


//        /** To force the background thread to sleep for 2 seconds,
//         * we are temporarily simulating a very slow network response time.
//         * We are “pretending” that it took a long time to fetch the response.
//         * That allows us to see the loading spinner on the screen for a little longer
//         * than it normally would appear for.**/
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of News
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of news
        return news;
    }
}


