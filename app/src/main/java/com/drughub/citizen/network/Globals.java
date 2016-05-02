package com.drughub.citizen.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.drughub.citizen.MyApplication;
import com.drughub.citizen.model.DoctorClinic;
import com.drughub.citizen.model.User;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;


public class Globals {
    private static final String TAG = "GLOBALS";
    private static final String VOLLEY_TAG = "VOLLEY_GLOBALS";
    public static Drawable drawable;
    public static int MY_SOCKET_TIMEOUT_MS = 15000;
    public static Typeface typeFace, lightTypeFace;
    public static Double latitude = 0.0, longitude = 0.0;
    public static boolean sendLocation = false;
    public static User userProfile;
    static String stringResponse = null;
    static Bitmap responseBitmap;
    private static ImageLoader mImageLoader;
    private static Context mContext;
    private static RequestQueue mRequestQueue;
    public static DoctorClinic selectedDoctorClinic;
    public static final String COOKIES_HEADER = "Set-Cookie";
    private static CookieManager mCookieManager = null;
    private static final boolean USE_VOLLEY = false;
    public static String className = "";

    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE,
        PATCH,
    }

    public static void init(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static void startStringRequest(RequestMethod method, final String url, final Map<String, String> headers, final Map<String, String> params,
                                          final String body, final String progressText, final VolleyCallback callback) {
        Log.d(VOLLEY_TAG, "WebService URL: " + method + " " + url);

        if(USE_VOLLEY) {
            final ProgressDialog progress = (progressText != null) ? ProgressDialog.show(mContext, progressText, "Please wait...", true) : null;

            StringRequest stringRequest = new StringRequest(method.ordinal(), url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.v(VOLLEY_TAG, "onResponse(): " + response);
                    stringResponse = response;

                    if (progress != null)
                        progress.dismiss();

                    if (callback != null)
                        callback.onSuccess(stringResponse);

                    try {
                        JSONObject object = new JSONObject(stringResponse);
                        if (!object.getBoolean("result"))
                            Toast.makeText(mContext, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    try {
                        if (progress != null)
                            progress.dismiss();

                        Log.v(VOLLEY_TAG, "URL: " + url);
                        Log.v(VOLLEY_TAG, "onErrorResponse(): " + volleyError);

                        if (volleyError.networkResponse != null)
                            Log.v(VOLLEY_TAG, "onErrorResponse(): StatusCode: " + volleyError.networkResponse.statusCode);

                        if (volleyError.networkResponse == null)
                            stringResponse = "Unable to process your request, please try again.";
                        else
                            stringResponse = getResponseCodeError(volleyError.networkResponse.statusCode);


                        Toast.makeText(mContext, stringResponse, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        stringResponse = "Unable to process your request, please try again.";
                        Log.v(VOLLEY_TAG, "onErrorResponse(): Exception: " + e);
                        e.printStackTrace();
                    }
                    if (callback != null)
                        callback.onFail(stringResponse);
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> localHeaders = headers;

                    if (localHeaders == null)
                        localHeaders = super.getHeaders();

                    if (localHeaders == null
                            || localHeaders.equals(Collections.emptyMap())) {
                        localHeaders = new HashMap<String, String>();
                    }
                    MyApplication.get().addSessionCookie(localHeaders);

                    localHeaders.put("Content-Type", "application/json");

                    Log.v(VOLLEY_TAG, "URL: " + url);
                    Log.v(VOLLEY_TAG, "getHeaders(): " + localHeaders.toString());

                    return localHeaders;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> localParams = params;

                    if (localParams == null)
                        localParams = super.getParams();

                    Log.v(VOLLEY_TAG, "getParams(): " + localParams);

                    return localParams;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {

                    String localBody = body;

                    if (localBody == null || localBody.isEmpty())
                        return super.getBody();

                    Log.v(VOLLEY_TAG, "getBody(): " + localBody);

                    return localBody.getBytes();
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    MyApplication.get().checkSessionCookie(response.headers);

                    Log.v(VOLLEY_TAG, "URL: " + url);
                    Log.v(VOLLEY_TAG, "parseNetworkResponse(): StatusCode: " + response.statusCode);
                    Log.v(VOLLEY_TAG, "parseNetworkResponse(): Data: " + response.data.toString());

                    return super.parseNetworkResponse(response);
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addRequestQueue(stringRequest);
        }
        else
        {
            WebService service = new WebService(method, url, body, callback, progressText);
            service.execute();
        }
    }

    public static String requestMethod(RequestMethod method)
    {
        switch (method)
        {
            case GET:
                return "GET";
            case POST:
                return "POST";
            case PUT:
                return "PUT";
            case PATCH:
                return "PATCH";
            case DELETE:
                return "DELETE";
        }
        return "INVALID";
    }

    public static class WebService extends AsyncTask<Void, Void, Boolean> {

        RequestMethod method;
        String url = null;
        String body = null;
        VolleyCallback callback = null;
        String progressText = null;
        String response = null;
        ProgressDialog progress = null;

        public WebService(RequestMethod method, String url, String body, VolleyCallback callback, String progressText) {
            this.method = method;
            this.url = url;
            this.body = body;
            this.callback = callback;
            this.progressText = progressText;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(!isOnline(mContext)) {
                response = "No Internet Connectivity";
                return false;
            }

            if(mCookieManager == null)
                mCookieManager = new CookieManager();

            HttpURLConnection conn = null;
            response = null;

            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setReadTimeout(10000 * 60);
                conn.setConnectTimeout(15000 * 60);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                if(mCookieManager.getCookieStore().getCookies().size() > 0) {
                    //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                    conn.setRequestProperty("Cookie",
                            TextUtils.join(";", mCookieManager.getCookieStore().getCookies()));
                }

//                if(method == RequestMethod.DELETE && body != null) {
//                    conn.setRequestProperty("X-HTTP-Method-Override", "DELETE");
//                    conn.setRequestMethod("POST");
//                }
//                else
                conn.setRequestMethod(requestMethod(method));

                conn.setDoInput(true);

                if(method == RequestMethod.GET || method == RequestMethod.DELETE)//(method == RequestMethod.DELETE && body == null))
                    conn.setDoOutput(false);
                else
                    conn.setDoOutput(true);

                if(body != null) {
                    Log.d(VOLLEY_TAG, "WebService method: " + method + " Url: " + url);
                    Log.d(VOLLEY_TAG, "WebService Request: " + body);
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(body);
                    writer.flush();
                    writer.close();
                }
                // Start the query
                conn.connect();

                int statusCode = conn.getResponseCode();
                if(statusCode != HttpURLConnection.HTTP_OK) {
                    Log.d(VOLLEY_TAG, "WebService ErrorCode: "+statusCode);
                    conn.disconnect();
                    response = getResponseCodeError(statusCode);
                    return false;
                }

                //InputStream errorStream = conn.getErrorStream();

                //read output
                InputStream stream = conn.getInputStream();

                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                if(cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        mCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }

                Scanner s = new Scanner(stream);
                response = s.hasNextLine() ? s.nextLine() : "";
                Log.d(VOLLEY_TAG, "WebService method: " + method + " Url: " + url);
                Log.d(VOLLEY_TAG, "WebService Response: "+response);

            } catch (Exception e) {
                Log.d(VOLLEY_TAG, "WebService method: " + method + " Url: " + url);
                Log.d(VOLLEY_TAG, "WebService Exception: "+e);
                e.printStackTrace();
            }

            if (conn != null)
                conn.disconnect();

            if(response == null) {
                response = "Unable to process your request, please try again.";
                return false;
            }

            return true;
        }

        @Override
        protected  void onPreExecute() {
            progress = (progressText != null) ? ProgressDialog.show(mContext, progressText, "Please wait...", true) : null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("result"))
                        callback.onSuccess(response);
                    else {
                        Toast.makeText(mContext, object.getString("errorCode") + ": " + object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        callback.onFail(response);
                    }

                } catch (Exception e) { e.printStackTrace(); }
            }
            else {
                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                callback.onFail(response);
            }

            try {
                if (progress != null)
                    progress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Exception in code", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
            callback.onFail(response);
            if(progress != null)
                progress.dismiss();
        }
    }

    public static String getResponseCodeError(int errorCode){
        String stringResponse;
        if (errorCode == 0)
            stringResponse = "Unable to process your request, please try again.";
        else if (errorCode == 400)
            stringResponse = "Bad Request";
        else if (errorCode == 401)
            stringResponse = "Session Timed Out";
        else if (errorCode == 403)
            stringResponse = "Forbidden Request";
        else if (errorCode == 404)
            stringResponse = "URL Not Found";
        else if (errorCode == 500)
            stringResponse = "Internal Server Error";
        else
            stringResponse = "Unable to process your request, please try again.";

        return stringResponse;
    }

    /*GET METHOD REQUEST FOR API*/
    public static void GET(String url, VolleyCallback callback, String progressText) {
        startStringRequest(RequestMethod.GET, url, null, null, null, progressText, callback);
    }

    /*POST METHOD REQUEST FOR API*/
    public static void POST(String url, String body, VolleyCallback callback, String progressText) {
        startStringRequest(RequestMethod.POST, url, null, null, body, progressText, callback);
    }

    /*PUT METHOD REQUEST FOR API*/
    public static void PUT(String url, String body, VolleyCallback callback, String progressText) {
        startStringRequest(RequestMethod.PUT, url, null, null, body, progressText, callback);
    }

    /*PATCH METHOD REQUEST FOR API*/
    public static void PATCH(String url, String body, VolleyCallback callback, String progressText) {
        startStringRequest(RequestMethod.PATCH, url, null, null, body, progressText, callback);
    }

    /*PUT METHOD REQUEST FOR API*/
    public static void DELETE(String url, String body, VolleyCallback callback, String progressText) {
        startStringRequest(RequestMethod.DELETE, url, null, null, body, progressText, callback);
    }

    /*IMAGE REQUEST FOR API*/
    public static Bitmap IMAGE(String url, final HashMap<String, String> headers, int width, int height, final VolleyImageCallback callback) {
        getRequestQueue();
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                responseBitmap = bitmap;
                callback.onSuccess(bitmap);

            }
        }, width, height, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    stringResponse = getResponseCodeError(volleyError.networkResponse.statusCode);
                    callback.onFail(stringResponse);
                } catch (Exception e) {
                    stringResponse = "error occured";
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequestQueue(imageRequest);
        return responseBitmap;
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mContext);

        return mRequestQueue;

    }

    public static void addRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    /*Method For ImageLoader*/
    public static ImageLoader getImageLoader() {
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruBitmapCache();

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
        return mImageLoader;
    }

    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    public static Typeface typefaceFor(Context context, String typeface) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/" + typeface);
    }

    public static String encryptString(String value) {
        String md5 = null;
//        value = salt.concat(value);
        if (null == value) return null;
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(value.getBytes(), 0, value.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            // make sure "HTC" is fully capitalized.
            return "HTC " + model;
        }
        return manufacturer + " " + model;
    }

