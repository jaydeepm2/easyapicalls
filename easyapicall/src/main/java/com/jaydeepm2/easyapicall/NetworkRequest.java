package com.jaydeepm2.easyapicall;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.jaydeepm2.easyapicall.Constants.EASYAPI_STATUS_FAILURE;
import static com.jaydeepm2.easyapicall.Constants.EASYAPI_STATUS_SUCCESS;

public class NetworkRequest {



    public static void Request(final Context context, final boolean showProgressDialog, String progressMessage, String url, final Map<String, String> params, final Map<String, String> headers, int methodType, final String StatusKeyName, final String success_value, final GetResponse onCallBack) {

        try {
            if (NetworkUtility.isNetworkAvailable(context)) {

                if (showProgressDialog) {
                    NetworkUtility.OpenLoadingDialog(context, progressMessage);
                }

                StringRequest stringRequest = new StringRequest(methodType, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (showProgressDialog) {
                            NetworkUtility.HideLoading();
                        }

                        try {
                            JSONObject obj = new JSONObject(response);
//                            Iterator it = status_codes.entrySet().iterator();
                            String return_status_code = EASYAPI_STATUS_FAILURE;
                            if (obj.getString(StatusKeyName).equals(success_value)){
                                return_status_code = EASYAPI_STATUS_SUCCESS;
                            }
//                            while (it.hasNext()) {
//                                Map.Entry pair = (Map.Entry) it.next();
//                                if (obj.getString(StatusKeyName).equals(pair.getValue().toString())) {
//                                    return_status_code = pair.getValue().toString();
//                                    break;
//                                }
//                            }
                            onCallBack.onSuccess(return_status_code, obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                onCallBack.onSuccess(EASYAPI_STATUS_FAILURE, new JSONObject());
                            }
                            catch (Exception e2){}
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (showProgressDialog) {
                                    NetworkUtility.HideLoading();
                                }
                                String msg = "";
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    msg = "Timeout or No Connection Error.";
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                    msg = "Authentication Failure Error.";
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    msg = "Server Error.";
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    msg = "Network Error.";
                                } else if (error instanceof ParseError) {
                                    //TODO
                                    msg = "Data Error.";
                                }
                                onCallBack.onFail(msg);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramsFinal = new HashMap<>();
                        paramsFinal.put("EASYAPI", "");
                        try {
                            if (Objects.requireNonNull(params) != null) {
                                paramsFinal = params;
                            }
                        }
                        catch (Exception e){
                        }
                        return paramsFinal;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headersFinal = new HashMap<>();
                        headersFinal.put("EASYAPIH", "");
                        try {
                            if (Objects.requireNonNull(headers) != null) {
                                headersFinal = headers;
                            }
                        }
                        catch (Exception e){}
                        return headersFinal;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
            } else {
                NetworkUtility.Toast(context, "Please check your internet connection");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface GetResponse {
        void onSuccess(String status_code, JSONObject result) throws JSONException;

        void onFail(String msg);
    }

}
