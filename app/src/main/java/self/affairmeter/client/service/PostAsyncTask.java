package self.affairmeter.client.service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import self.affairmeter.client.userdata.UserDataHolder;
import self.affairmeter.client.userdata.notification.UserNotification;

public class PostAsyncTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        while (true) {
            List<String> notificationsJSONS = UserDataHolder.allNotifications.stream().map(UserNotification::toJSONString).collect(Collectors.toList());
            JSONArray jsonArray = new JSONArray(notificationsJSONS);
            String res = jsonArray.toString().replaceAll("\\p{C}", "?");
            System.out.println(res);
            UserDataHolder.allNotifications.clear();
            try {
                URL url = new URL(params[0]); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(res);
                wr.flush();
                wr.close();
                Log.i("POST", "STATUS: "+ httpURLConnection.getResponseCode());
                Log.i("POST", "MSG: " +httpURLConnection.getResponseMessage());

            } catch (IOException e) {
                Log.e("POST", e.getMessage());
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}