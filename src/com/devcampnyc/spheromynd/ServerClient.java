package com.devcampnyc.spheromynd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.devcampnyc.spheromynd.DashboardActivity.MindwaveState;

import android.util.Log;

public class ServerClient {

  private static final String TAG = "SpheroServerClient";
  private static final String MINDSTATE_URL = "";
  
  private HttpClient mClient;
  
  public ServerClient() {
    mClient = new DefaultHttpClient();
  }
  
  public void sendMindwaveState(final MindwaveState state) {
   
    new Thread(new Runnable() {
      
      @Override
      public void run() {
        try {
          JSONObject json = new JSONObject();
          json.put("attention", state.attention);
          json.put("meditation", state.meditation);
          json.put("blink", state.blink);

          HttpPost request = new HttpPost(MINDSTATE_URL);
          request.setEntity(new StringEntity(json.toString()));

          Log.d(TAG, "Sending to [" + MINDSTATE_URL + "]: " + json.toString());

          HttpResponse response = mClient.execute(request);

          if (response.getStatusLine().getStatusCode() != 200) {
            Log.w(TAG, "Could not send state to server");
          }
        } catch (JSONException e) {
          Log.e(TAG, "Could not create mindwave state json", e);
        }
        catch (IOException e) {
          Log.e(TAG, "Could not connect to server", e);
        }
      }
    }).start();
  }
  
}
