package com.ludovical.crossword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import static com.ludovical.crossword.SharedPreferencesManager.getIntData;

public class Utils {

    //Méthode affichant un message à l'écran...
    public static void showNeutralMessage(String titre, String message, Context context) {
        AlertDialog monAlerte;
        monAlerte = new AlertDialog.Builder(context).create();
        monAlerte.setTitle(titre);
        monAlerte.setMessage(message);
        monAlerte.setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.alertDialogBouton).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Ce bouton n'a aucun effet sinon de fermer le AlertDialog
            }
        });
        monAlerte.show();
    }

    //Méthode appelée à la création de chaque activité afin de déterminer la thématique de couleur à utiliser
    public static void determineTheme(Context context, boolean fullScreen) {
        int currentTheme = getIntData("optionTheme", context);
        switch (currentTheme) {
            case 1:
                if (fullScreen) {
                    context.setTheme(R.style.greenOrangeThemeFullScreen);
                } else {
                    context.setTheme(R.style.greenOrangeTheme);
                }
                break;
            case 2:
                if (fullScreen) {
                    context.setTheme(R.style.pinkBlueThemeFullScreen);
                } else {
                    context.setTheme(R.style.pinkBlueTheme);
                }
                break;
            case 3:
                if (fullScreen) {
                    context.setTheme(R.style.NormalThemeFullScreen);
                } else {
                    context.setTheme(R.style.NormalTheme);
                }
                break;
            default:
                if (fullScreen) {
                    context.setTheme(R.style.redGreyThemeFullScreen);
                } else {
                    context.setTheme(R.style.redGreyTheme);
                }
        }
    }
}
