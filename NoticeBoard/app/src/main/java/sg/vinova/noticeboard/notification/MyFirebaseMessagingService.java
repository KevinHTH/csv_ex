package sg.vinova.noticeboard.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;
import sg.vinova.noticeboard.model.DataNotification;
import sg.vinova.noticeboard.ui.activity.MainActivity;
import sg.vinova.noticeboard.utils.Log;

/**
 * Created by Jacky on 5/31/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        ArrayMap<String, String> data = (ArrayMap<String, String>) remoteMessage.getData();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data.valueAt(0));
            String numberCategory = jsonObject.getString("number_category");
            String userId = jsonObject.getString("user_id");
            sendNotification(numberCategory);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String messageBody) {
      /*  Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notice board have new post!")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());*/


        ShortcutBadger.applyCount(getApplicationContext(), Integer.parseInt(messageBody));
    }


}
