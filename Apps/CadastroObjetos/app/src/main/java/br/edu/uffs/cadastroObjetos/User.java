package br.edu.uffs.cadastroObjetos;

public class User {
    private int id;
    private String email;
    private String password;

    //SETTERS////////////////////////////////////////
    public void setId(int a){id = a;}
    public void setEmail(String a){email = a;}
    public void setPass(String a){password = a;}
    /////////////////////////////////////////////////

    //GETTERS////////////////////////////////////////
    public int getId(){return id;}
    public String getEmail(){
        return email;
    }
    public String getPass(){
        return password;
    }
    /////////////////////////////////////////////////
}
