package com.example.android.popularmoviesapp_stage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        // Toast.makeText(DetailActivity.this, b.getString("movie_title"), Toast.LENGTH_SHORT).show();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.moviedetailscontainer, new DetailFragment())
                    // Add this transaction to the back stack
                    .addToBackStack("detail")
                    .commit();
        }
    }
}
