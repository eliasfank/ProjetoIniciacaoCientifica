package br.edu.uffs.cc.userapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Imagem {
    private String resultado;
    private File imagem;
    private double lat;
    private double lon;
    private String url;

    public Imagem(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("PrefsFile", 0);
        url = sharedPreferences.getString("MEM1", "");
    }

    public void setImagem(File a){
        imagem = a;
    }
    public void setResultado(String a){resultado = a;};
    public void setLat(double a){
        lat = a;
    }
    public void setLon(double a){
        lon = a;
    }

    public File getImagem(){
        return imagem;
    }
    public String getResultado(){return resultado;};
    public double getLat(){
        return lat;
    }
    public double getLon(){
        return lon;
    }
}
