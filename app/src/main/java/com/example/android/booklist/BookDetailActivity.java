package com.example.android.booklist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

public class BookDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = BookDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Intent intent = getIntent();

        String bookId = (String) intent.getSerializableExtra("BookId");

        Log.i(LOG_TAG, "bookId: "+ bookId);
        Book book = BookUtil.get(this).getBook(bookId);

        BookHolder holder = new BookHolder();

            holder.BookTitleView = (TextView) findViewById(R.id.book_detail_book_title);
            holder.BookSubTitleView = (TextView) findViewById(R.id.book_detail_book_subtitle);
            holder.BookImageView = (ImageView) findViewById(R.id.book_detail_book_cover_image);
            holder.BookAuthorsView = (TextView) findViewById(R.id.book_detail_book_authors);
            holder.BookPublishedDate = (TextView) findViewById(R.id.book_detail_book_publishedDate);
            holder.BookPublisher = (TextView) findViewById(R.id.book_detail_book_publisher);
            holder.BookRatingBar = (RatingBar) findViewById(R.id.book_detail_book_rating_bar);
            holder.BookDescription = (TextView) findViewById(R.id.book_detail_book_description);

        holder.BookTitleView.setText(book.getBookTitle());
        holder.BookSubTitleView.setText(book.getBookSubTitle());
        if(book.getBookCoverImage().equals("") && TextUtils.isEmpty(book.getBookCoverImage())){
            Picasso.with(this).load(R.drawable.booknotpictured).into(holder.BookImageView);
        }else {
            Picasso.with(this).load(book.getBookCoverImage()).into(holder.BookImageView);
        }
        holder.BookAuthorsView.setText(book.getBookAuthors());
        holder.BookPublishedDate.setText(book.getBookPublishedDate());
        holder.BookPublisher.setText((book.getBookPublisher()));
        holder.BookRatingBar.setRating(book.getBookRatingCount());
        holder.BookRatingBar.setNumStars(5);
        holder.BookDescription.setText(book.getBookDescription());




        Log.i(LOG_TAG, "BookId: " + bookId);
    }
    private class BookHolder
    {
        public TextView BookTitleView;
        public TextView BookSubTitleView;
        public ImageView BookImageView;
        public TextView BookAuthorsView;
        public TextView BookPublishedDate;
        public TextView BookPublisher;
        public RatingBar BookRatingBar;
        public TextView BookDescription;
    }
}
