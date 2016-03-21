package com.example.vikash.masterdetail.singleton;

import com.example.vikash.masterdetail.loggers.Logger;
import com.example.vikash.masterdetail.api.Api;
import com.example.vikash.masterdetail.constants.Constant;

import retrofit2.converter.gson.GsonConverterFactory;


public class ReFitInst {
    private static final ReFitInst ourInstance = new ReFitInst();
    private final Api methods;

    public static ReFitInst getInstance() {
        return ourInstance;
    }

    private ReFitInst() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(Logger.getInstance().getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        methods = retrofit.create(Api.class);
    }

    public Api getMethod() {
        return methods;
    }


}