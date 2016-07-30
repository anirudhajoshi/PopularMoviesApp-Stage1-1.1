package com.example.android.popularmoviesapp_stage1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anirudha.joshi on 7/29/2016.
 */


public class CustomGridViewAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = CustomGridViewAdapter.class.getSimpleName();

    Context context;
    ArrayList<Movie> movies;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movies         A List of Movie objects to display in a list
     */
    public CustomGridViewAdapter(Activity context, ArrayList<Movie> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
/*
        ImageView imageView;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item,
                    parent,
                    false);
        }

        imageView = (ImageView) convertView.findViewById(R.id.item_image);
        String poster = (movies.get(position)).getPoster();
        Picasso.with(context).load(poster).into(imageView );
        return convertView;
*/
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie m = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item,
                    parent,
                    false);
        }

        ImageView movie_poster = (ImageView) convertView.findViewById(R.id.item_image);
        movie_poster.setPadding(0, 0, 0, 0);
        movie_poster.setAdjustViewBounds(true);

        // Use Picasoo to load the image into the ImageView
        Picasso.with(context).load((movies.get(position)).getPoster()).into(movie_poster);

        return convertView;
    }
}
