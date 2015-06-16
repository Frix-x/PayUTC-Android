package fr.utc.frixx.payutc_android;

import java.util.Hashtable;

/**
 * Created by frixx on 14/06/15.
 */
public class UserData {

    private static UserData manager = null;

    private String login;
    private String sessionid;
    private String prenom;
    private String nom;
    private Integer solde;
    private Boolean blocked_status;
    //private Hashtable history;

    public static UserData manager (){
        if (manager == null){
            manager = new UserData();
        }
        return manager;
    }

    public void reset () {
        login = null;
        sessionid = null;
        prenom = null;
        nom = null;
        solde = null;
        blocked_status = null;
    }

    public void setLoginAndId (String l, String sid){
        login = l;
        sessionid = sid;
    }

    public String getLogin (){
        return login;
    }

    public String getSessionid (){
        return sessionid;
    }

    public void setNom (String p, String n){
        prenom = p;
        nom = n;
    }

    public String getPrenom (){
        return prenom;
    }

    public String getNom (){
        return nom;
    }

    public void setSolde (Integer s){
        solde = s;
    }

    public void setBlocked_status (Boolean bs){
        blocked_status = bs;
    }

    public Integer getSolde (){
        return solde;
    }

    public Boolean getBlocked_status (){
        return blocked_status;
    }
}
