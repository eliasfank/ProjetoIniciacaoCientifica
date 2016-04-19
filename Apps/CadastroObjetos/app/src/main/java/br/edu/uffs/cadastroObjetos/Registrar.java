package br.edu.uffs.cadastroObjetos;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registrar extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    private User user = new User();
    private EditText inputEmail;
    private EditText inputPass;

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // Edit Text
        inputEmail = (EditText) findViewById(R.id.email);
        inputPass = (EditText) findViewById(R.id.senha1);

        context = this;


        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.registrar);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (inputEmail.getText().toString().trim().equals("")) {

                    inputEmail.setError("Email é necessario!");

                } else {

                    // creating new product in background thread
                    user.setEmail(inputEmail.getText().toString());
                    user.setPass(inputPass.getText().toString());
                    new CreateNewUser().execute();
                }
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registrar.this);
            pDialog.setMessage("Criando Usuário..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            DbHelperCloud db = new DbHelperCloud(context);
            boolean r = db.registrarUsuario(user);
            if(r == true){
                Intent i = new Intent(getApplicationContext(), Entrar.class);
                startActivity(i);
                // closing this screen
                finish();
            }else{
                showErrorMessage();
            }
            return null;
        }

        private void showErrorMessage() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Registrar.this);
            dialogBuilder.setMessage("Algo deu errado!");
            dialogBuilder.setPositiveButton("Ok", null);
            dialogBuilder.show();
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
