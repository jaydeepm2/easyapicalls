package com.jaydeepm2.easyapicall;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.jaydeepm2.easyapicall.RequestTypes.POST;

public class AuthApiCall implements RequestApp {
    private static Context context;
    private String url;
    private int methodType;
    private Map<String, String> params;
    private Map<String, String> headers;

    private String StatusKeyName, success_value;
    private boolean showProgressDialog;
    private String progressMessage;


    public static AuthApiCall init(Context context){
        AuthApiCall.context = context;
        return new AuthApiCall(context);
    }

    public AuthApiCall(Context context) {
        this.url = null;
        this.methodType = RequestTypes.GET;
        this.params = null;
        this.headers = null;
        this.StatusKeyName = null;
        this.success_value = null;
        this.showProgressDialog = false;
        this.progressMessage = "";
    }

    @Override
    public RequestApp setStatusHandler(String StatusKeyName, String success_value) {
        this.StatusKeyName = StatusKeyName;
        this.success_value = success_value;
        return this;
    }

    @Override
    public RequestApp showProgressDialog(boolean showProgressDialog) {
        this.showProgressDialog = showProgressDialog;
        return this;
    }

    @Override
    public RequestApp setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
        return this;
    }


    @Override
    public RequestApp setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public RequestApp setType(int type) {
        this.methodType = type;
        return this;
    }

    @Override
    public RequestApp setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public RequestApp setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }


    @Override
    public void makeRequest(final NetworkRequest.GetResponse onCallBack) {

        NetworkRequest.Request(this.context, this.showProgressDialog, this.progressMessage, this.url, this.params, this.headers, POST, this.StatusKeyName, this.success_value, new NetworkRequest.GetResponse() {
            @Override
            public void onSuccess(String status_code, JSONObject result) throws JSONException {

                onCallBack.onSuccess(status_code, result);
            }

            @Override
            public void onFail(String error_code, String error_body) {

                onCallBack.onFail(error_code, error_body);

            }
        });
    }
}
