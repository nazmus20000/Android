package seniordesign.ipfw.fw_trails_app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.preference.PreferenceActivity;
import android.util.Log;

import org.json.*;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.IllegalFormatException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Jaron on 3/9/2016.
 */
public class HttpClientUtil {

   public static final String BASE_URL_STATISTICS = "http://68.39.46.187:50000/GreenwayCap/DataRelay.svc/trails/api/1/Statistics";
   public static final String BASE_URL_ACTIVITY = "http://68.39.46.187:50000/GreenwayCap/DataRelay.svc/trails/api/1/Activity";
   public static final String BASE_URL_ACCOUNT_DETAILS = "http://68.39.46.187:50000/GreenwayCap/DataRelay.svc/trails/api/1/Account";
   public static final String BASE_URL_ACCOUNT_DETAILS_UPDATE = "http://68.39.46.187:50000/GreenwayCap/DataRelay.svc/trails/api/1/Account/edit";
   public static final String BASE_URL_LOGIN = "http://68.39.46.187:50000/GreenwayCap/DataRelay.svc/trails/api/1/Login";
   public static final String BASE_URL_CREATE_ACCOUNT = "http://68.39.46.187:50000/GreenwayCap/DataRelay.svc/trails/api/1/Account/Create";
   public static final String CONTENT_TYPE = "application/json";

   private String authKeycode;
   private String username;
   private String password;
   private boolean loginSuccessful;

   private static HttpClientUtil httpClientUtil = new HttpClientUtil();
   private static SyncHttpClient client = new SyncHttpClient();

   private HttpClientUtil(){

   }

   public static HttpClientUtil getInstance(){
      return httpClientUtil;
   }

   public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.get(url, params, responseHandler);
   }

   public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.post(getAbsoluteUrl(url), params, responseHandler);
   }


   public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.get(url, params, responseHandler);
   }

   public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.post(url, params, responseHandler);
   }

   public static void postByUrl(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
      client.post(context, url, entity, contentType, responseHandler);
   }

   // Sets the authentication keycode that is used in all api calls after login.
   public void setAuthKeycode(String theKey){
      authKeycode = theKey;
      client.addHeader("Trails-Api-Key", authKeycode);
   }

   // Save the account login info in the case the server loses the existing auth token for
   // the current instance of the application. We can use this data to login again, under the hood,
   // to get a new auth token that is recognized.
   public void setAccountInfo(String username, String password){
      this.username = username;
      this.password = password;
   }

   // Returns the authcode that is used to be appended in the headers of the posts and gets.
   public String getAuthCode(){
      return authKeycode;
   }

   // Returns the username of the current account.
   public String getAccountUsername(){
      return username;
   }

   // Returns the password of the current account.
   public String getAccountPassword(){
      return password;
   }

   public void reobtainAuthToken(){

   }

   // Concatenates the relative URL with the base url of our API.
   private static String getAbsoluteUrl(String relativeUrl){
      return relativeUrl;
   }

   /*
The LoginController class.

This class extends the AsyncTask to spawn off a new thread that posts login attempts to
 the web server. The HttpClientUtil class is the class that actually sends off the request using
a Synchronous Http handler.

doInBackground:
We send the login credentials to the server to check if they are valid. If they are valid, the
server should return onSuccess and from there we can instantiate the record activity view.

The onPFailure tells the user there was an incorrect username and password combo.
 */
   private class HttpClientUtilController extends AsyncTask<Void, Void, Void> {


      private final String contentType = "application/json";
      private final String token = "token";


      @Override
      protected void onPreExecute() {

         // Reset the login attempt to failing so we know if it actually passed or not.
         loginSuccessful = false;
      }



      // Send off the activity data to the server.
      @Override
      protected Void doInBackground(Void... params) {

         // Build the parameters for the activity via JSON
         // If we create a RecordActivityModel, we can use Gson to generate a JSON Object directly
         // from the RecordActivityModel object that contains the data for the Activity. We can then
         // manually add the username property to the Gson object and be done.
         // Currently we do it the old fashioned way since we dont have a model for record actiivty
         JSONObject loginJSON = null;
         StringEntity jsonString = null;
         try{

            // Convert Login info to JSON then to parameters for the post activity.
            loginJSON = createLoginJSONObject();
            jsonString = new StringEntity(loginJSON.toString());
         }
         catch(Exception ex){
            Log.i("JSON/Encode Exception:", ex.getMessage());
         }

         // Post the login attempt to the server with the given username and password.
         postByUrl(null, HttpClientUtil.BASE_URL_LOGIN, jsonString, contentType,
                 new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    // Before the actual post happens.
                    @Override
                    public void onStart() {

                    }

                    // Here you received http 200, Save off the auth token to the HttpUtils class to
                    // use in subsequent api calls and start record activity.
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                       try {

                          // parse the response to save the auth code for future api calls and
                          // save the password and username in case we need to login again.
                          JSONObject jsonResponse = new JSONObject(new String(response));
                          httpClientUtil.setAuthKeycode(jsonResponse.getString(token));
                          httpClientUtil.setAccountInfo(username, password);
                          loginSuccessful = true;

                       } catch (JSONException ex) {
                          Log.i("Networking Exception", ex.getMessage());
                          Log.i("Response: ", new String(response));
                       }


                    }

                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    // If it fails to post, you can issue some sort of alert dialog saying the error
                    // and writing the activity to file.
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {


                    }
                 });

         return null;
      }


      // This method gets executed after the doInBackground process finishes.
      @Override
      protected void onPostExecute(Void params) {
         super.onPostExecute(params);




      }

      /* Example Login POST in JSON
        {
             "username":"szook",
             "password":"somePW"
        }
      */
      // Creates a Login JSON Object the server can use to verify a user's identity.
      private JSONObject createLoginJSONObject() throws JSONException {
         JSONObject loginJSONObject = new JSONObject();

         // add the username and password to the object.
         loginJSONObject.put("username", username);
         loginJSONObject.put("password", password);

         return loginJSONObject;
      }

   }


}
