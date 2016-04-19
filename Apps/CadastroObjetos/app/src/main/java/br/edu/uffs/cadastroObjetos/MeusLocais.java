package br.edu.uffs.cadastroObjetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MeusLocais extends ActionBarActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private int localId;
    private int creator_id;
    ListView lv;
    List<Local> locais;
    Context context;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // JSON Node names
    private static final String TAG_PID = "chave";
    private static final String TAG_NAME = "nome_local";

    Local localExcluir = new Local();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_locais);

        creator_id = (int) getIntent().getSerializableExtra("creator_id");

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        context = this;

        // Loading products in Background Thread
        new LoadAllProducts().execute();

        // Get listview
        lv = (ListView) findViewById(R.id.listLocais);

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

                // Starting new intent
                // Intent in = new Intent(getApplicationContext(), EditProductActivity.class);
                // sending pid to next activity
                // in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                // startActivityForResult(in, 100);
                localId = Integer.parseInt(pid);
                iniciaListaObjetos();
                //Toast.makeText(getApplicationContext(), pid, Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), productsList.get(position).get(TAG_PID) +"- "+ productsList.get(position).get(TAG_NAME), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MeusLocais.this);
            pDialog.setMessage("Carregando Locais. Aguarde...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            DbHelperCloud db = new DbHelperCloud(context);
            locais = db.buscaLocais(creator_id);

            if(locais != null){

                for(int i =0; i < locais.size();i++) {
                    String id = Integer.toString(locais.get(i).getId());
                    String name = locais.get(i).getNome();

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);

                    // adding HashList to ArrayList
                    productsList.add(map);

                }


            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            MeusLocais.this, productsList,
                            R.layout.minha_lista_locais, new String[] { TAG_PID, TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    lv.setAdapter(adapter);
                }
            });

        }

    }
    public void iniciaListaObjetos() {
        Intent myIntent = new Intent(MeusLocais.this, ListaObjetos.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.putExtra("localId", localId);
        MeusLocais.this.startActivity(myIntent);
    }

    public void iniciaNovoLocal(View view) {
        Intent myIntent = new Intent(MeusLocais.this, NovoLocal.class);
        Integer creator_id = (Integer) getIntent().getSerializableExtra("creator_id");
        myIntent.putExtra("creator_id", creator_id);
        MeusLocais.this.startActivity(myIntent);
    }

}