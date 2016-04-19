package br.edu.uffs.cadastroObjetos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListaObjetos extends ActionBarActivity {
    MeuListAdapter adapter;
    Activity este;
    int localId;
    List<Objeto> objetos;
    ListView list;
    String[] nomes;
    String[] frases;
    Double[] lats;
    Double[] longs;
    byte[][] imagens;
    private ProgressDialog pDialog;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_objetos);

        localId = (int) getIntent().getSerializableExtra("localId");

        list=(ListView)findViewById(R.id.list);

        este = this;
        context = this;
        new LoadAllObjects().execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = nomes[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                //do your stuff here
                return true;
            }
        });

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllObjects extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListaObjetos.this);
            pDialog.setMessage("Carregando objetos. Aguarde...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            DbHelperCloud db = new DbHelperCloud(context);
            objetos = db.buscaObjetos(localId);

            if(objetos != null) {
                nomes = new String[objetos.size()];
                frases = new String[objetos.size()];
                lats = new Double[objetos.size()];
                longs = new Double[objetos.size()];
                imagens = new byte[objetos.size()][];
                if (objetos.size() > 0) {
                    for (int i = 0; i < objetos.size(); i++) {
                        nomes[i] = objetos.get(i).getNome();
                        frases[i] = objetos.get(i).getFrase();
                        imagens[i] = objetos.get(i).getImagem();
                        lats[i] = objetos.get(i).getLatitude();
                        longs[i] = objetos.get(i).getLongitude();
                    }
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
                    if (objetos != null) {
                        adapter = new MeuListAdapter(este, nomes, frases, imagens, lats, longs);
                        list.setAdapter(adapter);
                    }
                }
            });

        }

    }


    public void iniciaNovoObjeto(View view) {
        Intent myIntent = new Intent(ListaObjetos.this, NovoObjeto.class);
        Integer localId = (Integer) getIntent().getSerializableExtra("localId");
        myIntent.putExtra("localId", localId);
        ListaObjetos.this.startActivity(myIntent);
    }
}
