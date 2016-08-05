package com.example.android.popularmoviesapp_stage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by anirudha.joshi on 7/29/2016.
 */


public class CustomGridViewAdapter extends ArrayAdapter<MovieModel> {

    private static final String LOG_TAG = CustomGridViewAdapter.class.getSimpleName();

    Context context;
    Movies movies;

    final private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    static class ViewHolder {
        public ImageView image;
        public TextView title;
    }

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context The current context. Used to inflate the layout file.
     * @param movies  A List of Movie objects to display in a list
     */
    // public CustomGridViewAdapter(Activity context, ArrayList<Movie> movies) {
    public CustomGridViewAdapter(Activity context, Movies movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies.getResults());
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        final MovieModel m = getItem(position);

        viewHolder.image.setPadding(0, 0, 0, 0);
        viewHolder.image.setAdjustViewBounds(true);
        String poster_path = IMAGE_BASE_URL + movies.getResults().get(position).getPosterPath();
        Picasso.with(context).load(poster_path).into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putString("movie_id", m.getId());

                Intent detailIntent = new Intent(context, DetailActivity.class);
                detailIntent.putExtras(b);
                context.startActivity(detailIntent);
            }
        });

        return convertView;
    }
}
