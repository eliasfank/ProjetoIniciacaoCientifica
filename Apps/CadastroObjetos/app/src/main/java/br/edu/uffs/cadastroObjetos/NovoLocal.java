package br.edu.uffs.cadastroObjetos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NovoLocal extends ActionBarActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    private Local local = new Local();
    private EditText et;
    private int creator_id;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_local);

        creator_id = (int) getIntent().getSerializableExtra("creator_id");

        context = this;
        // Edit Text
        et = (EditText) findViewById(R.id.novolocal);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.salvarlocal);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                local.setNome(et.getText().toString());
                local.setChave_Criador(creator_id);
                new CreateNewLocal().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewLocal extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NovoLocal.this);
            pDialog.setMessage("Salvando Local..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            DbHelperCloud db = new DbHelperCloud(context);
            boolean r = db.registrarLocal(local);
            if(r == true){
                Intent i = new Intent(getApplicationContext(), MeusLocais.class);
                i.putExtra("creator_id", creator_id);
                startActivity(i);
                // closing this screen
                finish();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}