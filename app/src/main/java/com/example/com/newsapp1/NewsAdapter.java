package com.example.com.newsapp1;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {

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

        /** Find the TextView in the list_item.xml layout with the title of the of the current news article**/
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentArticle.getTitle());


        /** Find the TextView in the list_item.xml layout with the author of the of the current news article**/
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        authorTextView.setText(currentArticle.getAuthor());


        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText((CharSequence) currentArticle.getTime());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
