package com.nawaf.jobfinder.retrofit;

import com.nawaf.jobfinder.models.FilterModel;
import com.nawaf.jobfinder.models.JobModel;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Handling the multiple provider call to return one provider call in one place
 */
public final class DataSource {


    public static Call<ArrayList<JobModel>> getJobsByProvider(FilterModel filterModel, int page) throws Exception {

        switch (filterModel.getProviders()) {
            case GITHUB:
                return getGitHubJobs(filterModel.getTitle(), filterModel.getLocation(), page);
            case SEARCH_GOV:
                return getSearchGovJobs(filterModel.getTitle(), filterModel.getLocation(),page);
            default:
                throw new Exception("provider not found Exception you should add provider for this api");
        }
    }

    private static Call<ArrayList<JobModel>> getGitHubJobs(String title, String location, int page) {

        return RetrofitClient.getClient().getGitHubJobList(title, location, page);

    }

    private static Call<ArrayList<JobModel>> getSearchGovJobs(String title, String location, int page) {

        int size = 20;
        int from = (page - 1) * size;
        String query;

        if (location == null || location.isEmpty()) {
            query = String.format("%s+jobs", title);
        } else {
            query = String.format("%s+jobs+in%s", title, location);
        }

        return RetrofitClient.getClient().getSearchGovJobList(query, size, from);

    }


}
