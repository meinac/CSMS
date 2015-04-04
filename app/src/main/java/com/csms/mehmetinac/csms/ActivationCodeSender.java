package com.csms.mehmetinac.csms;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mehmetinac on 27/01/15.
 */
public class ActivationCodeSender extends AsyncTask {

    private String activationCode;
    private Activity activity;

    public ActivationCodeSender(String activationCode, Activity activity) {
        this.activationCode = activationCode;
        this.activity = activity;
    }

    protected Object doInBackground(Object[] params) {
        try {
            String filePath = activity.getBaseContext().getFilesDir().getPath().toString() + PrivateKey.PHONE_NUMBER_FILE_NAME;
            File file = new File(filePath);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file.getAbsoluteFile())));
            String phoneNumber = (String) ois.readObject();

            URL url = new URL("http://key-server.herokuapp.com/clients/" + phoneNumber + "/verify");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            String urlParameters = "validation_code=" + activationCode;

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();
            Log.i("Response Code is", "" + responseCode);
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.e("Network", "Couldn't send activation code to the server");
        }
        return null;
    }

    protected void onPostExecute(Object o) {
        Toast.makeText(activity.getApplicationContext(), "Activation Code Has Been Sent", Toast.LENGTH_LONG).show();
    }
}
