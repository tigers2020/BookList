package com.example.android.booklist;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookListActivity.class.getSimpleName();
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String REQUEST_QUERY = "q";
    private static final String REQUEST_MAX_RESULTS = "maxResults";
    private static final int BOOK_LOADER_ID = 1;
    private String mSearchQueryString = "";
    private String mSearchString = "*";
    private int mMaxResults = 10;
    private List<Book> mBooks;
    private BookListAdapter mListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mBooks = new ArrayList<>();
        mSearchQueryString = createUrl();
        EditText searchEditView = (EditText) findViewById(R.id.book_list_search_edit_text_view);
        TextView emptyView = (TextView) findViewById(R.id.book_list_empty);
        searchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSearchString = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchQueryString = createUrl();
            }
        });
        Spinner maxResultSpinner = (Spinner) findViewById(R.id.book_list_search_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.max_results_numbers, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxResultSpinner.setAdapter(spinnerAdapter);

        maxResultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mMaxResults = Integer.parseInt(adapterView.getSelectedItem().toString());
                mSearchQueryString = createUrl();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mMaxResults = 10;

            }
        });

        ListView bookListView = (ListView) findViewById(R.id.book_list);

        bookListView.setEmptyView(emptyView);
        mListAdapter = new BookListAdapter(this, mBooks);

        bookListView.setAdapter(mListAdapter);
        Button searchButton = (Button) findViewById(R.id.book_list_search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = createUrl();
                Toast.makeText(getBaseContext(), url, Toast.LENGTH_SHORT).show();
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);
    }

    private String createUrl() {
        String url = Uri.parse(REQUEST_URL).buildUpon().appendQueryParameter(REQUEST_QUERY, mSearchString)
                .appendQueryParameter(REQUEST_MAX_RESULTS, Integer.toString(mMaxResults)).build().toString();
        return url;
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, mSearchQueryString);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mListAdapter.clear();
        mBooks = books;

        if (books != null && !books.isEmpty()) {
            mListAdapter.addAll(mBooks);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        mListAdapter.clear();

    }
}
