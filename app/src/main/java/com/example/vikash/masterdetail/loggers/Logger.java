package com.example.vikash.masterdetail.loggers;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class Logger {
    private static final Logger ourInstance = new Logger();
    private final OkHttpClient client;

    public static Logger getInstance() {
        return ourInstance;
    }

    private Logger() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    }

    public OkHttpClient getClient() {
        return client;
    }
}
