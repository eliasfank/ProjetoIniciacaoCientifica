package br.edu.uffs.cadastroObjetos;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Entrar extends ActionBarActivity implements View.OnClickListener  {

    // Progress Dialog
    private ProgressDialog pDialog;
    boolean userverified;
    Button bEntrar;
    TextView registrarL;
    EditText email, pass;
    private User user = new User();
    Context context;
    private int creator_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        context = this;

        bEntrar = (Button) findViewById(R.id.entrar);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.senha);
        registrarL = (TextView) findViewById(R.id.registrar);

        bEntrar.setOnClickListener(this);
        registrarL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.entrar:
                user.setEmail(email.getText().toString());
                user.setPass(pass.getText().toString());
                new ValidUser().execute();
                break;
            case R.id.registrar:
                Intent registerIntent = new Intent(Entrar.this, Registrar.class);
                startActivity(registerIntent);
                break;
        }
    }

    /**
     * Background Async Task to Create new product
     * */
    class ValidUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Entrar.this);
            pDialog.setMessage("Autenticando..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            DbHelperCloud db = new DbHelperCloud(context);
            creator_id = db.verificaUsuario(user);

            if(creator_id > 0)
                userverified=true;
            else
                userverified=false;

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            super.onPostExecute(file_url);
            pDialog.dismiss();
            if(userverified==true){
                logUserIn(user);
            }
            else{
                Toast.makeText(getApplicationContext(), "Credenciais inváldias...", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void logUserIn(User returnedUser) {
        // userLocalStore.storeUserData(returnedUser);
        // userLocalStore.setUserLoggedIn(true);
        Intent myIntent = new Intent(this, MeusLocais.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.putExtra("creator_id", creator_id);
        startActivity(myIntent);
    }
}