package com.example.android.popularmoviesapp_stage1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private String LOG_TAG;

    final private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.poster)
    ImageView poster;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.runtime)
    TextView runtime;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.synopsis)
    TextView synopsis;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        LOG_TAG = getActivity().getClass().getSimpleName();

        String moviedetail_title = getActivity().getResources().
                getString(R.string.moviedetail_title);
        getActivity().setTitle(moviedetail_title);

        Bundle b = intent.getExtras();
        String movie_id = b.getString("movie_id");

        MovieDBAPI movieDBAPIService = MovieDBAPI.retrofit.create(MovieDBAPI.class);

        // Get individual movie details
        final Call<MovieModel> call_getMovie =
                movieDBAPIService.getMovie(movie_id, BuildConfig.MyMovieDBApiKey);

        call_getMovie.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call_getMovie, Response<MovieModel> response) {
                ButterKnife.setDebug(true);
                if (response.body() != null) {
                    MovieModel movie = response.body();

                    Log.d("DetailFragment", movie.toString());

                    // Set the title
                    titleView.setText(movie.getTitle());

                    // Set the poster
                    Picasso.with(getActivity()).load((IMAGE_BASE_URL +
                            movie.getPosterPath())).into(poster);

                    // Set release year
                    year.setText(movie.getReleaseYear());

                    // Set movie runtime
                    String mins = getActivity().getResources().getString(R.string.minutes);
                    runtime.setText(Integer.toString(movie.getRuntime()) + " " + mins);

                    // Set movie rating
                    String maxrating = getActivity().getResources().getString(R.string.maxratings);
                    rating.setText(movie.getVoteAvarage() + "/" + maxrating);

                   // Set the movie synopsis
                   synopsis.setText(movie.getOverview());
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call_getMovie, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
                String error = getResources().getString(R.string.error);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}
