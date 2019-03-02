package com.nawaf.jobfinder.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nawaf.jobfinder.models.GitHubJob;
import com.nawaf.jobfinder.models.JobModel;
import com.nawaf.jobfinder.models.SearchGovJob;

import java.lang.reflect.Type;

/**
 * deserializer class to parse the job class based parameter types
 */
public class JobDeserializer implements JsonDeserializer<JobModel> {


    @Override
    public JobModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.has("location") && jsonObject.get("location").getAsJsonPrimitive().isString()) {
                return context.deserialize(json, GitHubJob.class);
            }else if (jsonObject.has("locations") && jsonObject.get("locations").isJsonArray()){
                return context.deserialize(json, SearchGovJob.class);
            }else{
                return null;
            }

    }
}
