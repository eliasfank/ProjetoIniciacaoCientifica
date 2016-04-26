package br.edu.uffs.cadastroObjetos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfigIp extends ActionBarActivity {

    EditText editText;
    Button buttonSaveMem;
    String strSavedMem;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_ip);

        editText = (EditText)findViewById(R.id.edittext1);

        buttonSaveMem = (Button)findViewById(R.id.save_mem1);

        buttonSaveMem.setOnClickListener(buttonSaveMem1OnClickListener);

        LoadPreferences();

        editText.setText(strSavedMem, TextView.BufferType.EDITABLE);
    }

    Button.OnClickListener buttonSaveMem1OnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            SavePreferences("MEM1", editText.getText().toString());
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
        strSavedMem = sharedPreferences.getString("MEM1", "");
    }

    public void iniciarApp() {
        Intent myIntent = new Intent(ConfigIp.this, Entrar.class);
        myIntent.putExtra("IP", strSavedMem);
        ConfigIp.this.startActivity(myIntent);
    }
}