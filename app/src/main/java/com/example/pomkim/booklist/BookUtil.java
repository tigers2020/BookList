package com.example.pomkim.booklist;

import android.content.Context;
import android.net.Uri;
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
import java.util.UUID;

/**
 * Created by pomkim on 11/26/16.
 */

public class BookUtil {

    private static final String LOG_TAG = BookUtil.class.getSimpleName();
    private static ArrayList<Book> mBooks;
    private static BookUtil sBookUtil;

    private BookUtil(Context context) {
        mBooks = new ArrayList<>();
    }

    public static BookUtil get(Context context) {

        if (sBookUtil == null) {
            sBookUtil = new BookUtil(context);
        }
        return sBookUtil;
    }

    private static ArrayList<Book> extractBooks(String BookJson) {
        if (TextUtils.isEmpty(BookJson)) {
            return null;
        }

        mBooks = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(BookJson);
            JSONArray items = root.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject currentBook = items.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                UUID bookId = UUID.randomUUID();
                String title = volumeInfo.getString("title");
                String subtitle = "";
                if (volumeInfo.has("subtitle")) {
                    subtitle = volumeInfo.getString("subtitle");
                }
                String description = "";
                if (volumeInfo.has("description")) {
                    description = volumeInfo.getString("description");
                }
                JSONArray authors = volumeInfo.getJSONArray("authors");
                String authorsString;
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < authors.length(); j++) {
                    builder.append(authors.getString(j));
                    builder.append(", ");
                }
                authorsString = builder.toString();

                Uri bookUri = Uri.parse(volumeInfo.getString("infoLink"));

                String bookPublisher = volumeInfo.getString("publisher");
                String bookPublishedDate = "";
                if (volumeInfo.has("publishedDate")) {
                    bookPublishedDate = volumeInfo.getString("publishedDate");
                }
                String bookCount = "no pages";
                if (volumeInfo.has("pageCount")) {
                    bookCount = volumeInfo.getString("pageCount") + " pages";
                }
                float bookRate = 0;
                if (volumeInfo.has("averageRating")) {
                    bookRate = (float) volumeInfo.getDouble("averageRating");
                }

                JSONObject bookCoverImage = volumeInfo.getJSONObject("imageLinks");

                Book book = new Book(bookId, title, subtitle, description, authorsString, bookUri, bookPublisher, bookPublishedDate, bookCount, bookRate, bookCoverImage);

                mBooks.add(book);

            }

        } catch (JSONException je) {
            Log.e(LOG_TAG, "JSON Failed: ", je);
        }

        return mBooks;

    }

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String JsonResponse = "";
        try {
            JsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "makeHttpRequest Error: ", e);
        }
        mBooks = extractBooks(JsonResponse);

        return mBooks;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response Message:  " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

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


    private static URL createUrl(String requestUrl) {
        String stringUrl = requestUrl;

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    public List<Book> getBooks(String url) {
        return fetchBookData(url);
    }

    public Book getBook(UUID id) {
        for (Book book : mBooks) {
            if (book.getBookId().equals(id)) {
                return book;
            }
        }
        return null;
    }
}