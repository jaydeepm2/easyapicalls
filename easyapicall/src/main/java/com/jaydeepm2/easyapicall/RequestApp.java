package com.jaydeepm2.easyapicall;

import android.content.Context;
import android.net.Uri;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public interface RequestApp {
    RequestApp setStatusHandler(String StatusKeyName, String success_value);

    RequestApp showProgressDialog(boolean showProgressDialog);
    RequestApp setProgressMessage(String progressMessage);
    RequestApp setUrl(String url);
    RequestApp setType(int type);
    RequestApp setParams(Map<String, String> params);
    RequestApp setHeaders(Map<String, String> headers);
    RequestApp setMultipartRequest(boolean multipartRequest);
    RequestApp setFiles(Map<String, Uri> fileParams);
    RequestApp setJsonData(JSONObject jsonData);
    RequestApp setJsonRequest(boolean isJsonRequest);
    void makeRequest(final NetworkRequest.GetResponse onCallBack);
}
