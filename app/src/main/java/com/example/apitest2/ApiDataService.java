package com.example.apitest2;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ApiDataService {
    protected Context context;
    //public static final String BASE_URL = "https://take-me-backend.herokuapp.com";
    public static final String BASE_URL = "http://192.168.1.4:3000";

    public static final String LOCAL_URL = "http://192.168.1.3:3000";

    public ApiDataService(Context context) {
        this.context = context;
    }

    public Map<String, String> makeHeaders(String key, String value) {
        // below line we are creating a map for
        // storing our values in key and value pair.
        Map<String, String> params = new HashMap<>();

        // on below line we are passing our key
        // and value pair to our parameters.
        params.put(key, value);

        // at last we are
        // returning our params.
        return params;
    }

    public interface VolleyResponseListener {
        void onError(Object message);

        void onResponse(Object responseObject);
    }
    public void getData(String email, String password, VolleyResponseListener volleyResponseListener) {

        // Request a string response from the provided URL.
        //String url = LOCAL_URL + "/api/dummy/owner/reqTest";
        String url = BASE_URL + "/api/owner/login";

        Map<String,String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        String requestBody = new JSONObject(params).toString();

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                volleyResponseListener::onResponse,
                volleyResponseListener::onError) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("email", email);
                params.put("password", password);

                // at last we are
                // returning our params.
                return params;
            }
        };*/

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                volleyResponseListener::onResponse,
                volleyResponseListener::onError){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("body", new JSONObject(jsonString));
                    jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };


        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getProfileData(String token, VolleyResponseListener volleyResponseListener) {

        // Request a string response from the provided URL.
        //String url = LOCAL_URL + "/api/dummy/owner/reqTest";
        String url = BASE_URL + "/api/owner/dashboard";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                volleyResponseListener::onResponse,
                volleyResponseListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("auth-token", token);

                // at last we are
                // returning our params.
                return params;
            }
        };


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     *
     * @param email like "robin@loa.com"
     * @param password like "thisisrobin"
     * @param volleyResponseListener the onResponse method should be something like this
     * <pre>
     * {@code
     *  try {
     *      responseData = new JSONObject(responseObject.toString());
     *      System.out.println(responseData);
     *
     *      bodyData = (JSONObject) responseData.get("body");
     *      headerData = (JSONObject) responseData.get("headers");
     *
     *      System.out.println("body:" + bodyData);
     *      System.out.println("headers:" + headerData);
     *
     *      driverInfo = (JSONObject) bodyData.get("data");
     *      System.out.println("driverInfo:" + driverInfo);
     *
     *      //save the token somewhere
     *      token = (String) headerData.get("Auth-Token");
     *      System.out.println("token:" + token);
     *
     *  } catch (JSONException e) {
     *      e.printStackTrace();
     *  }
     *  }</pre>
     */
    public void driverLoginData(String email, String password, VolleyResponseListener volleyResponseListener) {

        // Request a string response from the provided URL.
        //String url = LOCAL_URL + "/api/driver/login/";
        String url = BASE_URL + "/api/driver/login/";

        Map<String,String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        //System.out.println(new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                volleyResponseListener::onResponse,
                volleyResponseListener::onError){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("body", new JSONObject(jsonString));
                    assert response.headers != null;
                    jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     *
     * @param token the token retrieved from login
     * @param volleyResponseListener onResponse you get plain object containing driver data
     * this method should be implemented following the given sample
     * <pre>
     * {@code
     *  try {
     *      responseData = new JSONObject(responseObject.toString());
     *      System.out.println(responseData);
     * } catch (JSONException e) {
     *      e.printStackTrace();
     * }
     *                               }</pre>
     */
    public void getDriverProfileData(String token, VolleyResponseListener volleyResponseListener) {

        // Request a string response from the provided URL.
        //String url = LOCAL_URL + "/api/driver/dashboard/";
        String url = BASE_URL + "/api/driver/dashboard/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                volleyResponseListener::onResponse,
                volleyResponseListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     *
     * @param token the token retrieved from login
     * @param volleyResponseListener onResponse
     * this method should be implemented following the given sample
     *
     * <pre>
     * {@code
     *  try {
     *      responseData = new JSONObject(responseObject.toString());
     *      System.out.println(responseData);
     *
     *      if(responseData.has("passengerInfo") {
     *          passengerInfo = (JSONObject) responseData.get("passengerInfo");
     *          System.out.println(passengerInfo);
     *      } else {
     *          message = responseData.get("message");
     *          //first time or no match so nothing I guess
     *      }
     *
     *   } catch (JSONException e) {
     *      e.printStackTrace();
     *   }
     * }</pre>
     *
     */
    public void searchPassenger(String token, VolleyResponseListener volleyResponseListener) {

        // Request a string response from the provided URL.
        //String url = LOCAL_URL + "/api/driver/search/";
        String url = BASE_URL + "/api/driver/search/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                volleyResponseListener::onResponse,
                volleyResponseListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     *
     * @param token the token retrieved from login
     * @param volleyResponseListener
     * this method should be implemented following the given sample
     *
     * <pre>
     * {@code
     *  try {
     *      responseData = new JSONObject(responseObject.toString());
     *      System.out.println(responseData);
     *   } catch (JSONException e) {
     *      e.printStackTrace();
     *   }
     * }</pre>
     *
     */
    public void stopSearchPassenger(String token, VolleyResponseListener volleyResponseListener) {

        // Request a string response from the provided URL.
        //String url = LOCAL_URL + "/api/driver/stopSearch/";
        String url = BASE_URL + "/api/driver/stopSearch/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                volleyResponseListener::onResponse,
                volleyResponseListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * A function that matches a custom passenger to the given driverID and removes the driver from the pool
     * @param driverID driverID
     * @param volleyResponseListener onResponse
     * I don't think this is needed :3
     *
     * <pre>
     * {@code
     *  try {
     *      responseData = new JSONObject(responseObject.toString());
     *      System.out.println(responseData);
     *   } catch (JSONException e) {
     *      e.printStackTrace();
     *   }
     * }</pre>
     *
     */
    public void customMatching(String driverID, VolleyResponseListener volleyResponseListener) {

        // Request a string response from the provided URL.
        //String url = LOCAL_URL + "/api/dummy/owner/reqTest";
        String url = BASE_URL + "/api/passenger/acceptDriver";
        //String url = BASE_URL + "/api/dummy/owner/reqTest";

        String[] tokens = {"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDc0NzgxNzhjMjljMTQwOGNmYWQyOTAiLCJpYXQiOjE2MjI1NjQ3NDZ9.JZAM2JfO-QuVD5qbL0wQ7ptsifX3KQEe0kzsWQYo9bA",
                            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDc0NzgxNzhjMjljMTQwOGNmYWQyOTIiLCJpYXQiOjE2MjI1NjUxMjZ9.S_pl-rQ-lxw6Dc9QM6B4jW6WUGhHdZYGxjd-E4Scsa4",
                            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDc0NzgxNzhjMjljMTQwOGNmYWQyOTciLCJpYXQiOjE2MjI1NjUxNzB9.Eq9h22EUefCVY9eQSIYI1S0c_VvA3ywW7oSGmviAjyk",
                            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDc0NzgxODhjMjljMTQwOGNmYWQyOTkiLCJpYXQiOjE2MjI1NjUyMTB9.BrdCkVeT1cdFd1VhrgD7IwKxHDEpkkfIswH2trXcdiE",
                            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDc0NzgxODhjMjljMTQwOGNmYWQyYTEiLCJpYXQiOjE2MjI1NjUyNTd9.697B5W-LF-5su6jV5vvOdQkOj4WMWuGLWFpJ5CnjBug",
                            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDc0NzgxODhjMjljMTQwOGNmYWQyYTUiLCJpYXQiOjE2MjI1NjUyOTJ9.UulbXYNHCxt7u0O7Scj48umJmquWXq7dPdGEhcI-F7k"};

        Random random = new Random();
        String token = tokens[random.nextInt(tokens.length)];

        double lat = 23 + (random.nextInt(1000000)%75000 + 40000)/100000.0;
        double lon = 90 + (random.nextInt(1000000)%75000 + 40000)/100000.0;
        double[] pickUpPoint = {lat, lon};

        Map<String,String> params = new HashMap<>();
        params.put("driverID", driverID);
        try {
            System.out.println(Arrays.toString(pickUpPoint));
            JSONArray p = new JSONArray(Arrays.toString(pickUpPoint));
            System.out.println(p);
            params.put("pickUpPoint", p.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                volleyResponseListener::onResponse,
                volleyResponseListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void cancelMatch(String token, VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "/api/driver/cancelMatch";

        Map<String,String> params = new HashMap<>();
        params.put("entity", "driver");
        //System.out.println(new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                volleyResponseListener::onResponse,
                volleyResponseListener::onError){
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void startRide(String token, VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "/api/driver/startRide";

        Map<String,String> params = new HashMap<>();
        params.put("entity", "driver");
        //System.out.println(new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                volleyResponseListener::onResponse,
                volleyResponseListener::onError){
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void endRide(String token, double lat, double lon, VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "/api/driver/endRide";
        double[] location = {lat, lon};
        Map<String,String> params = new HashMap<>();
        params.put("entity", "driver");
        try {
            System.out.println(Arrays.toString(location));
            JSONArray p = new JSONArray(Arrays.toString(location));
            System.out.println(p);
            params.put("location", p.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println(new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                volleyResponseListener::onResponse,
                volleyResponseListener::onError){
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void updateLocation(String token, double lat, double lon, VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "/api/driver/vehicle/location";

        double[] location = {lat, lon};

        Map<String,String> params = new HashMap<>();
        try {
            System.out.println(Arrays.toString(location));
            JSONArray p = new JSONArray(Arrays.toString(location));
            System.out.println(p);
            params.put("location", p.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println(new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                volleyResponseListener::onResponse,
                volleyResponseListener::onError){
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void viewDriver(String token, String vehicleID, VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "/api/owner/vehicle/id/" + vehicleID + "/status";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                volleyResponseListener::onResponse,
                volleyResponseListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                return makeHeaders("auth-token", token);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void deletePool(VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "/api/driver/pool";

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                volleyResponseListener::onResponse,
                volleyResponseListener::onError
        );

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
