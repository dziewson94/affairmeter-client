package self.affairmeter.client.userdata.notification;

public enum TargetApps {

    WHATSAPP("whatsapp"),
    MESSENGER("orca"),
    INSTAGRAM("instagram.android"),
    TELEGRAM("telegram.messenger"),
    SNAPCHAT("com.snapchat.android"),
    CHROME("com.android.chrome"),
    SYSTEM("android");


    private String appPackage;

    TargetApps(String appName) {
        this.appPackage = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

}
