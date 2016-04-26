package br.edu.uffs.cc.userapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfigIp extends Activity {

    EditText editText1;
    Button buttonSaveMem1;
    String strSavedMem1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_ip);

        editText1 = (EditText)findViewById(R.id.edittext1);

        buttonSaveMem1 = (Button)findViewById(R.id.save_mem1);

        buttonSaveMem1.setOnClickListener(buttonSaveMem1OnClickListener);

        LoadPreferences();

        editText1.setText(strSavedMem1, TextView.BufferType.EDITABLE);
    }

    Button.OnClickListener buttonSaveMem1OnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            SavePreferences("MEM1", editText1.getText().toString());
            LoadPreferences();
            iniciarApp();
        }

    };

    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("PrefsFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("PrefsFile",MODE_PRIVATE);
        strSavedMem1 = sharedPreferences.getString("MEM1", "");

    }
    public void iniciarApp() {
        Intent myIntent = new Intent(ConfigIp.this, CameraActivity.class);
        myIntent.putExtra("IP", strSavedMem1);
        ConfigIp.this.startActivity(myIntent);
    }
}
