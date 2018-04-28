package org.qtrp.nadir.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qtrp.nadir.Database.FilmRollDbHelper;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyncHelper {
    private final FilmRollDbHelper filmRollDbHelper;
    private String serverUrl = "";
    private String syncSpace = "";

    private Context context;

    public interface SyncItem {
        Long getLastUpdate();
        String getUniqueID();
        JSONObject jsonify() throws JSONException;
    }

    public SyncHelper(Context context) {
        this.context = context;
        this.filmRollDbHelper = new FilmRollDbHelper(context);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sync_url = prefs.getString("te_sync_url", "");

        String server_url = sync_url.substring(0, sync_url.indexOf('#'));
        String sync_space = sync_url.substring(sync_url.indexOf('#') + 1);

        if (!server_url.isEmpty() && !sync_space.isEmpty()) {
            this.serverUrl = server_url;
            this.syncSpace = sync_space;
        }
    }

    public interface OnSuccessFailListener {
        void onSuccess();
        void onFail(String error);
    }

    public void Get(Long lastUpdate)
    public void Push(Iterable<SyncItem> items, final OnSuccessFailListener successFailListener) throws JSONException {
        JSONArray records = new JSONArray();
        for (SyncItem item: items) {
            JSONObject record = new JSONObject();
            record.put("space", syncSpace);
            record.put("key", item.getUniqueID());
            record.put("last_change", item.getLastUpdate());
            record.put("data", item.jsonify());

            records.put(record);
        }

        JSONObject request = new JSONObject();
        request.put("records", records);

        postRequest(request.toString(), new OnResponseListener() {
            @Override
            public void onResponse(String response) {
                Log.i("SYNC", "push returned: " + response);
                if (response == "\"ok\"") {
                    successFailListener.onSuccess();
                } else {
                    successFailListener.onFail("Unable to decode server response: " + response);
                }
            }

            @Override
            public void onFail(String error) {
                successFailListener.onFail(error);
            }
        });
    }

    private interface OnResponseListener {
        void onResponse(String response);
        void onFail(String error);
    }

    private void postRequest(final String body, final OnResponseListener onResponseListener) {
        Log.v("HTTP", "request to [" + serverUrl + "]: " + body);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(serverUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(body);

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    if (conn.getResponseCode() == 200) {
                        onResponseListener.onResponse(conn.getResponseMessage());
                    } else {
                        onResponseListener.onFail("HTTP error: " + ((Integer)conn.getResponseCode()).toString());
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    onResponseListener.onFail("HTTP exception.");
                }
            }
        });
    }
}
