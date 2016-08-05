package com.example.android.popularmoviesapp_stage1;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by anirudha.joshi on 8/2/2016.
 */
public interface MovieDBAPI {

    @GET("{id}")
    Call<MovieModel> getMovie(
            @Path("id") String id,
            @Query("api_key") String api_key);

    @GET("{sortOrder}")
    Call<Movies> getMovies(
            @Path("sortOrder") String sortOption,
            @Query("api_key") String api_key);

    @GET("{popular}")
    Call<Movies> getPopularMovies(
            @Path("popular") String sortOption,
            @Query("api_key") String api_key);

    @GET("{top_rated}")
    Call<Movies> getTopRatedMovies(
            @Path("top_rated") String sortOption,
            @Query("api_key") String api_key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
