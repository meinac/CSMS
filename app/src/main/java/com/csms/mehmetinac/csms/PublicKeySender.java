package com.csms.mehmetinac.csms;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by mehmetinac on 26/01/15.
 */
public class PublicKeySender extends AsyncTask {

    private String phoneNumber;
    private RSAPublicKeySpec publicKey;
    private Activity activity;

    public PublicKeySender(String phoneNumber, RSAPublicKeySpec publicKey, Activity activity){
        this.phoneNumber = phoneNumber;
        this.publicKey = publicKey;
        this.activity = activity;
    }

    protected Object doInBackground(Object[] params) {
        try {
            URL url = new URL("http://key-server.herokuapp.com/clients");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            String urlParameters = "number=" + phoneNumber + "&public_key_exponent=" + publicKey.getPublicExponent() + "&public_key_modulus=" + publicKey.getModulus();

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();
            Log.i("Response Code is", "" + responseCode);
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.e("Network", "Network Fault");
        }
        return null;
    }

    protected void onPostExecute(Object o) {
        Toast.makeText(activity.getApplicationContext(), "Public Key Has Been Sent", Toast.LENGTH_LONG).show();
        super.onPostExecute(o);
    }
}
