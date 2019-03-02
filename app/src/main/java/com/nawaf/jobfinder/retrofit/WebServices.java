package com.nawaf.jobfinder.retrofit;

import com.nawaf.jobfinder.models.JobModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebServices {


    @GET("https://jobs.github.com/positions.json")
    Call<ArrayList<JobModel>> getGitHubJobList(
            @Query("description") String description,
            @Query("location") String location,
            @Query("page") int page
    );


    @GET("https://jobs.search.gov/jobs/search.json")
    Call<ArrayList<JobModel>> getSearchGovJobList(@Query("query") String description,
                                                  @Query("size") int size,
                                                  @Query("from") int from

    );


}
