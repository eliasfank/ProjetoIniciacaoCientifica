package br.edu.uffs.cadastroObjetos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class DbHelperCloud {
    // JSON Node names
    ProgressDialog progressDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OBJETOS = "objetos";
    private static final String TAG_PRODUCTS = "locais";
    private static final String TAG_PID = "chave";
    private static final String TAG_NAME = "nome_local";
    private String url;

    JSONParser jsonParser = new JSONParser();

    public DbHelperCloud(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PrefsFile", 0);
        url = "http://"+sharedPreferences.getString("MEM1", "")+"/CadastroObjetos/";
    }

    public boolean registrarUsuario(User user) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", user.getEmail()));
        params.add(new BasicNameValuePair("pass", user.getPass()));

        // getting JSON Object
        // Note that create product url accepts POST method
        String url_create_user = url+"user_create.php";
        JSONObject json = jsonParser.makeHttpRequest(url_create_user, "POST", params);

        // check log cat fro response
        Log.d("Create Response", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created user

                return true;
            } else {
                // failed to create user
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean registrarLocal(Local local) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("nome_local", local.getNome()));
        params.add(new BasicNameValuePair("creator_id", Integer.toString(local.getChave_Criador())));

        // getting JSON Object
        // Note that create product url accepts POST method
        String url_create_local = url+"local_create.php";
        JSONObject json = jsonParser.makeHttpRequest(url_create_local, "POST", params);


        // check log cat fro response
        Log.d("Create Response", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created user

                return true;
            } else {
                // failed to create user
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insereObjeto(Objeto o) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("nome", o.getNome()));
        params.add(new BasicNameValuePair("frase", o.getFrase()));
        params.add(new BasicNameValuePair("local_id", Integer.toString(o.getLocal())));
        params.add(new BasicNameValuePair("latitude", Double.toString(o.getLatitude())));
        params.add(new BasicNameValuePair("longitude", Double.toString(o.getLongitude())));
        params.add(new BasicNameValuePair("imagem", Base64.encodeToString(o.getImagem(), Base64.DEFAULT)));
        // getting JSON Object
        // Note that create product url accepts POST method
        String url_create_objeto = url+"create_objeto.php";
        JSONObject json = jsonParser.makeHttpRequest(url_create_objeto, "POST", params);

        // check log cat fro response
        Log.d("Create Response Novo", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created user

                return true;
            } else {
                // failed to create user
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Objeto> buscaObjetos(int localId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("local_id", Integer.toString(localId)));
        // getting JSON string from URL
        String url_all_objetos = url+"get_objetos.php";
        JSONObject json = jsonParser.makeHttpRequest(url_all_objetos, "POST", params);

        // Check your log cat for JSON reponse
        Log.d("All Objetos: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);
            JSONArray products;
            List<Objeto> listObjetos = new ArrayList<>();
            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray(TAG_OBJETOS);

                // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    Objeto o = new Objeto();
                    // Storing each json item in variable

                    try {
                        o.setNome(URLDecoder.decode(c.getString("nome_objeto"), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        o.setFrase(URLDecoder.decode(c.getString("frase"), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    o.setLatitude(Double.parseDouble(c.getString("latitude")));
                    o.setLongitude(Double.parseDouble(c.getString("longitude")));
                    o.setImagem(Base64.decode(c.getString("imagem"), Base64.DEFAULT));

                    listObjetos.add(o);
                }
                return listObjetos;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Local> buscaLocais(int creatorId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("creator_id", Integer.toString(creatorId)));
        // getting JSON string from URL
        String url_all_objetos = url+"get_locais.php";
        JSONObject json = jsonParser.makeHttpRequest(url_all_objetos, "POST", params);

        // Check your log cat for JSON reponse
        Log.d("All Objetos: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);
            JSONArray products;
            List<Local> listLocais = new ArrayList<>();
            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray(TAG_PRODUCTS);

                // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    Local l = new Local();

                    // Storing each json item in variable
                    l.setId(Integer.parseInt(c.getString(TAG_PID)));
                    l.setNome(c.getString(TAG_NAME));


                    listLocais.add(l);
                }
                return listLocais;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int verificaUsuario(User user){

        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", user.getEmail()));
        params.add(new BasicNameValuePair("pass", user.getPass()));

        // getting JSON Object
        // Note that create product url accepts POST method
        String url_valid_user = url+"user_valid.php";
        JSONObject json = jsonParser.makeHttpRequest(url_valid_user, "POST", params);

        // check log cat fro response
        Log.d("Create Response", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created user

                return json.getInt("chave_criador");
            } else {
                // failed to create user
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;

    }


}
