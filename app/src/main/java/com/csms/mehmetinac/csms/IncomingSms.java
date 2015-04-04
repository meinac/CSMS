package com.csms.mehmetinac.csms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.spec.RSAPrivateKeySpec;

import javax.crypto.Cipher;

public class IncomingSms extends BroadcastReceiver {
    public IncomingSms() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        abortBroadcast();
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    if(currentMessage.getDisplayMessageBody().substring(0, 6).equals("#CSMS#")) {
                        String senderNum = phoneNumber;
                        File file = new File(context.getFilesDir().getPath().toString() + PrivateKey.FILE_NAME);
                        ObjectInputStream oin =
                                new ObjectInputStream(new BufferedInputStream(new FileInputStream(file.getAbsoluteFile())));

                        BigInteger modulus = (BigInteger) oin.readObject();
                        BigInteger exponent = (BigInteger) oin.readObject();
                        oin.close();

                        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
                        KeyFactory fact = KeyFactory.getInstance("RSA");
                        java.security.PrivateKey privateKey = fact.generatePrivate(keySpec);

                        Cipher cipher = Cipher.getInstance("RSA");
                        cipher.init(Cipher.DECRYPT_MODE, privateKey);
                        String tmp = currentMessage.getDisplayMessageBody();
                        byte[] cipherData = cipher.doFinal(Base64.decode(tmp.substring(7), Base64.DEFAULT));

                        String message = new String(cipherData);

                        DatabaseHandler db = new DatabaseHandler(context);
                        SMS sms = new SMS(senderNum, message);
                        db.addSMS(sms);

                        Toast.makeText(context, "senderNum: " + senderNum + ", message: " + message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch(Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e.getMessage());
        }
    }
}
