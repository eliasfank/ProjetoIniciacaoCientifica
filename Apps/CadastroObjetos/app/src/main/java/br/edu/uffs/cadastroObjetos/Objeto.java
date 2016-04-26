package br.edu.uffs.cadastroObjetos;

import java.io.Serializable;

public class Objeto implements Serializable {
    private String nome;
    private String frase;
    private int id;
    private int local;
    private double latitude;
    private double longitude;
    private byte[] imagem;

    //SETTERS////////////////////////////////////////
    public void setId(int a){id = a;}
    public void setNome(String a){nome = a;}
    public void setFrase(String a){frase = a;}
    public void setLocal(int a){local = a;}
    public void setLatitude(double a){latitude = a;}
    public void setLongitude(double a){longitude = a;}
    public void setImagem(byte[] a){imagem = a;}
    /////////////////////////////////////////////////

    //GETTERS////////////////////////////////////////
    public int getId(){return id;}
    public String getNome(){return nome;}
    public String getFrase(){return frase;}
    public int getLocal(){return local;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public byte[] getImagem(){return imagem;}
    /////////////////////////////////////////////////
}
