package com.nawaf.jobfinder.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nawaf.jobfinder.models.JobModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static WebServices api;

    public static WebServices getClient() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient okClient = new OkHttpClient.
                    Builder().
                    connectTimeout(30, TimeUnit.SECONDS).
                    readTimeout(30, TimeUnit.SECONDS).
                    addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            Request request = original.newBuilder()
                                    .method(original.method(), original.body())
                                    .build();

                            return chain.proceed(request);
                        }
                    }).addInterceptor(logging)
                    .build();

            // register gson deserializer to parse every response in one object
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(JobModel.class,new JobDeserializer())
                    .create();

            Retrofit client = new Retrofit.Builder()
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("https://jobs.search.gov/jobs/")
                    .build();
            api = client.create(WebServices.class);
        }
        return api;
    }


}
