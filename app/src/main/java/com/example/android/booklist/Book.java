package com.example.android.booklist;

import java.util.UUID;

/**
 * Created by tiger on 2016-12-01.
 */

public class Book {
    private UUID mBookId;
    private String mBookTitle;
    private String mBookSubTitle;
    private String mBookAuthors;
    private String mBookPublisher;
    private String mBookPublishedDate;
    private int mBookPrintCount;
    private float mBookRatingCount;
    private String mBookDescription;
    private String mBookSmallCoverImage;
    private String mBookCoverImage;
    private String mInfoLink;


    public Book(UUID bookId, String bookTitle, String bookSubTitle, String bookAuthors, String bookPublisher, String bookPublishedDate, int bookPrintCount, float bookRatingCount, String bookDescription, String bookSmallCoverImage, String bookCoverImage, String infoInk) {
        mBookId = bookId;
        mBookTitle = bookTitle;
        mBookSubTitle = bookSubTitle;
        mBookAuthors = bookAuthors;
        mBookPublisher = bookPublisher;
        mBookPublishedDate = bookPublishedDate;
        mBookPrintCount = bookPrintCount;
        mBookRatingCount = bookRatingCount;
        mBookDescription = bookDescription;
        mBookSmallCoverImage = bookSmallCoverImage;
        mBookCoverImage = bookCoverImage;
        mInfoLink = infoInk;
    }

    public float getBookRatingCount() {
        return mBookRatingCount;

    }

    public int getBookPrintCount() {
        return mBookPrintCount;
    }

    public String getBookAuthors() {
        return mBookAuthors;
    }

    public String getBookCoverImage() {
        return mBookCoverImage;
    }

    public String getBookDescription() {
        return mBookDescription;
    }

    public String getBookPublishedDate() {
        return mBookPublishedDate;
    }

    public String getBookPublisher() {
        return mBookPublisher;
    }

    public String getBookSmallCoverImage() {
        return mBookSmallCoverImage;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getInfoLink() {
        return mInfoLink;
    }

    public UUID getBookId() {
        return mBookId;
    }

    public String getBookSubTitle() {
        return mBookSubTitle;
    }
}
