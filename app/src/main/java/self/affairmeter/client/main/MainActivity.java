package self.affairmeter.client.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.security.SecurityPermission;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getStoragePermission = new Intent();
                getStoragePermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getStoragePermission);
            }
        }
        System.out.println(NotificationManagerCompat.getEnabledListenerPackages(this.getApplicationContext()));
        if(!doesAppHasPermission(this.getApplicationContext(),"enabled_notification_listeners")) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
        if(!doesAppHasPermission(this.getApplicationContext(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }
    }


    private boolean doesAppHasPermission(Context c, String permissionName){
        String pkgName = c.getPackageName();
        final String flat = Settings.Secure.getString(c.getContentResolver(),
                permissionName);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
