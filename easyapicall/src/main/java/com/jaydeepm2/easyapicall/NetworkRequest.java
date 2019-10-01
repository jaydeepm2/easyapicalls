package com.jaydeepm2.easyapicall;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.jaydeepm2.easyapicall.Constants.EASYAPI_AUTH_FAIL_ERROR;
import static com.jaydeepm2.easyapicall.Constants.EASYAPI_DATA_ERROR;
import static com.jaydeepm2.easyapicall.Constants.EASYAPI_NETWORK_ERROR;
import static com.jaydeepm2.easyapicall.Constants.EASYAPI_SERVER_ERROR;
import static com.jaydeepm2.easyapicall.Constants.EASYAPI_STATUS_FAILURE;
import static com.jaydeepm2.easyapicall.Constants.EASYAPI_STATUS_SUCCESS;
import static com.jaydeepm2.easyapicall.Constants.EASYAPI_TIMEOUT_ERROR;

public class NetworkRequest {



    public static void Request(final Context context, final boolean showProgressDialog, String progressMessage, String url, final Map<String, String> params, final Map<String, String> headers, int methodType, final String StatusKeyName, final String success_value, boolean isMultipartRequest, final Map<String, Uri> fileParams, final GetResponse onCallBack) {

        try {
            if (NetworkUtility.isNetworkAvailable(context)) {

                if (showProgressDialog) {
                    NetworkUtility.OpenLoadingDialog(context, progressMessage);
                }

                if (isMultipartRequest) {
                Log.i("JAY EASY N", "1");
                    MultipartRequest multipartRequest = new MultipartRequest(RequestTypes.POST, url, new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response1) {

                            if (showProgressDialog) {
                                NetworkUtility.HideLoading();
                            }

                            try {
                                String response = new String(response1.data, "UTF-8");
                                JSONObject obj = new JSONObject(response);
//                            Iterator it = status_codes.entrySet().iterator();
                                String return_status_code = EASYAPI_STATUS_FAILURE;
                                if (obj.getString(StatusKeyName).equals(success_value)) {
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
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    onCallBack.onSuccess(EASYAPI_STATUS_FAILURE, new JSONObject());
                                } catch (Exception e2) {
                                }
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (showProgressDialog) {
                                        NetworkUtility.HideLoading();
                                    }
                                    String error_code = "";
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        error_code = EASYAPI_TIMEOUT_ERROR;
                                    } else if (error instanceof AuthFailureError) {
                                        //TODO
                                        error_code = EASYAPI_AUTH_FAIL_ERROR;
                                    } else if (error instanceof ServerError) {
                                        //TODO
                                        error_code = EASYAPI_SERVER_ERROR;
                                    } else if (error instanceof NetworkError) {
                                        //TODO
                                        error_code = EASYAPI_NETWORK_ERROR;
                                    } else if (error instanceof ParseError) {
                                        //TODO
                                        error_code = EASYAPI_DATA_ERROR;
                                    }

                                    String body = null;
                                    //get status code here
                                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse.data != null) {
                                        try {
                                            body = new String(error.networkResponse.data, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    onCallBack.onFail(error_code, body);
                                }
                            }
                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> paramsFinal = new HashMap<>();
                            paramsFinal.put("EASYAPI", "");
                            try {
                                if (Objects.requireNonNull(params) != null) {
                                    paramsFinal = params;
                                }
                            } catch (Exception e) {
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
                            } catch (Exception e) {
                            }
                            return headersFinal;
                        }
                        /*
                         *pass files using below method
                         * */
                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> fParams = new HashMap<>();
                            try {
                                for (Map.Entry<String,Uri> entry : fileParams.entrySet()){
                                    Log.i("JAY FILES", entry.getValue()+"");
                                    long imagename = System.currentTimeMillis();
                                    byte[] dataPart = NetworkUtility.getFileDataFromDrawable(MediaStore.Images.Media.getBitmap(context.getContentResolver(), entry.getValue()));
                                    fParams.put(entry.getKey(), new DataPart(imagename + ".png", dataPart));
                                }
                            }
                            catch (Exception e){
                            }
                            return fParams;
                        }
                    };

                    multipartRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance(context).addToRequestQueue(multipartRequest);
                }
                else{
                Log.i("JAY EASY N", "0");
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
                                if (obj.getString(StatusKeyName).equals(success_value)) {
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
                                } catch (Exception e2) {
                                }
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (showProgressDialog) {
                                        NetworkUtility.HideLoading();
                                    }
                                    String error_code = "";
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        error_code = EASYAPI_TIMEOUT_ERROR;
                                    } else if (error instanceof AuthFailureError) {
                                        //TODO
                                        error_code = EASYAPI_AUTH_FAIL_ERROR;
                                    } else if (error instanceof ServerError) {
                                        //TODO
                                        error_code = EASYAPI_SERVER_ERROR;
                                    } else if (error instanceof NetworkError) {
                                        //TODO
                                        error_code = EASYAPI_NETWORK_ERROR;
                                    } else if (error instanceof ParseError) {
                                        //TODO
                                        error_code = EASYAPI_DATA_ERROR;
                                    }

                                    String body = null;
                                    //get status code here
                                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse.data != null) {
                                        try {
                                            body = new String(error.networkResponse.data, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    onCallBack.onFail(error_code, body);
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
                            } catch (Exception e) {
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
                            } catch (Exception e) {
                            }
                            return headersFinal;
                        }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
                }

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

        void onFail(String error_id, String error_body);
    }

}
