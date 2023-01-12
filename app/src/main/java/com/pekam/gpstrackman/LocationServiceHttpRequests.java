package com.pekam.gpstrackman;


import android.content.Context;
import android.provider.Settings;

import androidx.lifecycle.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import com.pekam.gpstrackman.events.MessageEvent;

import com.pekam.gpstrackman.viewmodels.UserViewModel;
import org.greenrobot.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import pekam.entities.TblGpsAll;
import pekam.entities.TblUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


public class LocationServiceHttpRequests  {

    private RequestQueue mRequestQueue=null;
    private UserViewModel mUserViewModel=null;
    private final Context mContext;
    private Logger mLogger;
    private final boolean isDevServer =false;
    private final ObjectMapper mMapper = new ObjectMapper();

    public LocationServiceHttpRequests(ViewModelStoreOwner viewModelStoreOwner,Context context) {
     super();
     mContext=context;
     mLogger= new Logger(context.getDataDir().getAbsolutePath() +" logfile.txt");
     mRequestQueue= Volley.newRequestQueue(mContext);
     mUserViewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
     mMapper.registerModule(new JavaTimeModule());
     }

    public void getUser(int userid) {

        String mJSONURLString=null;
         if(isDevServer) {

             mJSONURLString = "http://10.0.0.154:8080/rest/user/get?id=" + userid;
         }else {
             mJSONURLString = "http://vmi496836.contaboserver.net:8080/gpstracker/rest/user/get?id=" + userid;
         }

     // Initialize a new JsonObjectRequest instance
     JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
             Request.Method.GET,
             mJSONURLString,
             null,
             new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            //JSON string to Java Object
                            TblUser u= mMapper.readValue(response.toString(), TblUser.class);
                            mUserViewModel.getUser().setValue(u);
                            } catch (Exception ex) {
                            mLogger.appendLog(ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLogger.appendLog(error.getMessage());
                        EventBus.getDefault().post(new MessageEvent("Volley Error: " + error.getLocalizedMessage()));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String mApiKey = "application/json";
                headers.put("content-type:", mApiKey);
                return headers;
            }
        };

