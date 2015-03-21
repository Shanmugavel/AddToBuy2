package infosys.com.addtobuy;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shanmugavelsundaramoorthy on 3/21/15.
 */
public abstract class AbstractGetNameTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "MyApp";
    protected Login mActivity;
    public static String GOOGLE_USER_DATA="No_data";
    protected String mScope;
    protected String mEmail;
    protected int mRequestCode;

    AbstractGetNameTask(Login activity, String email, String scope) {
        this.mActivity = activity; this.mScope = scope; this.mEmail = email;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            fetchNameFromProfileServer();
        } catch (IOException ex) {
            onError("Following Error occured, please try again. " + ex.getMessage(), ex);
        } catch (JSONException e) {
            onError("Bad response: " + e.getMessage(), e);
        }
        return null;

    }

    private void fetchNameFromProfileServer() throws IOException, JSONException {
        String token = fetchToken();
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+ token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();

        if (sc == 200) {
            InputStream is = con.getInputStream();
            GOOGLE_USER_DATA = readResponse(is);
            is.close();

            Intent intent=new Intent(mActivity,DashboardActivity.class);
            intent.putExtra("email_id", mEmail);
            mActivity.startActivity(intent);
            //mActivity.finish();
            return;
        } else if (sc == 401) {
            GoogleAuthUtil.invalidateToken(mActivity, token);
            onError("Server auth error, please try again.", null);
            return;
        } else {
            onError("Server returned the following error code: " + sc, null);
            return;

        }
    }

    protected void onError(String msg, Exception e) {
        if (e != null) {
            Log.e(TAG, "Exception: ", e);
        }
    }

    protected abstract String fetchToken() throws IOException;

    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while ((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }

        return new String(bos.toByteArray(), "UTF-8");
    }


}
