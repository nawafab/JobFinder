package com.nawaf.jobfinder.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nawaf.jobfinder.R;
import com.nawaf.jobfinder.common.OnRecyclerItemClickLister;
import com.nawaf.jobfinder.models.JobModel;

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // item types constants
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    ArrayList<JobModel> jobModels = new ArrayList<>();

    // to check if the load more item is added
    private boolean isLoadingAdded = false;

    // call back for the item click of the recyclerView
    private OnRecyclerItemClickLister<String> onRecyclerItemClickLister;

    // load more related variables
    private int visibleThreshold = 2;
    private int lastVisibleItem;
    private int totalItemCount;
    private int startPage = 1 ;
    private int page = startPage;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    JobAdapter(RecyclerView recyclerView){

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition();
                        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            // End has been reached
                            // Do something
                            if (onLoadMoreListener != null) {
                                page++;

                                onLoadMoreListener.onLoadMore(page);

                            }
                            loading = true;
                        }
                    }

                }
            });
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {

        if (itemType == LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_loading, viewGroup, false));

        } else {
            return new JobViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_job, viewGroup, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof JobViewHolder) {
            ((JobViewHolder) viewHolder).bind(jobModels.get(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return (position == jobModels.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return jobModels.size();
    }


    /**
     * Add lister to the recycler item clickes
     *
     * @param onRecyclerItemClickLister
     */
    public void setOnRecyclerItemClickLister(OnRecyclerItemClickLister<String> onRecyclerItemClickLister) {
        this.onRecyclerItemClickLister = onRecyclerItemClickLister;
    }


    /**
     * This method is to add list of jobs for the end of the current list if exists
     *
     * @param jobModels the new list to be added to the list
     */
    public void addItems(ArrayList<JobModel> jobModels) {

        if (isLoadingAdded) {
            removeLoadingFooter();
        }

        int lastPosition = this.jobModels.size() - 1;

        this.jobModels.addAll(jobModels);

        if (lastPosition >= 0)
            notifyItemInserted(lastPosition);
        else
            notifyItemInserted(0);

    }

    /**
     * Add call back for the load more on recyclerView scroll
     *
     * @param loadMoreListener call the will run
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener){
        this.onLoadMoreListener = loadMoreListener;
    }

    /**
     * Add single object to the end of the list
     *
     * @param jobModel
     */
    public void addItem(JobModel jobModel) {
        jobModels.add(jobModel);
        notifyItemInserted(jobModels.size() - 1);
    }

    /**
     * Get object from position
     *
     * @param position position of the requested object
     * @return JobModel object from position passed
     */
    public JobModel getItem(int position) {
        return jobModels.get(position);
    }

    /**
     * This method used to clear the current list and add new list you pass
     *
     * @param jobModels the new list to be updated
     */
    public void updateItems(ArrayList<JobModel> jobModels) {
        // male sure to reset the list
        page = startPage;

        this.jobModels.clear();
        this.jobModels.addAll(jobModels);
        notifyDataSetChanged();

    }

    /**
     * add loading progress object to the end of the list
     */
    public void addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true;
            addItem(new JobModel());
        }
    }

    /**
     * remove loading progress object to the end of the list
     */
    public void removeLoadingFooter() {

        if (isLoadingAdded) {
            isLoadingAdded = false;

            int position = jobModels.size() - 1;
            JobModel result = getItem(position);

            if (result != null) {
                jobModels.remove(position);
                notifyItemRemoved(position);
            }
        }

    }

    public void clear() {
        page= startPage;
        isLoadingAdded = false;
        jobModels.clear();
        notifyDataSetChanged();
    }


    /**
     * Job view holder for the recycler view
     */
    class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SimpleDraweeView sdvCompanyLogo;
        private TextView tvJobTitle;
        private TextView tvCompanyName;
        private TextView tvLocation;
        private TextView tvPostDate;


        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            sdvCompanyLogo = itemView.findViewById(R.id.sdvCompanyLogo);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPostDate = itemView.findViewById(R.id.tvPostDate);

            itemView.setOnClickListener(this);
        }

        void bind(JobModel jobModel) {

            if (jobModel != null) {

                sdvCompanyLogo.setImageURI(jobModel.getCompanyLogo());

                tvJobTitle.setText(jobModel.getTitle());

                tvCompanyName.setText(jobModel.getCompanyName());

                tvLocation.setText(jobModel.getLocation());

                tvPostDate.setText(jobModel.getCreatedAt());

            }

        }

        @Override
        public void onClick(View v) {
            if (onRecyclerItemClickLister != null) {
                onRecyclerItemClickLister.onItemClicked(jobModels.get(getAdapterPosition()).getUrl());
            }
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface OnLoadMoreListener{

        void onLoadMore(int page);

    }

}