//        public static Bitmap customMarker(Context context, Bitmap bmp1) {
//        Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pin);
//        try {
//
//            Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight(), bmp1.getConfig());
//            Canvas canvas = new Canvas(bmOverlay);
//            canvas.drawBitmap(bmp1, 12, 10, null);
//            canvas.drawBitmap(bmp2, 0, 0, null);
//            return bmOverlay;
//
//        } catch (Exception e) {
//            return bmp2;
//        }
//    }

    public final static boolean isValidEmail(String email) {
        if (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;

        return false;
    }

//    public static void addDevice(Context context){
//        HashMap<String, String> postParams = new HashMap<>();
//        HashMap<String, String> postHeaders = new HashMap<>();
//        postHeaders.put("token",Globals.userProfile.token);
//
//        postParams.put("push_token", ZeroPush.getInstance().getDeviceToken());
//        postParams.put("model", getDeviceName());
//        postParams.put("os_version", Build.VERSION.RELEASE);
//        postParams.put("os_name","android");
//        Log.v("device params"," "+postParams);
//
//        Globals.POST(Globals.urlFor(context, "device"), postHeaders, postParams, "", new VolleyCallback() {
//            @Override
//            public void onSuccess(String result) {
//                Log.v("result", " " + result);
//            }
//
//            @Override
//            public void onFail(String result) {
//                Log.v("result error"," "+result);


//            }
//        });
//    }

    public static void getCountries(final VolleyCallback callback) {
        GET(Urls.COUNTRY, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "");
    }

    public static void getState(int id, final VolleyCallback callback) {
        GET(Urls.STATE + id, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "");
    }

    public static void getCity(int id, final VolleyCallback callback) {
        GET(Urls.CITY + id, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "");
    }

    public interface VolleyCallback {
        void onSuccess(String result);

        void onFail(String result);
    }

    public interface VolleyImageCallback {
        void onSuccess(Bitmap bitmap);

        void onFail(String result);
    }
    public static void getSpecialization(final VolleyCallback callback){
        GET(Urls.GET_SPECIALIZATION, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
                Log.v("result===", result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "");
    }
    public static void getQualification(final VolleyCallback callback){
        GET(Urls.GET_QUALIFICATION, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
                Log.v("resultQL===",result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "");
    }

    public static int getHour(String time)
    {
        if(time == null)
            return 12;
        String hour = time.substring(0, 2);
        return Integer.parseInt(hour);
    }

    public static int getMinute(String time)
    {
        if(time == null)
            return 0;
        String minute = time.substring(3, 5);
        return Integer.parseInt(minute);
    }

    public static String getMeridian(String time)
    {
        if(time == null)
            return "AM";
        return time.substring(6, 8);
    }

    public static String getCurrentDayOfWeek()
    {
        return new SimpleDateFormat("EEEE").format(new Date());
    }

    public static String convertDateFormat(String dateStr, String fromFormatStr, String toFormatStr)
    {
        SimpleDateFormat fromFormat = new SimpleDateFormat(fromFormatStr, Locale.getDefault());
        SimpleDateFormat toFormat = new SimpleDateFormat(toFormatStr, Locale.getDefault());

        try {
            Date date = fromFormat.parse(dateStr);
            return toFormat.format(date);
        } catch (Exception e) {}

        return toFormatStr;
    }

    public static Date getDateFromString(String dateStr, String dateFormatStr)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr, Locale.getDefault());

        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {}

        return null;
    }

    public static String getStringFromDate(Date date, String dateFormatStr)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr, Locale.getDefault());

        try {
            return dateFormat.format(date);
        } catch (Exception e) {}

        return null;
    }

    static SimpleDateFormat format12Hour = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    static SimpleDateFormat format24Hour = new SimpleDateFormat("kk:mm:ss", Locale.getDefault());

    public static String to12HourFormat(String time)
    {
        try {
            Date date = format24Hour.parse(time);
            return format12Hour.format(date);
        } catch (Exception e) {}


        return "00:00 AM";
    }

    public static String to24HourFormat(String time)
    {
        try {
            Date date = format12Hour.parse(time);
            return format24Hour.format(date);
        } catch (Exception e) {}

        return "00:00:00";
    }

    public final static boolean isValidPhoneNumber(String mobile ){
        if (mobile!=null && Patterns.PHONE.matcher(mobile).matches())
            return true;
        return false;
    }

}
