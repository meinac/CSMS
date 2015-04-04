package com.csms.mehmetinac.csms;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrivateKey.checkKey(this);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_new_sms) {
            Intent intent = new Intent(this, NewSMSActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_sms_list) {
            Intent intent = new Intent(this, SMSListActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_about) {
            return true;
        }
        else if(id == R.id.action_activation_code) {
            Intent intent = new Intent(this, ActivationCodeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
