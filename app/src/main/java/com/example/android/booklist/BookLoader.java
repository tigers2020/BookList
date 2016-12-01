package com.example.android.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by tiger on 2016-12-01.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private static final String LOG_TAG = BookLoader.class.getSimpleName();

    String mUrl;
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<Book> books = BookUtil.get(getContext()).getBooks(mUrl);
        return books;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
