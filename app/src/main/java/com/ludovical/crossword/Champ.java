package com.ludovical.crossword;

import java.util.ArrayList;

public class Champ {
    private boolean horizontal;
    private ArrayList<String> listeDesDefinitions;
    private ArrayList<Case> listeDesCasesDuChamp;
    private int nbLettres;
    private int nbIndices;
    private int nbDefinitions;

    public Champ (boolean horizontal, ArrayList<Case> listeDesCasesDuChamp, int nbLettres) {
        this.horizontal = horizontal;
        this.listeDesDefinitions = new ArrayList<>();
        this.listeDesCasesDuChamp = listeDesCasesDuChamp;
        this.nbLettres = nbLettres;
        this.nbIndices = 1;
        this.nbDefinitions = 1;
    }

    public Champ (boolean horizontal, ArrayList<Case> listeDesCasesDuChamp, int nbLettres, String listeDesDefinitions) {
        this.horizontal = horizontal;
        ArrayList<String> tempo = new ArrayList<>();
        tempo.add(listeDesDefinitions);
        this.listeDesDefinitions = tempo;
        this.listeDesCasesDuChamp = listeDesCasesDuChamp;
        this.nbLettres = nbLettres;
        this.nbIndices = 1;
        this.nbDefinitions = 1;
    }

    //GETTERS AND SETTERS

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public ArrayList<Case> getListeDesCasesDuChamp() {
        return listeDesCasesDuChamp;
    }

    public void setListeDesCasesDuChamp(ArrayList<Case> listeDesCasesDuChamp) {
        this.listeDesCasesDuChamp = listeDesCasesDuChamp;
    }

    public int getNbLettres() {
        return nbLettres;
    }

    public void setNbLettres(int nbLettres) {
        this.nbLettres = nbLettres;
    }

    public ArrayList<String> getListeDesDefinitions() {
        return listeDesDefinitions;
    }

    public void setListeDesDefinitions(ArrayList<String> listeDesDefinitions) {
        this.listeDesDefinitions = listeDesDefinitions;
    }

    public int getNbIndices() {
        return nbIndices;
    }

    public void setNbIndices(int nbIndices) {
        this.nbIndices = nbIndices;
    }

    public int getNbDefinitions() {
        return nbDefinitions;
    }

    public void setNbDefinitions(int nbDefinitions) {
        this.nbDefinitions = nbDefinitions;
    }
}
