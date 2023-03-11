package self.affairmeter.client.userdata;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import self.affairmeter.client.userdata.notification.UserNotification;

public class UserDataHolder {
    public static List<UserNotification> allNotifications = new ArrayList<>();
    public static List<UserNotification> filteredNotifications = new ArrayList<>();
    public static Map<String, File> notificationsIcons = new HashMap<>();
}
