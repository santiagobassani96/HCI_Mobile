package com.bassanidevelopment.santiago.hci_movil.API;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bassanidevelopment.santiago.hci_movil.Model.Device;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DevicesAPI  {

    private static String BASE_URL = SingletonAPI.BASE_URL;
    private Context context;


    /**
     * Retrieve all devices
     */
    public static void getAllDevices(final Callback callbackAction, Context context) {
        callbackAction.showSpinner();
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + "devices",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("getAllDevices", response.toString());
                        try {

                        callbackAction.handleResponse(response);
                        callbackAction.hideSpinner();
                        }catch (Exception e){
                            Log.d("ERROR","something went wrong while retrieving all dev");
                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("getAllDevices", "Error: " + error.getMessage());
                    }
                });

        SingletonAPI.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectReq, "getAllDevices");

    }


    public static Object getResponse() throws InterruptedException {
        SingletonResponse apiResponse = SingletonResponse.getInstance();
        return apiResponse.getResponse();
    }

    /**
     * Creates a new device
     * @param typeId The devices type id
     * @param name The device name
     */
    public static void createDevice(Context context, String typeId, String name) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("typeId", typeId);
            jsonObject.accumulate("name", name);
            jsonObject.accumulate("meta", "{count:1}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST,
                BASE_URL + "devices",
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("createDevice", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("createDevice", "Error: " + error.getMessage());
                    }
                });

        SingletonAPI.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectReq, "createDevice");
    }

    /**
     * Delete an existing device
     * @param deviceId The device id
     */
    public static void deleteDevice(Context context, String deviceId, final Callback callback) {

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.DELETE,
                BASE_URL + "devices/" + deviceId,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.handleResponse(response);
                        Log.d("deleteDevice", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("deleteDevice", "Error: " + error.getMessage());
                    }
                });

        SingletonAPI.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectReq, "deleteDevice");
    }

    /**
     * Updates an existing device
     *
     */
    public static void updateDevice(Context context, JSONObject dev) throws JSONException {



        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.PUT,
                BASE_URL + "devices/" + dev.getString("id"),
                dev,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("updateDevice", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("updateDevice", "Error: " + error.getMessage());
                    }
                });

        SingletonAPI.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectReq, "updateDevice");
    }


    public static void checkDevicestatus(Context context,String id,  final  Callback callback ) throws JSONException {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + "devices/" + id + "/events",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Event:", response.toString());
                        callback.handleResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error","Event couldn't be reached");
                    }
                });

        SingletonAPI.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest, "deviceEvents");
    }

    public static void getEvents(Context context, final Callback callback)throws JSONException{
        final StringRequest request = new StringRequest(Request.Method.GET,
                BASE_URL + "devices/events",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        JSONArray events = extractEvents(response);
                        JSONObject eventResponse = new JSONObject();
                        try {
                            eventResponse.put("events",events);
                            callback.handleResponse(eventResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error","Event couldn't be reached");
                    }
                });
        SingletonAPI.getInstance(context.getApplicationContext()).addToRequestQueue(request, "deviceEvents");
    }

    public static JSONArray extractEvents(String responseStream){


        JSONArray arrayOfEvents = new JSONArray();
        List<String> responseLines = Arrays.asList(responseStream.split("\n"));
        int counter = 0;
        for(String line : responseLines){
            if(line.matches(".* \"event\": .*")){
                JsonObject event = new JsonObject();

                event.addProperty("event", line.substring(line.indexOf("\"event\"")));
                arrayOfEvents.put(event);
            }

        }
        System.out.println(responseStream.replaceAll("data:",""));
        return arrayOfEvents;

    }
}
