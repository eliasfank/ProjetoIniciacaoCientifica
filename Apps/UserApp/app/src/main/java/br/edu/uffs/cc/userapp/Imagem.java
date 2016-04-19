package br.edu.uffs.cc.userapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Imagem {
    private static final String TAG_SUCCESS = "success";
    private String resultado;
    private byte[] imagem;
    private double lat;
    private double lon;
    private String url;

    public Imagem(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("PrefsFile", 0);
        url = sharedPreferences.getString("MEM1", "");
    }


    public void setImagem(byte[] a){
        imagem = a;
    }
    public void setResultado(String a){resultado = a;};
    public void setLat(double a){
        lat = a;
    }
    public void setLon(double a){
        lon = a;
    }

    public byte[] getImagem(){
        return imagem;
    }
    public String getResultado(){return resultado;};
    public double getLat(){
        return lat;
    }
    public double getLon(){
        return lon;
    }

    public void uploadImagem() {
        Log.i("IMAGEM", Base64.encodeToString(this.imagem, Base64.DEFAULT));
        Log.i("LAT", String.valueOf(this.lat));
        Log.i("LON", String.valueOf(this.lon));
        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("latitude", Double.toString(lat)));
        params.add(new BasicNameValuePair("longitude", Double.toString(lon)));
        params.add(new BasicNameValuePair("imagem", Base64.encodeToString(imagem, Base64.DEFAULT)));
        // getting JSON Object
        // Note that create product url accepts POST method
        String url_enviar_imagem = "http://"+url+"/Recognizer/receber_imagem.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.makeHttpRequest(url_enviar_imagem, "POST", params);

        // check log cat fro response
        Log.d("Respostaaaa", json.toString());

        // check for success tag
        try {
            this.setResultado(json.getString("frase"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
