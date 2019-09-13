package com.jaydeepm2.easyapicall;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.Map;

public class EasyApiCalls implements RequestApp{

    Context context;
    String url;
    int methodType;
    Map<String, String> params;
    Map<String, String> headers;

    @Override
    public RequestApp init(Context context) {
        this.context = context;
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

        NetworkRequest.Request(this.context, this.url, this.params, this.headers, this.methodType, new NetworkRequest.GetResponse() {
            @Override
            public void onSuccess(String result) throws JSONException {

                onCallBack.onSuccess(result);
            }

            @Override
            public void onFail(VolleyError error) {
                onCallBack.onFail(error);

            }
        });
    }
}
