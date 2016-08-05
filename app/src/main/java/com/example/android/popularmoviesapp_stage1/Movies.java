package com.example.android.popularmoviesapp_stage1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anirudha.joshi on 8/2/2016.
 */
public class Movies {
    List<MovieModel> results;
    int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieModel> getResults() {
        return results;
    }

    public void setResults(MovieModel[] results) {
        this.results = new ArrayList<MovieModel>(Arrays.asList(results));

    }

}