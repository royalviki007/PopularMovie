package com.example.vikash.masterdetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vikash.masterdetail.R;
import com.example.vikash.masterdetail.model.ReviewModel;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder> {

    private final Context mContext;
    private ReviewModel mReviewModel;

    public ReviewAdapter(Context context, ReviewModel trailerModel) {
        this.mContext = context;
        mReviewModel = trailerModel;
    }

    @Override
    public ReviewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_revie_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.mTextView.setText(mReviewModel.getResults().get(position).getContent());
    }


    @Override
    public int getItemCount() {
        return (mReviewModel.getResults() != null ? mReviewModel.getResults().size() : 0);
    }

    public void setmTrailerModel(ReviewModel mReviewModel) {
        this.mReviewModel = mReviewModel;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;


        public CustomViewHolder(View itemView) {
            super(itemView);
            this.mTextView = (TextView) itemView.findViewById(R.id.review_detail_id);

        }
    }

}
