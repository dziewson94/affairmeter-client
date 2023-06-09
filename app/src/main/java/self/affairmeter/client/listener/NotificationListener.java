package self.affairmeter.client.listener;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import self.affairmeter.client.R;
import self.affairmeter.client.service.PostService;
import self.affairmeter.client.userdata.UserDataHolder;
import self.affairmeter.client.userdata.notification.TargetApps;
import self.affairmeter.client.userdata.notification.UserNotification;

public class NotificationListener extends NotificationListenerService {


    public String foregroundApp = "";
    private Timer timer;
    public String mAccountName;

    public NotificationListener() {
        // PostService.executeTask(getApplicationContext());
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        UserNotification thisNotification = parseUserNotification(sbn);
        if (thisNotification == null) return;
        UserDataHolder.allNotifications.add(thisNotification);
        if (!thisNotification.getAppName().equals(getString(R.string.no_target_app))) {
            UserDataHolder.filteredNotifications.add(thisNotification);

        }
        getNotificationIcon(sbn);
        PostService.executeTask(getApplicationContext());
        super.onNotificationPosted(sbn);
    }

    private UserNotification parseUserNotification(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        if ((sbn.getNotification().flags & Notification.FLAG_GROUP_SUMMARY) != 0) {
            Log.d("error", "Ignore the notification FLAG_GROUP_SUMMARY");
            return null;
        }
        String title = "";
        String message = "";
        try {
            if (extras.get("android.title") instanceof String) {
                title = extras.getString("android.title");
            }
            if (extras.get("android.title") instanceof SpannableString) {
                title = extras.get("android.title").toString();
            }
            message = extras.getCharSequence("android.text").toString();
        } catch (Exception e) {
            Log.i("exception", e.getMessage());
        }

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        UserNotification thisNotification = new UserNotification(message, title, sbn.getPackageName(), time, sbn.getId(), appNameFromPackage(sbn.getPackageName()));
        return thisNotification;
    }

    private void getNotificationIcon(StatusBarNotification sbn) {
        int iconId = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            iconId = sbn.getNotification().getSmallIcon().getResId();
        } else {
            iconId = sbn.getNotification().extras.getInt("android.icon");

        }


        try {
            Context appCon = createPackageContext(sbn.getPackageName(), CONTEXT_IGNORE_SECURITY);
            Drawable drawable = appCon.getDrawable(iconId);
            Bitmap bitmap = null;
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + sbn.getPackageName().replaceAll("\\.", "_") + ".jpg");
            file.createNewFile();
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));

            if (drawable instanceof VectorDrawable) {
                bitmap = getBitmap((VectorDrawable) drawable);
            } else if(drawable instanceof BitmapDrawable){
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }else  {
                Log.d("ICON",sbn.getPackageName());
                Log.d("ICON",drawable.getClass().toString());
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            UserDataHolder.notificationsIcons.put(sbn.getPackageName(), file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private String appNameFromPackage(String packageName) {

        for (TargetApps app : TargetApps.values()) {
            if (packageName.endsWith(app.getAppPackage())) {
                return app.name();
            }
        }
        return getString(R.string.no_target_app);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onListenerDisconnected() {

        super.onListenerDisconnected();
    }
}
