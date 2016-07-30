package com.example.android.popularmoviesapp_stage1;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private ArrayList<Movie> mMovies;
    private GridView gridView;
    CustomGridViewAdapter movieAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        gridView = (GridView) rootView.findViewById(R.id.moviegrid);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {

        // String sortOrder = "ratings";
        String sortOrder = "popular";
        FetchMoviesTask moviestask = new FetchMoviesTask();
        moviestask.execute(sortOrder);
    }


    private class FetchMoviesTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        @Override
        protected Void doInBackground(String... params) {

            try {
                String APPID_PARAM = "api_key";

                String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
                if (params[0].equals("popular"))
                    MOVIE_BASE_URL += "popular";
                else if (params[0].equals("ratings"))
                    MOVIE_BASE_URL += "top_rated";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MyMovieDBApiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to movieDB and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    moviesJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    moviesJsonStr = null;
                }

                moviesJsonStr = buffer.toString();

                getMovieDataFromJson(moviesJsonStr);

                // return ((long)mMovies.size());

            } catch (Exception e) {
                Log.d(LOG_TAG, e.toString());
                moviesJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        private void getMovieDataFromJson(String moviesJsonStr)
                throws JSONException {
            // Parse the JSON string
            JSONObject moviesJson = new JSONObject(moviesJsonStr);

            // Make sure we have results
            String page = moviesJson.getString("page");
            if (Integer.parseInt(page) >= 1) {
                mMovies = new ArrayList<Movie>();

                JSONArray moviesArray = moviesJson.getJSONArray("results");
                for (int i = 0; i < moviesArray.length(); i++) {
                    Movie m = new Movie();

                    JSONObject movieJSONObj = moviesArray.getJSONObject(i);

                    // Get movie id
                    String movie_id = movieJSONObj.getString("id");
                    m.setId(movie_id);

                    // Set movie title
                    String movie_title = movieJSONObj.getString("title");
                    m.setTitle(movie_title);

                    // Get synopsis
                    String movie_synopsis = movieJSONObj.getString("overview");
                    m.setDescription(movie_synopsis);

                    // Get movie poster path
                    String movie_poster = movieJSONObj.getString("poster_path");

                    // Set the base poster path
                    String poster_path_url = "http://image.tmdb.org/t/p/w185";
                    poster_path_url += movie_poster;
                    m.setPoster(poster_path_url);

                    // Get the release date
                    String movie_releasedate = movieJSONObj.getString("release_date");
                    m.setReleasedate(movie_releasedate);

                    mMovies.add(m);

                    Log.d(LOG_TAG, m.toString());
                }
            } else {
                Log.d(LOG_TAG, "Page value is not 1 or greater. No results returned</string>");
            }
        }

        @Override
        protected void onPostExecute(Void v) {

            movieAdapter = new CustomGridViewAdapter(getActivity(), mMovies);

            // Get a reference to the ListView, and attach this adapter to it.
            if( mMovies!=null && mMovies.size() > 0) {
                gridView.setAdapter(movieAdapter);
                //movieAdapter.notifyDataSetChanged();
            }
        }
    }
}
