package com.nawaf.jobfinder.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.nawaf.jobfinder.R;
import com.nawaf.jobfinder.common.OnRecyclerItemClickLister;
import com.nawaf.jobfinder.common.Utils;
import com.nawaf.jobfinder.models.FilterModel;
import com.nawaf.jobfinder.models.JobModel;
import com.nawaf.jobfinder.models.Providers;
import com.nawaf.jobfinder.retrofit.DataSource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity contains the list of the jobs retried
 */
public class MainActivity extends AppCompatActivity implements SearchFragment.OnSearchListener, OnRecyclerItemClickLister<String>, JobAdapter.OnLoadMoreListener {

    // views
    private TextView tvSearch;
    private TextView tvMessage;
    private JobAdapter jobAdapter;


    // objects
    FilterModel filterModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();

        initFilter();

        initViews();

        requestDataFromApi(filterModel, 1);

//        jobAdapter.addLoadingFooter();
    }

    private void initViews() {
        tvSearch = findViewById(R.id.tvSearchValues);
        tvMessage = findViewById(R.id.tvMessage);

        tvSearch.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(filterModel))
                    .addToBackStack(SearchFragment.class.getName())
                    .commit();

        });

    }


    private void initFilter() {
        filterModel = new FilterModel();

        filterModel.setProviders(Providers.GITHUB);
    }

    /**
     * Initialize the recycler layoutManager ,adapter and the scroll listener.
     */
    private void initRecycler() {
        RecyclerView rvJobs = findViewById(R.id.rvJobs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvJobs.setLayoutManager(linearLayoutManager);

        jobAdapter = new JobAdapter(rvJobs);
        jobAdapter.setOnRecyclerItemClickLister(this);

        jobAdapter.setOnLoadMoreListener(this);

        rvJobs.setAdapter(jobAdapter);

    }


    /**
     * Method to request job list from apis based on the parameters passed
     *
     * @param filterModel object with filter model
     * @param page        page number needed
     */
    private void requestDataFromApi(FilterModel filterModel, int page) {

        if (!Utils.isNetworkAvailable(this)) {
            tvMessage.setText(R.string.error_message_internet);
        }
        jobAdapter.addLoadingFooter();

        try {
            DataSource.getJobsByProvider(filterModel, page).enqueue(new Callback<ArrayList<JobModel>>() {
                @Override
                public void onResponse(Call<ArrayList<JobModel>> call, Response<ArrayList<JobModel>> response) {

                    jobAdapter.removeLoadingFooter();
                    tvMessage.setText("");

                    if (response.body() != null) {

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (page == 1) {
                                    if (response.body().isEmpty()) {
                                        jobAdapter.clear();
                                        tvMessage.setText(R.string.empty_job_list);
                                    } else {
                                        jobAdapter.addItems(response.body());

                                    }
                                } else {
                                    jobAdapter.setLoaded();
                                    if (response.body().isEmpty()) {
                                    } else {
                                        jobAdapter.addItems(response.body());
                                    }
                                }
                            }

                        });

                    }

                }

                @Override
                public void onFailure(Call<ArrayList<JobModel>> call, Throwable t) {
//                    jobAdapter.removeLoadingFooter();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
//            jobAdapter.removeLoadingFooter();

        }


    }


    @Override
    public void onSearchClicked(FilterModel filterModel) {

        String hint = String.format("%s / %s / %s", filterModel.getProviders().getName(), filterModel.getTitle(), filterModel.getLocation());
        tvSearch.setText(hint);

        jobAdapter.clear();

        requestDataFromApi(filterModel, 1);

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onItemClicked(String url) {
        if (url != null) {
            Intent browse = new Intent(Intent.ACTION_VIEW);
            browse.setData(Uri.parse(url));
            startActivity(browse);
        }

    }

    @Override
    public void onLoadMore(int page) {
        requestDataFromApi(filterModel, page);
    }
}
