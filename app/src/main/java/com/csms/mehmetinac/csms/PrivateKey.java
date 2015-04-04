package com.csms.mehmetinac.csms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by mehmetinac on 26/01/15.
 */
public class PrivateKey {

    public static final String FILE_NAME = "/private_key";
    public static final String PHONE_NUMBER_FILE_NAME = "/phone_number";
    public static String phoneNumber;

    public static void checkKey(Activity activity){
        String filePath = activity.getBaseContext().getFilesDir().getPath().toString() + FILE_NAME;
        File file = new File(filePath);
        String str = "";
        if(file.exists()){
            str = "...Private Key File Exists...";
        } else {
            try {
                RSAPrivateKeySpec priv = generateKeys(activity);
                file.createNewFile();
                ObjectOutputStream oout = new ObjectOutputStream(
                        new BufferedOutputStream(new FileOutputStream(file.getAbsoluteFile())));

                oout.writeObject(priv.getModulus());
                oout.writeObject(priv.getPrivateExponent());
                oout.close();
            } catch (Exception e) {
                Log.e("File", e.getMessage());
            }
            str = "!!!Private File Doesn't Exist!!!";
        }
        Log.e("File", str);
    }

    private static RSAPrivateKeySpec generateKeys(Activity activity) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(790);
            KeyPair kp = kpg.genKeyPair();
            Key publicKey = kp.getPublic();
            Key privateKey = kp.getPrivate();

            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = (RSAPublicKeySpec) fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = (RSAPrivateKeySpec) fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);

            sendPublicKey(pub, activity);

            return priv;
        } catch(Exception ex) {
            return null;
        }
    }

    private static void sendPublicKey(final RSAPublicKeySpec publicKey, final Activity activity) {
        Log.e("Public Key", "Sending public key to the key server");
        if(!isNetworkAvailable(activity)){
            Log.e("Network", "Network is not available");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Please Enter Your Phone Number");

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrivateKey.phoneNumber = input.getText().toString();
                String filePath = activity.getBaseContext().getFilesDir().getPath().toString() + PHONE_NUMBER_FILE_NAME;
                File file = new File(filePath);
                try {
                    file.createNewFile();
                    ObjectOutputStream oout = new ObjectOutputStream(
                                    new BufferedOutputStream(new FileOutputStream(file.getAbsoluteFile())));
                    oout.writeObject(phoneNumber);
                    oout.close();
                } catch(Exception ex) {
                    Log.e("FILE", "Couldn't create phone number file");
                }
                Toast.makeText(activity.getApplicationContext(), "Sending Public Key", Toast.LENGTH_LONG).show();
                PublicKeySender ps = new PublicKeySender(PrivateKey.phoneNumber, publicKey, activity);
                ps.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
