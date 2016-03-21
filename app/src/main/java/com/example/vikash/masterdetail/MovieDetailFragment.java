package com.example.vikash.masterdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vikash.masterdetail.adapter.ReviewAdapter;
import com.example.vikash.masterdetail.adapter.TrailerAdapter;
import com.example.vikash.masterdetail.constants.Constant;
import com.example.vikash.masterdetail.model.ResultModel;
import com.example.vikash.masterdetail.model.ReviewModel;
import com.example.vikash.masterdetail.model.TrailerModel;
import com.example.vikash.masterdetail.singleton.ReFitInst;
import com.example.vikash.masterdetail.singleton.SharedPrefrence;
import com.example.vikash.masterdetail.utils.DateUtil;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements Callback<TrailerModel> {

    @Bind(R.id.movie_detail_name_id)
     TextView mMovieDetailName;

    @Bind(R.id.movie_detail_image_id)
     ImageView mMovieDetailImage;

    @Bind(R.id.movie_detail_year_id)
     TextView mMovieDetailYear;

    @Bind(R.id.movie_detail_rating_id)
     TextView mMovieDetailRating;

    @Bind(R.id.movie_detail_duration_id)
     TextView mMovieDetailDuration;

    @Bind(R.id.movie_detail_make_favorite_id)
     Button mMakeFavBut;

    @Bind(R.id.movie_detail_overview)
    TextView mMovieDetailOverview;

    @Bind(R.id.movie_detail_trailer_list)
    RecyclerView mMovieDetailTrailerList;

    @Bind(R.id.movie_detail_review_list)
    RecyclerView mMovieDetailReviewList;

    private ResultModel resultModel;
    private TrailerAdapter mTrailerAdapter;

    private ReviewAdapter mReviewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(String.valueOf(R.string.data))) {
            Bundle bundle = getArguments();
            resultModel = bundle.getParcelable(String.valueOf(R.string.data));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, null);
        ButterKnife.bind(this, rootView);
        if (resultModel != null) {
            mMovieDetailName.setText(resultModel.getOriginalTitle());
            try {
                mMovieDetailYear.setText(String.valueOf(DateUtil.getYear(resultModel.getReleaseDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            final String voteAverage = String.valueOf(resultModel.getVoteAverage()) + "/10";
            mMovieDetailRating.setText(voteAverage);
            String url = Constant.BASE_URL_IMAGE_DOWNLOAD + "" + resultModel.getBackdropPath();
            Glide.with(getActivity().getApplicationContext())
                    .load(url)
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mMovieDetailImage);
            mMovieDetailOverview.setText(resultModel.getOverview());
            if (resultModel.getFavMovie() != 1) {
                mMakeFavBut.setText("SELECT AS\nFAVORITE");
            } else {
                mMakeFavBut.setText("REMOVE AS\nFAVORITE");
            }
            LinearLayoutManager lLayoutTrailer = new LinearLayoutManager(getContext());
            mMovieDetailTrailerList = (RecyclerView) rootView.findViewById(R.id.movie_detail_trailer_list);
            mMovieDetailTrailerList.setLayoutManager(lLayoutTrailer);
            LinearLayoutManager lLayoutReview = new LinearLayoutManager(getContext());
            mMovieDetailReviewList = (RecyclerView) rootView.findViewById(R.id.movie_detail_review_list);
            mMovieDetailReviewList.setLayoutManager(lLayoutReview);
            getTrailerDetail();
            getReviewDetail();

        }
        return rootView;
    }

    private void getReviewDetail() {

        Call<ReviewModel> call = ReFitInst.getInstance().getMethod().getReviews(resultModel.getId(), Constant.API_KEY);
        //asynchronous call
        call.enqueue(reviewResponseListener);
    }

    private void getTrailerDetail() {

        Call<TrailerModel> call = ReFitInst.getInstance().getMethod().getTrailer(resultModel.getId(), Constant.API_KEY);
        //asynchronous call
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<TrailerModel> call, Response<TrailerModel> response) {

        if (response.code() != 404 && response.body() != null) {
            TrailerModel mTrailerModel = response.body();
            if (mTrailerAdapter == null) {
                mTrailerAdapter = new TrailerAdapter(getActivity().getApplicationContext(), mTrailerModel);
            } else {
                mTrailerAdapter.setmTrailerModel(mTrailerModel);
                mTrailerAdapter.notifyDataSetChanged();
            }
            mMovieDetailTrailerList.setAdapter(mTrailerAdapter);

        }

    }
    Callback<ReviewModel> reviewResponseListener=new Callback<ReviewModel>(){

        @Override
        public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {

            if (response.code() != 404 && response.body() != null) {
                ReviewModel mReviewModel = response.body();
                if (mReviewAdapter == null) {
                    mReviewAdapter = new ReviewAdapter(getActivity().getApplicationContext(), mReviewModel);
                    mMovieDetailReviewList.setAdapter(mReviewAdapter);
                } else {
                    mReviewAdapter.setmTrailerModel(mReviewModel);
                    mReviewAdapter.notifyDataSetChanged();
                }

            }
        }

        @Override
        public void onFailure(Call<ReviewModel> call, Throwable t) {

        }
    };
    @Override
    public void onFailure(Call<TrailerModel> call, Throwable t) {
        Log.i("TAG", "onFailure: ");

    }

    @OnClick(R.id.movie_detail_make_favorite_id)
    public void makeFab() {
        if (resultModel.getFavMovie() != 1) {
            resultModel.setFavMovie(1);
            SharedPrefrence.getInstance(getContext()).put(Constant.Movie.KEY_FAV_MOVIE,resultModel.getId());
            mMakeFavBut.setText("REMOVE AS\nFAVORITE");
        } else {
            SharedPrefrence.getInstance(getContext()).delete(Constant.Movie.KEY_FAV_MOVIE,resultModel.getId());
            resultModel.setFavMovie(0);
            mMakeFavBut.setText("SELECT AS\nFAVORITE");
        }

    }

}
