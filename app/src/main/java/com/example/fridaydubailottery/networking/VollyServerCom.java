package com.example.fridaydubailottery.networking;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fridaydubailottery.AppController;
import com.example.fridaydubailottery.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class VollyServerCom {

    private Context context;
    private VollyResponseCallsBack vollyResponseCallsBackListener;

    private static String TAG = "Volly Response";

    private CustomProgressDialog customProgressDialog;
    private static final int socketTimeout = 30000;//30 seconds - change to what you want
    private RetryPolicy retryPolicy;


    public VollyServerCom(Context context, VollyResponseCallsBack vollyCallBackListener) {
        this.context = context;
        this.vollyResponseCallsBackListener = vollyCallBackListener;

        customProgressDialog = new CustomProgressDialog(context);

        retryPolicy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }


    public void vollyGETRequest(final String uri, String request_tag, final int request_id) {

        //customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                        } else {
                            vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                        }
                    }
                });

        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyAuthGETRequest(final String uri, String request_tag, final String authorization, final int request_id) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                uri,
                null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                        } else {
                            vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.INSTANCE.getAUTHORIZATION(), authorization);
                return headers;
            }

           /* @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }*/
        };

        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyGETRequest(final String uri, String request_tag, final int request_id,
                                final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                        }

                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }

                        // hide the progress dialog
                        customProgressDialog.stopProgressDialog();
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 30 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyAuthGETRequest(final String uri, String request_tag, final String authorization, final int request_id,
                                    final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);

        StringRequest jsonObjReq = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response, request_id);
                        }
                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }

                        // hide the progress dialog
                        customProgressDialog.stopProgressDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.INSTANCE.getAUTHORIZATION(), authorization);
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollySimpleStringRequest(final String uri, String request_tag, final int request_id,
                                         final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);

        StringRequest strReq = new StringRequest(
                Request.Method.GET,
                uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                        }
                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }

                        customProgressDialog.stopProgressDialog();
                    }

                });

        strReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, request_tag);

    }

    public void vollyPOSTRequest(final String uri, final JSONObject request_body, String request_tag, final int request_id,
                                 final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                uri,
                request_body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                        }
                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }
                        customProgressDialog.stopProgressDialog();
                    }
                });

        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        jsonObjReq.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyPOSTRequest(final String uri, final JSONObject request_body, String request_tag, final int request_id) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                uri,
                request_body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (error == null || error.networkResponse == null) {
                            return;
                        }

                        String body;
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                        } else {
                            vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                        }
                    }
                }) {
        };


        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyAuthPOSTRequest(final String uri, final JSONObject request_body, final String authorization, String request_tag, final int request_id,
                                     final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                uri,
                request_body,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                        }

                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }

                        customProgressDialog.stopProgressDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.INSTANCE.getAUTHORIZATION(), authorization);
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(retryPolicy);
        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyAuthPOSTRequest(final String uri, final JSONObject request_body, final String authorization, String request_tag, final int request_id) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                uri,
                request_body,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());


                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                        } else {
                            vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                        }

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.INSTANCE.getAUTHORIZATION(), authorization);
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(retryPolicy);
        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyPUTRequest(final String uri, final JSONObject request_body, String request_tag, final int request_id,
                                final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                uri,
                request_body,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                        }


                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }

                        customProgressDialog.stopProgressDialog();
                    }
                });


        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyPUTRequest(final String uri, final JSONObject request_body, String request_tag, final int request_id) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                uri,
                request_body,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());


                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                        } else {
                            vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                        }

                    }
                });


        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyAuthPUTRequest(final String uri, final String authorization, final JSONObject request_body, String request_tag, final int request_id,
                                    final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                uri,
                request_body,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                        }


                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }

                        customProgressDialog.stopProgressDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.INSTANCE.getAUTHORIZATION(), authorization);
                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void vollyDELETERequest(final String uri, final String authorization, String request_tag, final int request_id,
                                   final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                uri,
                null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            vollyResponseCallsBackListener.onVollySuccess(response.toString(), request_id);
                        }

                        customProgressDialog.stopProgressDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        if (customProgressDialog.getProgressDialog().isShowing()) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                vollyResponseCallsBackListener.onVollyError(new String(error.networkResponse.data), request_id);
                            } else {
                                vollyResponseCallsBackListener.onVollyError(error.toString(), request_id);
                            }
                        }

                        // hide the progress dialog
                        customProgressDialog.stopProgressDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.INSTANCE.getAUTHORIZATION(), authorization);
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(retryPolicy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

    public void cancelPendingRequestsByTag(String tag) {
        customProgressDialog.stopProgressDialog();
        AppController.getInstance().cancelPendingRequests(tag);
    }

    public void cancelAllPendingRequests() {
        customProgressDialog.stopProgressDialog();
        AppController.getInstance().cancelAllPendingRequests();
    }

    public void vollyPOSTRequestString(final String uri, final String request_body, String request_tag, final int request_id,
                                       final String progress_dialog_title, final boolean cancelable) {

        customProgressDialog.startProgressDialog(progress_dialog_title, cancelable);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return request_body == null ? null : request_body.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", request_body, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        jsonObjReq.setRetryPolicy(retryPolicy);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, request_tag);
    }

}
