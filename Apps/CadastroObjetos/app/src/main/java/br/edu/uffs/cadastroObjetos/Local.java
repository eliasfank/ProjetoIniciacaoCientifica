package br.edu.uffs.cadastroObjetos;

import java.io.Serializable;

public class Local implements Serializable {
    private String nome;
    private int id;
    private int chave_criador;

    //SETTERS////////////////////////////////////////
    public void setId(int a){id = a;}
    public void setChave_Criador(int a){chave_criador = a;}
    public void setNome(String a){
        nome = a;
    }
    /////////////////////////////////////////////////

    //GETTERS////////////////////////////////////////
    public int getId(){
        return id;
    }
    public int getChave_Criador(){return chave_criador;}
    public String getNome(){
        return nome;
    }
    /////////////////////////////////////////////////
}
