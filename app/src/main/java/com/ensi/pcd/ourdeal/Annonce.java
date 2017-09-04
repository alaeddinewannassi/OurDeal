package com.ensi.pcd.ourdeal;

/**
 * Created by Ouannassi on 11/04/2017.
 */
import java.io.Serializable;
import java.util.Calendar ;

public class Annonce implements Serializable{
    private int id_objet ;
    public String titre ;
    private int id_vendeur ;
    public String description ;
    public int prix ;
    public String adresse;
    public String ville ;
    public String photoUrl ;
    public String date_pub ;
    public String categorie ;



    public Annonce(String titre, String description, int prix, String adresse, String ville, String categorie, String photoUrl) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.adresse = adresse;
        this.ville = ville;
        this.categorie = categorie;
        this.photoUrl= photoUrl;
        this.date_pub= date_pub;
    }


    public String getDate_pub() {
        return date_pub;
    }

    public void setDate_pub(String date_pub) {
        this.date_pub = date_pub;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public int getId_objet() {
        return id_objet;
    }

    public void setId_objet(int id_objet) {
        this.id_objet = id_objet;
    }
    public int getId_vendeur() {

        return id_vendeur;
    }

    public void setId_vendeur(int id_vendeur) {
        this.id_vendeur = id_vendeur;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Annonce() {
    }
}
