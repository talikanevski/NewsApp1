package com.example.com.newsapp1;

public class News {

    /**
     * section of the news article
     */
    private String mSection;

    /**
     * title of the news article
     */
    private String mTitle;

    /**
     * author of the news article
     */
    private String mAuthor;

    /**
     * Time when the news article was published
     */
    private String mTime;

    /**
     * Website URL of the news article
     */
    private String mUrl;

    public News(String section, String title, String author, String time, String url) {
        mSection = section;
        mTitle = title;
        mAuthor = author;
        mTime = time;
        mUrl = url;
    }

    public String getSection() {return mSection;}

    public String getAuthor() {return mAuthor;}

    public String getTitle() {return mTitle;}

    public String getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }
}
