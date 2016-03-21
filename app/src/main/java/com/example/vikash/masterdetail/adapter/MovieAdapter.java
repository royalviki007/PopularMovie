package com.example.vikash.masterdetail.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.vikash.masterdetail.MovieDetailActivity;
import com.example.vikash.masterdetail.MovieDetailFragment;
import com.example.vikash.masterdetail.R;
import com.example.vikash.masterdetail.constants.Constant;
import com.example.vikash.masterdetail.model.DataModel;
import com.example.vikash.masterdetail.model.ResultModel;

import java.util.Comparator;

import static android.view.LayoutInflater.from;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CustomViewHolder> {
    private DataModel mDataModel;
    private final Context mContext;
    private final Activity mActivity;
    private final boolean mTwoPane;

    public MovieAdapter(Context paramContext, DataModel paramDataModel,boolean paramTwoPane) {
        this.mDataModel = paramDataModel;
        this.mContext = paramContext;
        this.mActivity=(Activity)paramContext;
        this.mTwoPane=paramTwoPane;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = from(viewGroup.getContext()).inflate(R.layout.movie_list_content, null);
        if (mTwoPane) {
            view.setLayoutParams(new LinearLayout.LayoutParams(viewGroup.getMeasuredWidth() / 3, viewGroup.getMeasuredHeight() / 3));
        }else {
            view.setLayoutParams(new LinearLayout.LayoutParams(viewGroup.getMeasuredWidth() / 2, viewGroup.getMeasuredHeight() / 2));
        }

        return new CustomViewHolder(view);
    }

    public void setmDataModel(DataModel mDataModel) {
        this.mDataModel = mDataModel;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

        String url = Constant.BASE_URL_IMAGE_DOWNLOAD + "" + mDataModel.getResults().get(i).getBackdropPath();

        //Download image using picasso library
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(customViewHolder.imageView);



    }

    @Override
    public int getItemCount() {
        return (mDataModel.getResults() != null ? mDataModel.getResults().size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.movie_image_id);
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            Bundle mBundle = new Bundle();
            if (mDataModel.getResults() != null) {
                mBundle.putParcelable(String.valueOf(R.string.data), mDataModel.getResults().get(getAdapterPosition()));
            }
            if (mTwoPane) {
                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(mBundle);

                if (mActivity instanceof FragmentActivity) {
                      ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                }
            } else {
                Intent intent = new Intent(mContext,  MovieDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
        }
    }

    public static final Comparator<ResultModel> rattingComparator = new Comparator<ResultModel>() {

        @Override
        public int compare(ResultModel o1, ResultModel o2) {
            return o1.getVoteCount() - o2.getVoteCount();  // This will work because age is positive integer
        }

    };

    public static final Comparator<ResultModel> favoriteComparator = new Comparator<ResultModel>() {

        @Override
        public int compare(ResultModel o1, ResultModel o2) {
            return o2.getFavMovie() - o1.getFavMovie();  // This will work because age is positive integer
        }

    };

    public static final Comparator<ResultModel> popularityComparator = new Comparator<ResultModel>() {

        @Override
        public int compare(ResultModel o1, ResultModel o2) {
            return (o1.getPopularity() - o2.getPopularity() > 0.0 ? 1 : (o1.getPopularity() - o2.getPopularity() < 0 ? -1 : 0));
        }

    };



}