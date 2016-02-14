package com.ludovical.crossword;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Grille {
    private Context context;
    private ArrayList<Case> listeDesCases;
    private ArrayList<Champ> listeDesChamps;
    private ArrayList<Champ> listeDesChampsHorizontaux;
    private ArrayList<Champ> listeDesChampsVerticaux;
    private int nbCases;
    private int nbChamps;
    private int nbChampsHorizontaux;
    private int nbChampsVerticaux;
    private int hauteur;
    private int largeur;
    private String nomGrille;

    public Grille(Context context, int HAUTEUR, int LARGEUR, String nomGrille){
        this.context = context;
        this.nomGrille = nomGrille;
        this.hauteur = HAUTEUR;
        this.largeur = LARGEUR;
        this.nbCases = HAUTEUR * LARGEUR;
        this.listeDesCases = new ArrayList<>();
        getCaseData();
        this.listeDesChamps = new ArrayList<>();
        this.listeDesChampsHorizontaux = new ArrayList<>();
        this.listeDesChampsVerticaux = new ArrayList<>();
        this.listeDesChampsHorizontaux = recupereChampsHorizontaux();
        this.listeDesChampsVerticaux = recupereChampsVerticaux();
        this.listeDesChamps.addAll(listeDesChampsHorizontaux);
        this.listeDesChamps.addAll(listeDesChampsVerticaux);
        this.nbChampsHorizontaux = listeDesChampsHorizontaux.size();
        this.nbChampsVerticaux = listeDesChampsVerticaux.size();
        this.nbChamps = listeDesChamps.size();
        recupereChampsParents(true);
        recupereChampsParents(false);
        assigneDefinition();
    }

    //Méthode lisant un fichier texte pour y récupérer les informations des cases noires et blanche de la grille
    private void getCaseData() {
        AssetManager am =  context.getAssets();
        try {
            InputStream is = am.open("grilles/" + nomGrille + ".txt");
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String ligne;
                try {
                    int ligneNo = 0;
                    while ((ligne = br.readLine()) != null && ligneNo < hauteur) {
                        for (int i = 0; i < largeur && i < ligne.length(); i++) {
                            Case c = new Case(ligne.substring(i, i + 1));
                            listeDesCases.add(c);
                        }
                        ligneNo++;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Méthode lisant un fichier texte pour y récupérer les définitions et les associer aux objets "Champ" correspondants
    private void assigneDefinition() {
        AssetManager am =  context.getAssets();
        try {
            InputStream is = am.open("grilles/" + nomGrille + ".txt");
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String ligne;
                try {
                    int ligneNo = 0;
                    while (ligneNo < hauteur && (ligne = br.readLine()) != null) {
                        ligneNo++;
                    }
                    for (int i = 0; i < nbChamps && (ligne = br.readLine()) != null; i++) {
                        int debut = ligne.indexOf(";");
                        int fin = ligne.indexOf(";", debut + 1);
                        ArrayList<String> listeDesDefinitions = new ArrayList<>();
                        while (fin != -1 && fin > debut) {
                            listeDesDefinitions.add(ligne.substring(debut + 1, fin).trim());
                            debut = fin;
                            fin = ligne.indexOf(";", debut + 1);
                        }
                        listeDesChamps.get(i).setListeDesDefinitions(listeDesDefinitions);
                        listeDesChamps.get(i).setNbDefinitions(listeDesDefinitions.size());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Méthode récupérant les champs horizontaux de la grille
    private ArrayList<Champ> recupereChampsHorizontaux() {
        ArrayList<Champ> temp = new ArrayList<>();
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                ArrayList<Case> mot = new ArrayList<>();
                while (j < largeur && !listeDesCases.get(j + i * largeur).isNoire()) {
                    mot.add(listeDesCases.get(j + i * largeur));
                    j++;
                }
                if (mot.size() > 1) {
                    temp.add(new Champ(true, mot, mot.size()));
                }
            }
        }
        return temp;
    }

    //Méthode récupérant les champs verticaux de la grille
    private ArrayList<Champ> recupereChampsVerticaux() {
        ArrayList<Champ> temp = new ArrayList<>();
        for (int j = 0; j < largeur; j++) {
            for (int i = 0; i < hauteur; i++) {
                ArrayList<Case> mot = new ArrayList<>();
                while (i < hauteur && !listeDesCases.get(j + i * largeur).isNoire()) {
                    mot.add(listeDesCases.get(j + i * largeur));
                    i++;
                }
                if (mot.size() > 1) {
                    temp.add(new Champ(false, mot, mot.size()));
                }
            }
        }
        return temp;
    }

    //Méthode récupérant le champ parent de chaque case de la grille, une direction à la fois
    private void recupereChampsParents(boolean direction) {
        for (int i = 0; i < nbCases; i++) {
            boolean trouve = false;
            int nbChampsTempo =  getNbChampsDirection(direction);
            for (int j = 0; j < nbChampsTempo && !trouve; j++) {
                for (int k = 0; k < getListeDesChampsDirection(direction).get(j).getNbLettres() && !trouve; k++) {
                    if (listeDesCases.get(i) == getListeDesChampsDirection(direction).get(j).getListeDesCasesDuChamp().get(k)) {
                        listeDesCases.get(i).setChampParentDirection(getListeDesChampsDirection(direction).get(j), direction);
                        trouve = true;
                    }
                }
            }
        }
    }

    //GETTERS AND SETTERS

    public ArrayList<Case> getListeDesCases() {
        return listeDesCases;
    }

    public void setListeDesCases(ArrayList<Case> listeDesCases) {
        this.listeDesCases = listeDesCases;
    }

    public ArrayList<Champ> getListeDesChamps() {
        return listeDesChamps;
    }

    public void setListeDesChamps(ArrayList<Champ> listeDesChamps) {
        this.listeDesChamps = listeDesChamps;
    }

    public ArrayList<Champ> getListeDesChampsHorizontaux() {
        return listeDesChampsHorizontaux;
    }

    public void setListeDesChampsHorizontaux(ArrayList<Champ> listeDesChampsHorizontaux) {
        this.listeDesChampsHorizontaux = listeDesChampsHorizontaux;
    }

    public ArrayList<Champ> getListeDesChampsVerticaux() {
        return listeDesChampsVerticaux;
    }

    public void setListeDesChampsVerticaux(ArrayList<Champ> listeDesChampsVerticaux) {
        this.listeDesChampsVerticaux = listeDesChampsVerticaux;
    }

    public ArrayList<Champ> getListeDesChampsDirection(boolean direction) {
        if (direction) {
            return listeDesChampsHorizontaux;
        } else {
            return listeDesChampsVerticaux;
        }
    }

    public int getNbCases() {
        return nbCases;
    }

    public void setNbCases(int nbCases) {
        this.nbCases = nbCases;
    }

    public int getNbChamps() {
        return nbChamps;
    }

    public void setNbChamps(int nbChamps) {
        this.nbChamps = nbChamps;
    }

    public int getNbChampsHorizontaux() {
        return nbChampsHorizontaux;
    }

    public void setNbChampsHorizontaux(int nbChampsHorizontaux) {
        this.nbChampsHorizontaux = nbChampsHorizontaux;
    }

    public int getNbChampsVerticaux() {
        return nbChampsVerticaux;
    }

    public void setNbChampsVerticaux(int nbChampsVerticaux) {
        this.nbChampsVerticaux = nbChampsVerticaux;
    }

    public int getNbChampsDirection(boolean direction) {
        if (direction) {
            return nbChampsHorizontaux;
        } else {
            return nbChampsVerticaux;
        }
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }
}
