package com.dev_mobile.miniprojet_android;

public class Movie {
    private String id;
    private String marque;
    private String modele;
    private Double prix;
   // private String MiseEnCirculation;
    private String pimage;


    public Movie() {
    }



    public Movie(String id, String marque, String modele, Double prix,String pimage) {
        this.id = id;
        this.marque= marque;
        this.modele= modele;
        this.prix= prix;
       // this.MiseEnCirculation= MiseEnCirculation;
        this.pimage= pimage;

    }
   /* public Quad(String id, String marque, String modele, Double prix, String MiseEnCirculation,String pimage) {
        this.id = id;
        this.marque= marque;
        this.modele= modele;
        this.prix= prix;
        // this.MiseEnCirculation= MiseEnCirculation;
        this.pimage= pimage;

    }
*/
    public String getId() {
        return id;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public Double getPrix() {
        return prix;
    }

   /* public String getMiseEnCirculation() {
        return MiseEnCirculation;
    }
*/
    public String getPimage() {
        return pimage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }
    public void setPrix(Double prix) {
        this.prix = prix;
    }

    /*public void setMiseEnCirculation(String miseEnCirculation) {
        MiseEnCirculation = miseEnCirculation;
    }*/

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
}
