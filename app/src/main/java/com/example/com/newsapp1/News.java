package com.example.com.newsapp1;

import android.graphics.Bitmap;

public class News {

    private String mSection; /*** section of the news article*/
    private String mTitle; /** title of the news article  */
    private String mAuthor;/**author of the news article */
    private String mDate; /**date when the news article was published*/
    private String mTime;/** Time when the news article was published */
    private String mUrl; /*** Website URL of the news article*/
    private Bitmap mthumbnail;/** thumbnail of the news article - a picture.*/

    public News(String section, String title, String author, String date,String time, String url, Bitmap thumbnail) {
        mSection = section;
        mTitle = title;
        mAuthor = author;
        mTime = time;
        mDate = date;
        mUrl = url;
        mthumbnail = thumbnail;
    }

    public String getSection() {return mSection;}

    public String getAuthor() {return mAuthor;}

    public String getTitle() {return mTitle;}

    public String getmDate() {return mDate;}

    public String getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }

    public Bitmap getThumbnail() {return mthumbnail;}
}
