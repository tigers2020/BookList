package com.example.android.booklist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tiger on 2016-12-01.
 */

public class BookListAdapter extends ArrayAdapter<Book> {
    public BookListAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Book book = getItem(position);
        BookHolder holder = new BookHolder();

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);

            holder.bookCoverView = (ImageView) view.findViewById(R.id.book_list_cover_image);
            holder.bookTitleView = (TextView) view.findViewById(R.id.book_list_title);
            holder.bookSubTitleView = (TextView) view.findViewById(R.id.book_list_sub_title);
            holder.bookPublishedDateView = (TextView) view.findViewById(R.id.book_list_published_date);

            view.setTag(holder);
        } else {
            holder = (BookHolder) view.getTag();
        }

        if(book.getBookSmallCoverImage().equals("") && TextUtils.isEmpty(book.getBookSmallCoverImage())){
            Picasso.with(getContext()).load(R.drawable.booknotpictured).into(holder.bookCoverView);
        }else {
            Picasso.with(getContext()).load(book.getBookSmallCoverImage()).into(holder.bookCoverView);
        }
        holder.bookTitleView.setText(book.getBookTitle());
        holder.bookSubTitleView.setText(book.getBookSubTitle());
        holder.bookPublishedDateView.setText(book.getBookPublishedDate());

        return view;
    }

    private class BookHolder {
        public ImageView bookCoverView;
        public TextView bookTitleView;
        public TextView bookSubTitleView;
        public TextView bookPublishedDateView;
    }
}
