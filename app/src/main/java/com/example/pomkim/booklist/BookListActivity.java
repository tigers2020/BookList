package com.example.pomkim.booklist;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

public class BookListActivity extends SingleFragmentActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected Fragment createFragment() {
        return BookListFragment.newInstance();
    }
}
