package com.example.android.popularmoviesapp_stage1;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.net.HttpURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {


    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {

        String sortOrder = "ratings";   // or ratings
        FetchMoviesTask moviestask = new FetchMoviesTask();
        moviestask.execute(sortOrder);
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        @Override
        protected Void doInBackground(String... params) {

            try {
                String APPID_PARAM= "api_key";

                String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
                if(params[0].equals("popular"))
                    MOVIE_BASE_URL += "popular";
                else if(params[0].equals("ratings"))
                    MOVIE_BASE_URL += "top_rated";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MyMovieDBApiKey)
                        .build();

                Log.d(LOG_TAG, builtUri.toString());

//                URL url = new URL(builtUri.toString());
//
//                // Log.v(LOG_TAG, "Built URI " + builtUri.toString());
//
//                // Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    forecastJsonStr = null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//                    forecastJsonStr = null;
//                }
//
//                forecastJsonStr = buffer.toString();
//
//                // Log.v(LOG_TAG,"Forecast JSON string: " + forecastJsonStr);
//            }
            } catch (Exception e) {

            }

            return null;
        }
    }
}
