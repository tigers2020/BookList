package com.example.pomkim.booklist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.UUID;

/**
 * Created by pomkim on 11/26/16.
 */

public class BookDetailFragment extends Fragment {
    private static final String LOG_TAG = BookDetailFragment.class.getSimpleName();
    private static final String ARG_BOOK_ID = "book_id";
    private Book mBook;
    private TextView mBookTitleView;
    private TextView mBookSubTitleView;
    private ImageView mBookCoverImageView;
    private TextView mBookAuthorsView;
    private TextView mBookPublisherView;
    private TextView mBookPublishedDateView;
    private TextView mBookPageCountView;
    private RatingBar mBookRatingBar;
    private TextView mBookDescription;


    public static BookDetailFragment newInstance(UUID bookId) {
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_BOOK_ID, bookId);

        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book_detail, container, false);

        mBookTitleView = (TextView) view.findViewById(R.id.book_detail_book_title);
        mBookSubTitleView = (TextView) view.findViewById(R.id.book_detail_book_subtitle);
        mBookCoverImageView = (ImageView) view.findViewById(R.id.book_detail_book_cover_image);
        mBookAuthorsView = (TextView) view.findViewById(R.id.book_detail_book_authors);
        mBookPublisherView = (TextView) view.findViewById(R.id.book_detail_book_publisher);
        mBookPublishedDateView = (TextView) view.findViewById(R.id.book_detail_book_published_date);
        mBookPageCountView = (TextView) view.findViewById(R.id.book_detail_book_pages);
        mBookRatingBar = (RatingBar) view.findViewById(R.id.book_detail_book_rating_bar);
        mBookDescription = (TextView) view.findViewById(R.id.book_detail_book_description);


        mBookTitleView.setText(mBook.getBookTitle());
        mBookSubTitleView.setText(mBook.getBookSubTitle());
        String bookCoverUrl;
        bookCoverUrl = mBook.getBookCoverImage("thumbnail");
        Picasso.with(getContext()).load(bookCoverUrl).into(mBookCoverImageView);
        mBookAuthorsView.setText(mBook.getBookAuthors());
        mBookPublisherView.setText(mBook.getBookPublisher());
        mBookPublishedDateView.setText(mBook.getBookPublishedDate());
        mBookPageCountView.setText(mBook.getBookCount());
        mBookRatingBar.setRating(mBook.getBookRate());
        mBookDescription.setText(mBook.getBookDescription());


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        UUID bookId = (UUID) getArguments().getSerializable(ARG_BOOK_ID);
        mBook = BookUtil.get(getActivity()).getBook(bookId);

    }
}
