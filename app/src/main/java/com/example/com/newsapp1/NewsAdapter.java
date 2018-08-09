package com.example.com.newsapp1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class    NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(Activity context, ArrayList<News> news) {

        /**
         the second argument is used when the ArrayAdapter is populating a single TextView.
         Because this is a custom adapter for more then 1 TextView , the adapter is not going to use
         this second argument, so it can be any value. Here, I used 0.
         */
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /** Check if the existing view is being reused, otherwise inflate the view**/
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        /**Get the News object located at this position in the list**/
        final News currentArticle = getItem(position);

        /** Find the TextView in the list_item.xml layout with magnitude**/
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);

        /** Get the version name from the current News object and set this text on the name TextView**/

        // Display the section of the current news article in that TextView
        sectionTextView.setText(currentArticle.getSection());

        /** Find the TextView in the list_item.xml layout with the author of the of the current news article**/
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        authorTextView.setText(currentArticle.getAuthor());

        /** Find the TextView in the list_item.xml layout with the title of the of the current news article**/
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);

        /** Some of the articles have the author's name in the and of the article,
         * so I've decided to remove it**/
        if (currentArticle.getTitle().contains(currentArticle.getAuthor()) && currentArticle.getAuthor()!= "") {
            String[] parts = currentArticle.getTitle().split(currentArticle.getAuthor());

            titleTextView.setText(removeTheLastCharacter(parts[0]));
        } else {
            titleTextView.setText(currentArticle.getTitle());
        }

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText((CharSequence) currentArticle.getmDate());

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText((CharSequence) currentArticle.getTime());

        /** Find the View in the list_item.xml layout with the thumbnail of the of the current news article**/
        ImageView thumbnailImage = (ImageView) listItemView.findViewById(R.id.article_thumbnail);
        thumbnailImage.setImageBitmap(currentArticle.getThumbnail());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * method to remove last 3 characters from the title after split
     **/
    public String removeTheLastCharacter(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 3);
        }
        return str;
    }

}
