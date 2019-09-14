package com.jaydeepm2.easyapicall;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class EasyApiCalls implements RequestApp{

    private static Context context;
    private String url;
    private int methodType;
    private Map<String, String> params;
    private Map<String, String> headers;

    private String StatusKeyName;
    private Map<String, String> status_codes;
    private boolean showProgressDialog;
    private String progressMessage;

    public static EasyApiCalls init(Context context){
        EasyApiCalls.context = context;
        return new EasyApiCalls(context);
    }

    public EasyApiCalls(Context context) {
        this.url = null;
        this.methodType = RequestTypes.GET;
        this.params = null;
        this.headers = null;
        this.StatusKeyName = null;
        this.status_codes = null;
        this.showProgressDialog = false;
        this.progressMessage = "";
    }

    @Override
    public RequestApp setStatusHandler(String StatusKeyName, Map<String, String> status_codes) {
        this.StatusKeyName = StatusKeyName;
        this.status_codes = status_codes;
        return this;
    }

//    @Override
//    public RequestApp init(Context context) {
//        this.context = context;
//        return this;
//    }


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

        NetworkRequest.Request(this.context, this.showProgressDialog, this.progressMessage, this.url, this.params, this.headers, this.methodType, this.StatusKeyName, this.status_codes, new NetworkRequest.GetResponse() {
            @Override
            public void onSuccess(String status_code, JSONObject result) throws JSONException {

                onCallBack.onSuccess(status_code, result);
            }

            @Override
            public void onFail(String msg) {

                onCallBack.onFail(msg);

            }
        });
    }
}
