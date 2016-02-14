package com.ludovical.crossword;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static SharedPreferences getPrefs(String key, Context context) {
        return context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    //Méthode récupérant une "préférence" stocké sous la forme d'une string
    private static String getStringData(String key, Context context) {
        return getPrefs(key, context).getString(key, "");
    }

    //Méthode récupérant une "préférence" stocké sous la forme d'un boolean
    public static Boolean getBoolData(String key, Context context) {
        return getPrefs(key, context).getBoolean(key, true);
    }

    //Méthode récupérant une "préférence" stocké sous la forme d'un int
    public static int getIntData(String key, Context context) {
        return getPrefs(key, context).getInt(key, 0);
    }

    //Méthode appelée à la sortie de la fenêtre de jeu (la fenêtre avec la grille) afin d'enregistré le progrès du joueur
    public static void exporterTentative(String key, GridViewAdapter gvAdapter, Context context) {
        String recuperation = "";
        for (int i = 0, taille = gvAdapter.getGrille().getListeDesCases().size(); i < taille; i++) {
            if (gvAdapter.getGrille().getListeDesCases().get(i).isNoire()) {
                recuperation += "0";
            } else {
                if (gvAdapter.getGrille().getListeDesCases().get(i).getEt().getText().toString().length() > 0) {
                    recuperation += gvAdapter.getGrille().getListeDesCases().get(i).getEt().getText().toString();
                } else {
                    recuperation += "1";
                }
            }
        }
        SharedPreferences.Editor editor = getPrefs(key, context).edit();
        editor.putString(key, recuperation);
        editor.commit();
    }

    //Méthode appelée à l'ouverture de la fenêtre de jeu (la fenêtre avec la grille) afin de charger les progrès déjà accomplis du joueur
    public static void importerTentative(String key, GridViewAdapter gvAdapter, Context context) {
        String recuperation = getStringData(key, context);
        if (recuperation.length() > 0) { //Car la fonction getTentative renvoit une chaîne vide lorsqu'aucune tentative de remplissage préexistante n'est trouvée...
            for (int i = 0, taille = gvAdapter.getGrille().getListeDesCases().size(); i < taille; i++) {
                String lettre = recuperation.substring(i, i + 1);
                if (!lettre.equals("1") && !lettre.equals("0")) {
                    gvAdapter.getGrille().getListeDesCases().get(i).getEt().setText(lettre);
                }
            }
        }
    }

    //Méthode enregistrant une "préférence" sous la forme d'un boolean
    public static void exporterOptionBoolean(Context context, boolean data, String key) {
        SharedPreferences.Editor editor = getPrefs(key, context).edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    //Méthode enregistrant une "préférence" sous la forme d'un int
    public static void exporterOptionInt(Context context, int data, String key) {
        SharedPreferences.Editor editor = getPrefs(key, context).edit();
        editor.putInt(key, data);
        editor.commit();
    }
}
