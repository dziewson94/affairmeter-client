package self.affairmeter.client.service;

import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import self.affairmeter.client.R;
import self.affairmeter.client.userdata.UserDataHolder;
import self.affairmeter.client.userdata.notification.UserNotification;

public class PostService {
    public static void executeTask(Context context) {
        String[] params = new String[1];
        params[0] = context.getString(R.string.post_notification_url);
         new PostAsyncTask().execute(params);
    }
}