        jsonObjectRequest.setShouldRetryConnectionErrors(true);
        jsonObjectRequest.setShouldRetryServerErrors(true);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return -1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                EventBus.getDefault().post(new MessageEvent("Volley Error: " + error.getLocalizedMessage()));
            }
        });

        // Add JsonObjectRequest to the RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }
    public void getUser(String appid) {

     String mJSONURLString=null;
     if(isDevServer) {

          mJSONURLString = "http://10.0.0.154:8080/rest/user/getByAppId?id=" + appid;
     }else {
          mJSONURLString = "http://vmi496836.contaboserver.net:8080/gpstracker/rest/user/getByAppId?id=" + appid;
     }

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                mJSONURLString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                                    if (response.length()==0)
                                    {
                                        TblUser u = new TblUser();
                                     Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                                        mUserViewModel.getUser().setValue(u);

                                    } else {
                                ObjectMapper mapper = new ObjectMapper();
                                        mapper.enable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
                                        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
                                        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                mapper.registerModule(new JavaTimeModule());

                                //JSON string to Java Object
                                TblUser u = mapper.readValue(response.toString(), TblUser.class);
                                mUserViewModel.getUser().setValue(u);
                            }
                        } catch (JacksonException ex) {

                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        EventBus.getDefault().post(new MessageEvent("Volley Error: " + error.getLocalizedMessage()));

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String mApiKey = "application/json";
                headers.put("content-type:", mApiKey);
                return headers;
            }
        };

        jsonObjectRequest.setShouldRetryConnectionErrors(false);
        jsonObjectRequest.setShouldRetryServerErrors(false);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return -1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                EventBus.getDefault().post(new MessageEvent("Volley Error: " + error.getLocalizedMessage()));
            }
        });

        // Add JsonObjectRequest to the RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }
    public void saveUserToDevice(TblUser user) {

        try {
            String strJason = mMapper.writeValueAsString(mUserViewModel.getUser().getValue());

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("user.xml", Context.MODE_PRIVATE));
            outputStreamWriter.write(strJason);
            outputStreamWriter.close();
        } catch (JsonProcessingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void loadUserFromDevice() {

        try {
            InputStreamReader inputstream = new InputStreamReader(mContext.openFileInput("user.xml"));

            TblUser user = mMapper.readValue(inputstream.toString(), TblUser.class);
            inputstream.close();
            mUserViewModel.getUser().setValue(user);

        } catch (JsonProcessingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

}
    public void updateUser(TblUser user) throws JSONException {

        String mJSONURLString=null;
         if(isDevServer) {
            mJSONURLString = "http://10.0.0.154:8080/rest/user/update";
        }else {
            mJSONURLString = "http://vmi496836.contaboserver.net:8080/gpstracker/rest/user/update";
        }


        mMapper.enable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        mMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        mMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //JSON string to Java Object
        String strJason = "";

        try {
           // mapper.enable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);
            strJason= mMapper.writeValueAsString(mUserViewModel.getUser().getValue());
          /*  GsonBuilder gsonBuildr = new GsonBuilder();
            String jsonString = gsonBuildr.create().toJson(mUserViewModel.getUser().getValue());*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectPostRequest = new JsonObjectRequest(
                Request.Method.POST,
                mJSONURLString,
                new JSONObject(strJason),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response

                        try {

                            //JSON string to Java Object
                            mUserViewModel.getUser().setValue(mMapper.readValue(response.toString(), TblUser.class));

                        } catch (Exception ex) {
                            mLogger.appendLog(ex.getMessage());
                            EventBus.getDefault().post(new MessageEvent(ex.getLocalizedMessage()));
                         }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLogger.appendLog(error.getMessage());
                        EventBus.getDefault().post("VolleyError: "+ new MessageEvent(error.getLocalizedMessage()));
                     }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String mApiKey = "application/json";
                headers.put("content-type", mApiKey);
                return headers;
            }
        };

        jsonObjectPostRequest.setShouldRetryConnectionErrors(false);
        jsonObjectPostRequest.setShouldRetryServerErrors(false);
        jsonObjectPostRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return -1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });

        // Add JsonObjectRequest to the RequestQueue
        mRequestQueue.add(jsonObjectPostRequest);
    }
    public void updateGpsAll(TblGpsAll gpsall) throws JSONException {

        String mJSONURLString = null;

          if(isDevServer) {

            mJSONURLString = "http://10.0.0.154:8080/rest/gpsall/update";
        }else {
            mJSONURLString = "http://vmi496836.contaboserver.net:8080/gpstracker/rest/gpsall/update";
        }

        mMapper.enable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        mMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        mMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //JSON string to Java Object
        String strJason = "";

        try {
              strJason= mMapper.writeValueAsString(gpsall);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectPostRequest = new JsonObjectRequest(
                Request.Method.POST,
                mJSONURLString,
                new JSONObject(strJason),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response

                        try {
                            EventBus.getDefault().post(new MessageEvent("Send to Server Ok!"));
                        } catch (Exception ex) {
                        //    mLogger.appendLog(ex.getMessage());
                            EventBus.getDefault().post(new MessageEvent(ex.getLocalizedMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLogger.appendLog(error.getMessage());
                        EventBus.getDefault().post("VolleyError: "+ new MessageEvent(error.getLocalizedMessage()));
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String mApiKey = "application/json";
                headers.put("content-type", mApiKey);
                return headers;
            }
        };

        jsonObjectPostRequest.setShouldRetryConnectionErrors(false);
        jsonObjectPostRequest.setShouldRetryServerErrors(false);
        jsonObjectPostRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return -1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });

        // Add JsonObjectRequest to the RequestQueue
        mRequestQueue.add(jsonObjectPostRequest);
    }
}
