package com.dev_mobile.miniprojet_android;

public class Movie {
    private String id;
    private String titre;
    private String genre;
    private Double prix;
    // private String MiseEnCirculation;
    private String pimage;


    public Movie() {
    }


    public Movie(String id, String titre, String genre, Double prix, String pimage) {
        this.id = id;
        this.titre = titre;
        this.genre = genre;
        this.prix = prix;
        // this.MiseEnCirculation= MiseEnCirculation;
        this.pimage = pimage;

    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getGenre() {
        return genre;
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

    public void setTitre(String titre) {
        this.titre = titre;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }


    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
}
