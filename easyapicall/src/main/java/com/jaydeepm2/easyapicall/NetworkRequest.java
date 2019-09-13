package com.jaydeepm2.easyapicall;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NetworkRequest {



    public static void Request(final Context context, String url, final Map<String, String> params, final Map<String, String> headers, int methodType, final GetResponse onCallBack) {

        if (NetworkUtility.isNetworkAvailable(context)) {

            NetworkUtility.OpenLoadingDialog(context, "");

            StringRequest stringRequest = new StringRequest(methodType, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    NetworkUtility.HideLoading();

                    try {
                        onCallBack.onSuccess(response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkUtility.HideLoading();

                            onCallBack.onFail(error);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> paramsFinal = new HashMap<>();
                    if (Objects.requireNonNull(params) != null){
                        paramsFinal = params;
                    }
                    return paramsFinal;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headersFinal = new HashMap<>();
                    if (Objects.requireNonNull(headers) != null){
                        headersFinal = headers;
                    }
                    return headersFinal;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } else {
            NetworkUtility.Toast(context, "Please check your internet connection");
        }
    }

    public interface GetResponse {
        void onSuccess(String result) throws JSONException;

        void onFail(VolleyError error);
    }

}
