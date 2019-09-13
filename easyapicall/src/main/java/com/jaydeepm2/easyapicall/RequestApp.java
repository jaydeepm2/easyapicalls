package com.jaydeepm2.easyapicall;

import android.content.Context;

import java.util.Map;

public interface RequestApp {
    RequestApp init(Context context);
    RequestApp setUrl(String url);
    RequestApp setType(int type);
    RequestApp setParams(Map<String, String> params);
    RequestApp setHeaders(Map<String, String> headers);
    void makeRequest(final NetworkRequest.GetResponse onCallBack);
}
