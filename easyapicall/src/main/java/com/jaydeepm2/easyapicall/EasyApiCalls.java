package com.jaydeepm2.easyapicall;

import android.content.Context;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class EasyApiCalls implements RequestApp{

    private static Context context;
    private String url;
    private int methodType;
    private Map<String, String> params;
    private Map<String, String> headers;

    private String StatusKeyName,success_value;
    private boolean showProgressDialog;
    private String progressMessage;

    private boolean isMultipartRequest;
    private Map<String, Uri> fileParams;


    public static EasyApiCalls init(Context context){
        EasyApiCalls.context = context;
        NetworkUtility.requestMultiplePermissions(context);
        return new EasyApiCalls(context);
    }

    public EasyApiCalls(Context context) {
        this.url = null;
        this.methodType = RequestTypes.GET;
        this.params = null;
        this.headers = null;
        this.StatusKeyName = null;
        this.showProgressDialog = false;
        this.progressMessage = "";
        this.isMultipartRequest = false;
        this.fileParams = null;
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

    public RequestApp setMultipartRequest(boolean multipartRequest) {
        this.isMultipartRequest = multipartRequest;
        return this;
    }

    public RequestApp setFiles(Map<String, Uri> fileParams) {
        this.fileParams = fileParams;
        return this;
    }

    @Override
    public void makeRequest(final NetworkRequest.GetResponse onCallBack) {

        NetworkRequest.Request(this.context, this.showProgressDialog, this.progressMessage, this.url, this.params, this.headers, this.methodType, this.StatusKeyName, this.success_value, this.isMultipartRequest, this.fileParams, new NetworkRequest.GetResponse() {
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
