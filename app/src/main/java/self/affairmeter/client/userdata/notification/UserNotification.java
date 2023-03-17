package self.affairmeter.client.userdata.notification;

public class UserNotification {


    private int userId;
    private String message;
    private String title;
    private String packageName;
    private String time;

    private int id;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private String appName;

    public UserNotification() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public UserNotification(String message, String title, String packageName, String time, int id, String appName) {
        this.message = message;
        this.title = title;
        this.packageName = packageName;
        this.time = time;
        this.id = id;
        this.appName = appName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "{\"UserNotification\":{"
                + "\"message\":\"" + message + "\""
                + ", \"title\":\"" + title + "\""
                + ", \"packageName\":\"" + packageName + "\""
                + ", \"time\":\"" + time + "\""
                + ", \"id\":\"" + id + "\""
                + ", \"appName\":\"" + appName + "\""
                + "}}";
    }


    public String toJSONString() {
        return "{"
                + "\"message\":\"" + message + "\""
                + ", \"title\":\"" + title + "\""
                + ", \"packageName\":\"" + packageName + "\""
                + ", \"time\":\"" + time + "\""
                + ", \"id\":\"" + id + "\""
                + ", \"appName\":\"" + appName + "\""
                + "}";
    }
}
