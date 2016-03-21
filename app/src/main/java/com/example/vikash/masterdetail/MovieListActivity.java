package com.example.vikash.masterdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vikash.masterdetail.adapter.MovieAdapter;
import com.example.vikash.masterdetail.constants.Constant;
import com.example.vikash.masterdetail.model.DataModel;
import com.example.vikash.masterdetail.model.ResultModel;
import com.example.vikash.masterdetail.singleton.ReFitInst;
import com.example.vikash.masterdetail.singleton.SharedPrefrence;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements Callback<DataModel> {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Call<DataModel> call;
    private DataModel mDataModel;
    private MovieAdapter mMovieAdapter;


    @Bind(R.id.movie_list)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        assert recyclerView != null;
        setupRecyclerViewLayout(recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        getMovie();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mDataModel != null) {
            updateLocalFavMovie();
            setMovieAdapter();
        }
    }

    private void setupRecyclerViewLayout(@NonNull RecyclerView recyclerView) {
        GridLayoutManager lLayout;
        if (mTwoPane) {
            lLayout = new GridLayoutManager(MovieListActivity.this, 3);
        } else {
            lLayout = new GridLayoutManager(MovieListActivity.this, 2);
        }
        recyclerView.setLayoutManager(lLayout);

    }


    private void getMovie() {
        call = ReFitInst.getInstance().getMethod().getMovieList(Constant.Movie.POPULARITY_DESC, Constant.API_KEY);
        //asynchronous call
        call.enqueue(this);
    }

    private void getPopularMovie() {
        call = ReFitInst.getInstance().getMethod().getPopularMovieList(Constant.Movie.POPULARITY_DESC, Constant.API_KEY);
        //asynchronous call
        call.enqueue(this);
    }

    private void getTopRattedMovie() {
        call = ReFitInst.getInstance().getMethod().getMovieList(Constant.Movie.VOTE_COUNT_DESC, Constant.API_KEY);
        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.code() != 404 && response.body() != null) {
            mDataModel = (DataModel) response.body();
            updateLocalFavMovie();
            setMovieAdapter();
            initializeDetailFrag();


        }
    }

    private void setMovieAdapter() {
        if (mMovieAdapter == null) {
            mMovieAdapter = new MovieAdapter(this, mDataModel, mTwoPane);
            recyclerView.setAdapter(mMovieAdapter);
        } else {
            mMovieAdapter.setmDataModel(mDataModel);
            mMovieAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailure(Call<DataModel> call, Throwable t) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_most_popular) {
            getPopularMovie();
//            sortBaseOnPopularity();
            return true;
        } else if (id == R.id.action_heighest_rated) {
            getTopRattedMovie();
//            sortBaseOnRatting();
            return true;
        } else if (id == R.id.action_favorite) {
            sortBaseOnFavorite();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeDetailFrag() {

        Bundle mBundle = new Bundle();
        if (mDataModel.getResults() != null && mDataModel.getResults().size() > 0) {
            mBundle.putParcelable(String.valueOf(R.string.data), mDataModel.getResults().get(0));

            if (mTwoPane) {
                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(mBundle);

                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment)
                        .commit();


            }
        }
    }

    private void updateLocalFavMovie() {

        List<String> fabMovieList = SharedPrefrence.getInstance(getApplicationContext()).getFavMovieList(Constant.Movie.KEY_FAV_MOVIE);
        if (mDataModel != null && fabMovieList != null) {
            for (ResultModel resultModel : mDataModel.getResults()) {
                for (int i = 0; i < fabMovieList.size(); i++) {
                    if (fabMovieList.get(i).equalsIgnoreCase(String.valueOf(resultModel.getId()))) {
                        resultModel.setFavMovie(1);
                    } else {
                        resultModel.setFavMovie(0);
                    }
                }
            }
        }

    }


    private void sortBaseOnFavorite() {
        Collections.sort(mDataModel.getResults(), MovieAdapter.favoriteComparator);
        if (mMovieAdapter != null) {
            mMovieAdapter.notifyDataSetChanged();
        }
    }
}
