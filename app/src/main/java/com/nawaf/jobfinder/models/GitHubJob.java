package com.nawaf.jobfinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nawaf.jobfinder.common.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GitHubJob extends JobModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("company")
    @Expose
    private String companyName;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("company_logo")
    @Expose
    private String companyLogo;


    @Override
    public String getCompanyLogo() {
        return companyLogo;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getTitle() {
        return title;
    }


    @Override
    public String getCreatedAt() {
        //Wed Feb 27 22:18:38 UTC 2019
        DateFormat originalFormat = new SimpleDateFormat(AppConstants.DateFormats.GITHUB, Locale.US);

        DateFormat jobDateFormat = new SimpleDateFormat(AppConstants.DateFormats.DEFAULT, Locale.US);

        try {
            createdAt = createdAt.replace(" UTC","");

            Date date = originalFormat.parse(createdAt);
            return jobDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return createdAt;
        }

    }


    @Override
    public String getUrl() {
        return url;
    }
}
