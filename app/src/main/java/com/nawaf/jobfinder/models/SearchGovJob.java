package com.nawaf.jobfinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nawaf.jobfinder.common.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchGovJob extends JobModel {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("position_title")
    @Expose
    private String positionTitle;
    @SerializedName("organization_name")
    @Expose
    private String organizationName;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("locations")
    @Expose
    private List<String> locations = null;
    @SerializedName("url")
    @Expose
    private String url;


    @Override
    public String getCompanyLogo() {
        return "";
    }


    @Override
    public String getCompanyName() {
        return organizationName;
    }

    @Override
    public String getCreatedAt() {

        String availableDate = startDate != null ? startDate : endDate;
        DateFormat originalFormat = new SimpleDateFormat(AppConstants.DateFormats.SEARCH_GOV, Locale.US);

        DateFormat jobDateFormat = new SimpleDateFormat(AppConstants.DateFormats.DEFAULT, Locale.US);

        try {

            Date date = originalFormat.parse(availableDate);

            return jobDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return availableDate;
        }

    }

    @Override
    public String getLocation() {
        if (locations != null) {
            if (locations.size() > 0) {
                return locations.get(0);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public String getTitle() {
        return positionTitle;
    }
}

