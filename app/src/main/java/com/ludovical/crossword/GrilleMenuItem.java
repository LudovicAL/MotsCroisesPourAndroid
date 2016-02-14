package com.ludovical.crossword;

public class GrilleMenuItem {
    private String nomGrille;
    private boolean victoire;

    public GrilleMenuItem(String nomGrille) {
        this.nomGrille = nomGrille;
        this.victoire = false;
    }

    //GETTERS AND SETTERS

    public String getNomGrille() {
        return nomGrille;
    }

    public void setNomGrille(String nomGrille) {
        this.nomGrille = nomGrille;
    }

    public boolean isVictoire() {
        return victoire;
    }

    public void setVictoire(boolean victoire) {
        this.victoire = victoire;
    }
}
