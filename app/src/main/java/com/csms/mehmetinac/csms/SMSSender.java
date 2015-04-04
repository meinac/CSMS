package com.csms.mehmetinac.csms;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * Created by mehmetinac on 27/01/15.
 */
public class SMSSender  extends AsyncTask {

    private String phoneNumber;
    private String message;

    public SMSSender(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    protected Object doInBackground(Object[] params) {
        try {
            URL url = new URL("http://key-server.herokuapp.com/clients/" + phoneNumber + "/public_key");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bf.readLine()) != null) {
                sb.append(line);
            }

            bf.close();

            String jSONString = sb.toString();
            JSONObject jSON = new JSONObject(jSONString);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger((String) jSON.get("public_key_modulus")), new BigInteger((String) jSON.get("public_key_exponent")));
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherData = cipher.doFinal(message.getBytes());
            String encoded = Base64.encodeToString(cipherData, Base64.DEFAULT);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "#CSMS# " + encoded, null, null);
            return null;
        } catch(Exception ex) {
            Log.e("Getting Public Key", "Couldn't get public key");
            return null;
        }
    }
}