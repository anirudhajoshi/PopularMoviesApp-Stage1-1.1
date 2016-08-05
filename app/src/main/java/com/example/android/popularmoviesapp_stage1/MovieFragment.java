package com.example.android.popularmoviesapp_stage1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    @BindView(R.id.moviegrid)
    GridView gridView;

    @BindView(R.id.emptyview)
    TextView emptyview;

    CustomGridViewAdapter movieAdapter;
    private String LOG_TAG;
    private MovieDBAPI mMovieDBAPIService;

    private MovieModel[] movieList = null;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Report that this fragment would like to participate in populating the options menu
        // by receiving a call to onCreateOptionsMenu(Menu, MenuInflater) and related methods.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, rootView);
        LOG_TAG = getActivity().getClass().getSimpleName();
        if (savedInstanceState != null) {
            movieList = (MovieModel[]) savedInstanceState.getParcelableArray("movielist");
        }

        if (movieList == null) {
            updateMovies();
        } else {
            Movies movies = new Movies();
            movies.setResults(movieList);
            movieAdapter = new CustomGridViewAdapter(getActivity(), movies);
            movieAdapter.addAll(movieList);
            movieAdapter.notifyDataSetChanged();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "Save state here...");
        outState.putParcelableArray("movielist", movieList);
    }

    // Retrieve the movies from the movieDB site using API
    private void updateMovies() {

        // Get the users preferred sort order (default is "popular")
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sortorder_key),
                getString(R.string.pref_sortorder_default));

        // Initialize the movie service
        mMovieDBAPIService = MovieDBAPI.retrofit.create(MovieDBAPI.class);

        getMovies(sortOrder);
    }

    // Get movies based on users preferred sorting order - default is popular
    private void getMovies(final String sortOrder) {
        final Call<Movies> call_getMovies =
                mMovieDBAPIService.getMovies(sortOrder, BuildConfig.MyMovieDBApiKey);

        call_getMovies.enqueue(new Callback<Movies>() {

            @Override
            public void onResponse(Call<Movies> call_getMovies,
                                   Response<Movies> response) {
                try {
                    if (response.body() != null) {

                        String title;

                        if (sortOrder.equals("popular"))
                            title = getActivity().getResources().
                                    getString(R.string.title_popular);
                        else
                            title = getActivity().getResources().
                                    getString(R.string.title_toprated);

                        getActivity().setTitle(title);

                        Log.d(LOG_TAG, response.body().toString());
                        Movies movies = response.body();

                        movieList = new MovieModel[movies.getResults().size()];
                        movieList = movies.getResults().toArray(movieList);

                        movieAdapter = new CustomGridViewAdapter(getActivity(), movies);
                        if (movies != null && movies.getResults().size() > 0) {
                            gridView.setAdapter(movieAdapter);
                            movieAdapter.notifyDataSetChanged();
                        }
                    } else
                        Log.d(LOG_TAG, "response.body() is null");
                } catch (Exception e) {
                    Log.d(LOG_TAG, e.toString());
                }
            }

            @Override
            public void onFailure(Call<Movies> call_getMovies, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
                String error = getResources().getString(R.string.error);
                gridView.setEmptyView(emptyview);
            }
        });
    }
}
