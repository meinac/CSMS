package com.csms.mehmetinac.csms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;


public class SMSListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);


        DatabaseHandler db = new DatabaseHandler(this);
        List<SMS> smsList = db.getAllSMS();

        String sms = "";

        for(int i = 0; i < smsList.size(); i++) {
            sms += "From :" + smsList.get(i).phoneNumber + " : " + smsList.get(i).message+"\n";
        }

        view.setText(sms);

        setContentView(R.layout.activity_smslist);
        ScrollView sView = (ScrollView) findViewById(R.id.scrollView);
        sView.addView(view);
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
            return true;
        }
        else if(id == R.id.action_about) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
