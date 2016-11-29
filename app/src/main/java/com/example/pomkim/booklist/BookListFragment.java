package com.example.pomkim.booklist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by pomkim on 11/25/16.
 */
public class BookListFragment extends Fragment {
    private static final String LOG_TAG = BookListFragment.class.getSimpleName();
    int mCurCheckPosition = 0;

    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String REQUEST_QUERY = "q";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Books", mCurCheckPosition);
    }

    private static final String REQUEST_MAXRESULTS = "maxResults";

    private String mSearchQueryString = "";
    private int mSetMaxResult = 10;

    private BookAdapter mAdapter;
    private List<Book> mBooks;
    private Button mSearchButton;

    public static BookListFragment newInstance() {
        return new BookListFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_list_view, container, false);
        EditText searchEditView = (EditText) view.findViewById(R.id.book_text_search);
        searchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSearchQueryString = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearchButton = (Button) view.findViewById(R.id.book_search_button);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = createUrl();
                BookAsyncTask mTask = new BookAsyncTask();
                mTask.execute(url);
            }
        });

        ListView bookListView = (ListView) view.findViewById(R.id.book_list_view);
        TextView emptyView = (TextView) view.findViewById(R.id.Book_list_empty);
        bookListView.setEmptyView(emptyView);
        mAdapter = new BookAdapter(this.getContext(), new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = mAdapter.getItem(i);
                UUID bookId = book.getBookId();
                Intent intent = BookDetailActivity.newIntent(getActivity(), bookId);

                startActivity(intent);
            }
        });
        return view;

    }

    private String createUrl() {
        mSearchQueryString = Uri.parse(REQUEST_URL).buildUpon().appendQueryParameter(REQUEST_QUERY, mSearchQueryString)
                .appendQueryParameter(REQUEST_MAXRESULTS, Integer.toString(mSetMaxResult)).build().toString();
        return mSearchQueryString;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mCurCheckPosition = savedInstanceState.getInt("Books", 0);

        }
        setRetainInstance(true);
        mBooks = new ArrayList<>();
        String url = createUrl();
        BookAsyncTask mTask = new BookAsyncTask();
        mTask.execute(url);
    }

    private static class BookHolder {
        public ImageView bookSmallCoverImage;
        public TextView bookTitle;
        public TextView bookSubTitle;
        public TextView bookDescription;
        public TextView bookAuthors;

    }

    private class BookAdapter extends ArrayAdapter<Book> {


        public BookAdapter(Context context, List<Book> books) {
            super(context, 0, books);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Book book = getItem(position);
            BookHolder bookHolder = new BookHolder();

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);

                bookHolder.bookSmallCoverImage = (ImageView) view.findViewById(R.id.book_list_item_book_cover);
                bookHolder.bookTitle = (TextView) view.findViewById(R.id.book_list_item_book_title);
                bookHolder.bookSubTitle = (TextView) view.findViewById(R.id.book_list_item_book_subtitle);
                bookHolder.bookDescription = (TextView) view.findViewById(R.id.book_list_item_book_description);
                bookHolder.bookAuthors = (TextView) view.findViewById(R.id.book_list_item_book_authors);

                view.setTag(bookHolder);
            } else {
                bookHolder = (BookHolder) view.getTag();

            }


            Picasso.with(getContext()).load(book.getBookCoverImage("smallThumbnail")).into(bookHolder.bookSmallCoverImage);
            bookHolder.bookTitle.setText(book.getBookTitle());
            bookHolder.bookSubTitle.setText(book.getBookSubTitle());
            bookHolder.bookDescription.setText(book.getBookDescription());
            bookHolder.bookAuthors.setText(book.getBookAuthors());
            return view;
        }
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected void onPostExecute(List<Book> books) {
            mAdapter.clear();
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);

            }

        }

        @Override
        protected List<Book> doInBackground(String... url) {

            if (url.length < 1 || url[0] == null) {
                return null;
            }
            List<Book> books = BookUtil.get(getActivity()).getBooks(url[0]);
            return books;
        }
    }
}
