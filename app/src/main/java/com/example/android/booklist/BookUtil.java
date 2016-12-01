package com.example.android.booklist;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tiger on 2016-12-01.
 */

public class BookUtil {
    private static final String LOG_TAG = BookUtil.class.getSimpleName();

    private static BookUtil sBookUtil;
    private static List<Book> mBooks;


    private BookUtil(Context context) {
        mBooks = new ArrayList<>();


    }

    private static List<Book> extractBook(String bookJson) {
        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }

        mBooks = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(bookJson);
            JSONArray items = root.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject book = items.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                UUID bookId = UUID.randomUUID();
                String bookTitle = volumeInfo.getString("title");
                String bookSubTitle = "";
                if (volumeInfo.has("subTitle")) {
                    bookSubTitle = volumeInfo.getString("subTitle");
                }
                String bookAuthors = "";
                StringBuilder builder = new StringBuilder();

                for (int j = 0; j < bookAuthors.length(); j++) {
                    String authors = volumeInfo.getJSONArray("authors").getString(j);
                    builder.append(authors);
                    builder.append("\n");
                }
                bookAuthors = builder.toString();

                String publsiher = volumeInfo.getString("publisher");
                String publishedDate = "";
                if (volumeInfo.has("publishedDate")) {
                    publishedDate = volumeInfo.getString("publishedDate");
                }
                int bookPritCount = 0;
                if (volumeInfo.has("pageCount")) {
                    bookPritCount = volumeInfo.getInt("pageCount");
                }
                float ratingCount = 0;
                if (volumeInfo.has("averageRating")) {
                    ratingCount = (float) volumeInfo.getDouble("averageRating");
                }
                String smallBookCoverImage = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail");
                String thumbBookCoverImage = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

                String bookDescription = "";
                if (volumeInfo.has("description")) {
                    bookDescription = volumeInfo.getString("description");
                }
                String bookInfoLink = "";
                if (volumeInfo.has("infoLink")) {
                    bookInfoLink = volumeInfo.getString("infoLink");
                }

                Book thisBook = new Book(bookId, bookTitle, bookSubTitle, bookAuthors, publsiher, publishedDate, bookPritCount, ratingCount, bookDescription, smallBookCoverImage, thumbBookCoverImage, bookInfoLink);

                mBooks.add(thisBook);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to extract Json Book", e);
        }


        return mBooks;
    }

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "makeHttpRequest Error: ", e);
        }
        mBooks = extractBook(jsonResponse);


        return mBooks;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        Log.i(LOG_TAG, "Request URL = " + url.toString());
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
                Log.e(LOG_TAG, "Error response Message: " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem retrieving the Book JSON results.", e);
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
        StringBuilder builder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return builder.toString();
    }

    private static URL createUrl(String requestUrl) {

        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem build the URL : ", e);
        }

        return url;
    }


    public static BookUtil get(Context context) {
        if (sBookUtil == null) {
            sBookUtil = new BookUtil(context);
        }
        return sBookUtil;
    }

    public List<Book> getBooks(String url) {
        return fetchBookData(url);
    }

    public Book getBook(UUID bookId) {
        for (Book book : mBooks) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

}
