package com.ludovical.crossword;

import android.view.View;
import android.widget.EditText;

public class Case {
    private View view;
    private EditText et;
    private String bonneReponse;
    private boolean initialisee;
    private boolean noire;
    private boolean remplie;
    private Champ champParentHorizontal;
    private Champ champParentVertical;
    private boolean possedeVoisins;

    Case(String texte) {
        this.view = null;
        this.et = null;
        this.bonneReponse = texte;
        this.initialisee = false;
        this.noire = (texte.equals("0")) ? true : false;
        this.remplie = false;
        this.possedeVoisins = false;
        this.champParentHorizontal = null;
        this.champParentVertical = null;
    }

    //GETTERS AND SETTERS

    public String getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(String bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

    public boolean isNoire() {
        return noire;
    }

    public void setNoire(boolean noire) {
        this.noire = noire;
    }

    public boolean isRemplie() {
        return remplie;
    }

    public void setRemplie(boolean remplie) {
        this.remplie = remplie;
    }

    public boolean isPossedeVoisins() {
        return possedeVoisins;
    }

    public void setPossedeVoisins(boolean possedeVoisins) {
        this.possedeVoisins = possedeVoisins;
    }

    public EditText getEt() {
        return et;
    }

    public void setEt(EditText et) {
        this.et = et;
    }

    public Champ getChampParent(boolean horizontal) {
        if (horizontal) {
            return champParentHorizontal;
        } else {
            return champParentVertical;
        }
    }

    public void setChampParent(boolean horizontal, Champ champParent) {
        if (horizontal) {
            this.champParentHorizontal = champParent;
        } else {
            this.champParentVertical = champParent;
        }
    }

    public Champ getChampParentHorizontal() {
        return champParentHorizontal;
    }

    public void setChampParentHorizontal(Champ champParentHorizontal) {
        this.champParentHorizontal = champParentHorizontal;
    }

    public Champ getChampParentVertical() {
        return champParentVertical;
    }

    public void setChampParentVertical(Champ champParentVertical) {
        this.champParentVertical = champParentVertical;
    }

    public void setChampParentDirection(Champ champParent, boolean direction) {
        if (direction) {
            this.champParentHorizontal = champParent;
        } else {
            this.champParentVertical = champParent;
        }
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public boolean isInitialisee() {
        return initialisee;
    }

    public void setInitialisee(boolean initialisee) {
        this.initialisee = initialisee;
    }
}
