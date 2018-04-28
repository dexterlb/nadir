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
import java.util.ArrayList;
import java.util.List;

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

        if (!sync_url.contains("#")) {
            return;
        }

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

    public interface OnGetListener {
        void onGotItems(Iterable<SyncItem> items);
        void onFail(String error);
    }

    private class RawSyncItem implements SyncItem {
        private Long lastUpdate;

        public void setLastUpdate(Long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public void setUniqueID(String uniqueID) {
            this.uniqueID = uniqueID;
        }

        public JSONObject getData() {
            return data;
        }

        public void setData(JSONObject data) {
            this.data = data;
        }

        private String uniqueID;
        private JSONObject data;

        @Override
        public Long getLastUpdate() {
            return lastUpdate;
        }

        @Override
        public String getUniqueID() {
            return uniqueID;
        }

        @Override
        public JSONObject jsonify() throws JSONException {
            return getData();
        }
    }

    public void Get(Long lastUpdate, final OnGetListener onGetListener) {
        try {
            JSONObject request = new JSONObject();
            JSONArray spaces = new JSONArray();
            spaces.put(syncSpace);
            request.put("spaces", syncSpace);
            request.put("last_change", lastUpdate);

            postRequest(serverUrl + "/get", request.toString(), new OnResponseListener() {
                @Override
                public void onResponse(String responseBody) {
                    try {
                        JSONObject response = new JSONObject(responseBody);
                        JSONArray records = response.getJSONArray("records");
                        List<SyncItem> items = new ArrayList<SyncItem>();
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject record = records.getJSONObject(i);
                            RawSyncItem item = new RawSyncItem();
                            item.setLastUpdate(record.getLong("last_change"));
                            item.setUniqueID(record.getString("key"));
                            item.setData(record.getJSONObject("data"));
                            items.add(item);
                        }

                        onGetListener.onGotItems(items);
                    } catch (JSONException e) {
                        onGetListener.onFail("Unable to decode server response: " + responseBody);
                    }
                }

                @Override
                public void onFail(String error) {
                    onGetListener.onFail("HTTP error: " + error);
                }
            });
        } catch(JSONException e) {
            e.printStackTrace();
            onGetListener.onFail("Unable to encode request.");
        }
    }

    public void Push(Iterable<SyncItem> items, final OnSuccessFailListener successFailListener) {
        try {
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

            postRequest(serverUrl + "/push", request.toString(), new OnResponseListener() {
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
        } catch(JSONException e) {
            e.printStackTrace();
            successFailListener.onFail("Unable to encode request.");
        }
    }

    private interface OnResponseListener {
        void onResponse(String response);
        void onFail(String error);
    }

    private void postRequest(final String url, final String body, final OnResponseListener onResponseListener) {
        Log.v("HTTP", "request to [" + url + "]: " + body);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
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
