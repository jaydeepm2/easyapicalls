package com.jaydeepm2.easyapicall;

import android.content.Context;

import java.util.Map;

public interface RequestApp {
    RequestApp setStatusHandler(String StatusKeyName, String success_value);

    RequestApp showProgressDialog(boolean showProgressDialog);
    RequestApp setProgressMessage(String progressMessage);
    RequestApp setUrl(String url);
    RequestApp setType(int type);
    RequestApp setParams(Map<String, String> params);
    RequestApp setHeaders(Map<String, String> headers);
    void makeRequest(final NetworkRequest.GetResponse onCallBack);
}
