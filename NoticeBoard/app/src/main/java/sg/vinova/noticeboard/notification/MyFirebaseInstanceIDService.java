package sg.vinova.noticeboard.notification;



import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Utils;


/**
 * Created by Jacky on 5/31/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseIIdService";
    private String refeshedToken;

    @Override
    public void onTokenRefresh() {
      /*  if (Utils.isGooglePlayServicesAvailable(getBaseContext())){
            refeshedToken = FirebaseInstanceId.getInstance().getToken();
            CacheUtils.setDeviceToken(getBaseContext(),refeshedToken);
        }*/

    }
    private void sendRegistrationToServer(String token) {

        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
