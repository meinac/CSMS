package com.csms.mehmetinac.csms;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class NewSMSActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sms);
        final TextView mTextView = (TextView) findViewById(R.id.sms_length);
        EditText mEditText = (EditText) findViewById(R.id.message);
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextView.setText(String.valueOf(88 - s.length()) + " Characters Left");
            }

            public void afterTextChanged(Editable s) {
            }
        };
        mEditText.addTextChangedListener(mTextEditorWatcher);
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
            return true;
        }
        else if(id == R.id.action_sms_list) {
            Intent intent = new Intent(this, SMSListActivity.class);
            startActivity(intent);
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

    public void sendSMS(View view){
        TextView phone = (TextView) findViewById(R.id.phoneNumber);
        TextView message = (TextView) findViewById(R.id.message);
        Toast.makeText(this.getApplicationContext(), "Sending Sms", Toast.LENGTH_LONG).show();
        SMSSender smsSender = new SMSSender(phone.getText().toString(), message.getText().toString());
        smsSender.execute();
    }
}
