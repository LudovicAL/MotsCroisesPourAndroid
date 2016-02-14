package com.ludovical.crossword;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import java.util.ArrayList;
import static com.ludovical.crossword.SharedPreferencesManager.*;
import static com.ludovical.crossword.Utils.determineTheme;

public class GrilleActivity extends Activity {
    public static final int LARGEUR = 12;
    public static final int HAUTEUR = 12;
    private GridViewAdapter gvAdapter;

    private String nomGrille;
    private Grille grille;
    private boolean checkError;
    private boolean revealAnswer;
    private GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme(this, true);
        setContentView(R.layout.activity_grille);
        Intent intent = getIntent();
        this.nomGrille = intent.getStringExtra("nomGrille");
        grille = new Grille(this, HAUTEUR, LARGEUR, nomGrille);
        Button hintButton = (Button)findViewById(R.id.hintButton);
        Chronometer chronometer = (Chronometer)findViewById(R.id.chronometer);
        gv = (GridView)findViewById(R.id.gridView);
        gvAdapter = new GridViewAdapter(this, grille, this, hintButton, chronometer);
        gv.setAdapter(gvAdapter);
        gv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    //Ce listener attend le chargement complet du GridView avant d'y écrire des données (s'il y a lieu)
                    @Override
                    public void onGlobalLayout() {
                        importerTentative(nomGrille, gvAdapter, GrilleActivity.this);
                        gvAdapter.notifyDataSetChanged();
                        gv.getViewTreeObserver().removeOnGlobalLayoutListener(this); // unregister listener (this is important)
                    }
                }
        );
        this.checkError = getBoolData("optionCheckError", GrilleActivity.this);
        this.revealAnswer = getBoolData("optionRevealAnswer", GrilleActivity.this);
    }

    @Override
    protected void onPause() {
        exporterTentative(nomGrille, gvAdapter, this);
        Intent i = new Intent();
        i.putExtra("retour", (gvAdapter.isVictoire()) ? "victoire" : "défaite");
        setResult(RESULT_OK, i);
        super.onPause();
    }

    //Lorsque l'utilisateur ferme l'activité contenant la fenêtre de jeu simplifiée (la fenêtre avec la grille simplifiée)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Vérifier le code de l’activité
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK) {
                ArrayList<String> reponsesHorizontales = data.getStringArrayListExtra("reponsesHorizontales");
                ArrayList<String> reponsesVerticales = data.getStringArrayListExtra("reponsesVerticales");
                //Faire quelque chose ici avec le message reçu...
            }
        }
    }

    //Lors d'un clic sur le bouton "Suivant"
    public void buttonSuivant(View v) {
        gvAdapter.goNext(gvAdapter.getPositionActuelle());
    }

    //Lors d'un clic sur le bouton "Précédent"
    public void buttonPrecedent(View v) {
        gvAdapter.goPrevious(gvAdapter.getPositionActuelle());
    }

    //Lors d'un clic sur le bouton de direction
    public void buttonDirection(View v) {
        gvAdapter.perdFocus(gvAdapter.getPositionActuelle());
        gvAdapter.setChariotHorizontal(!gvAdapter.isChariotHorizontal());
        Button b = (Button) v;
        if (gvAdapter.isChariotHorizontal()) {
            b.setText(R.string.vertical);
        } else {
            b.setText(R.string.horizontal);
        }
        gvAdapter.gagneFocus(gvAdapter.getPositionActuelle());
    }

    //Lors d'un clic sur le bouton "close"
    public void fermeture(View v) {
        finish();
    }

    //Lors d'un clic sur l'un des boutons du clavier (lettre)
    public void lettreClick(View v) {
        Button b = (Button) v;
        EditText et = gvAdapter.getGrille().getListeDesCases().get(gvAdapter.getPositionActuelle()).getEt();
        et.setText(b.getText());
        et.setTextColor(Color.BLACK);
        gvAdapter.notifyDataSetChanged();
    }

    //Lors d'un clic sur le bouton "backspace"
    public void backspaceClick (View v) {
        gvAdapter.getGrille().getListeDesCases().get(gvAdapter.getPositionActuelle()).getEt().setText("");
        gvAdapter.notifyDataSetChanged();
    }

    //Lors d'un clic sur le bouton "vérifier les erreurs"
    public void checkErrorsClick (View v) {
        if (checkError) {
            for (int i = 0; i < gvAdapter.getGrille().getNbCases(); i++) {
                if (gvAdapter.getGrille().getListeDesCases().get(i).getEt().getText().length() > 0) {
                    if (gvAdapter.getGrille().getListeDesCases().get(i).getEt().getText().toString().equals(gvAdapter.getGrille().getListeDesCases().get(i).getBonneReponse())) {
                        gvAdapter.getGrille().getListeDesCases().get(i).getEt().setTextColor(Color.BLACK);
                    } else {
                        gvAdapter.getGrille().getListeDesCases().get(i).getEt().setTextColor(Color.RED);
                    }
                }
            }
        } else {
            Champ champ = gvAdapter.getGrille().getListeDesCases().get(gvAdapter.getPositionActuelle()).getChampParent(gvAdapter.isChariotHorizontal());
            if (champ != null) {
                for (int i = 0; i < champ.getNbLettres(); i++) {
                    if (champ.getListeDesCasesDuChamp().get(i).getEt().getText().toString().equals(champ.getListeDesCasesDuChamp().get(i).getBonneReponse())) {
                        champ.getListeDesCasesDuChamp().get(i).getEt().setTextColor(Color.BLACK);
                    } else {
                        champ.getListeDesCasesDuChamp().get(i).getEt().setTextColor(Color.RED);
                    }
                }
            }
        }
    }

    //Lors d'un clic sur le bouton "indice"
    public void hintClick(View v) {
        Champ champ = gvAdapter.getGrille().getListeDesCases().get(gvAdapter.getPositionActuelle()).getChampParent(gvAdapter.isChariotHorizontal());
        champ.setNbIndices(champ.getNbIndices() + 1);
        gvAdapter.perdFocus(gvAdapter.getPositionActuelle());
        gvAdapter.gagneFocus(gvAdapter.getPositionActuelle());
    }

    //Lors d'un clic sur le bouton "révéler une réponse"
    public void answerClick(View v) {
        Champ champ = gvAdapter.getGrille().getListeDesCases().get(gvAdapter.getPositionActuelle()).getChampParent(gvAdapter.isChariotHorizontal());
        if (champ != null) {
            if (revealAnswer) {
                for (int i = 0; i < champ.getNbLettres(); i++) {
                    champ.getListeDesCasesDuChamp().get(i).getEt().setText(champ.getListeDesCasesDuChamp().get(i).getBonneReponse());
                    champ.getListeDesCasesDuChamp().get(i).getEt().setTextColor(Color.BLACK);
                }
            } else {
                for (int i = 0; i < champ.getNbLettres(); i++) {
                    if (champ.getListeDesCasesDuChamp().get(i) == gvAdapter.getGrille().getListeDesCases().get(gvAdapter.getPositionActuelle())) {
                        champ.getListeDesCasesDuChamp().get(i).getEt().setText(champ.getListeDesCasesDuChamp().get(i).getBonneReponse());
                        champ.getListeDesCasesDuChamp().get(i).getEt().setTextColor(Color.BLACK);
                        break;
                    }
                }
            }

        }
    }

    //Lors d'un clic sur le bouton "vue simplifiée"
    public void simplifieeClick(View v) {
        ArrayList<String> reponsesHorizontales = recupererReponsesEnArrayList(true);
        ArrayList<String> reponsesVerticales = recupererReponsesEnArrayList(false);
        ArrayList<String> definitionsHorizontales = recupererDefinitionsEnArrayList(true);
        ArrayList<String> definitionsVerticales = recupererDefinitionsEnArrayList(false);
        Intent nouveau = new Intent(GrilleActivity.this, GrilleSimplifieeActivity.class);
        nouveau.putExtra("direction", gvAdapter.isChariotHorizontal());
        nouveau.putStringArrayListExtra("reponsesHorizontales", reponsesHorizontales);
        nouveau.putStringArrayListExtra("reponsesVerticales", reponsesVerticales);
        nouveau.putStringArrayListExtra("definitionsHorizontales", definitionsHorizontales);
        nouveau.putStringArrayListExtra("definitionsVerticales", definitionsVerticales);
        ((Activity) GrilleActivity.this).startActivityForResult(nouveau, 2);
    }

    //Méthode récupérant les réponses saisies par l'utilisateur et les stockant sous la forme d'un ArrayList de String, une direction à la fois
    private ArrayList<String> recupererReponsesEnArrayList(boolean direction) {
        ArrayList<String> reponses = new ArrayList<>();
        for (int i = 0, nbChampsTempo = gvAdapter.getGrille().getNbChampsDirection(direction); i < nbChampsTempo; i++) {
            String mot = "";
            for (int j = 0; j < gvAdapter.getGrille().getListeDesChampsDirection(direction).get(i).getNbLettres(); j++) {
                String lettre = gvAdapter.getGrille().getListeDesChampsDirection(direction).get(i).getListeDesCasesDuChamp().get(j).getEt().getText().toString();
                if (!lettre.isEmpty()) {
                    mot += lettre;
                } else {
                    mot += "1";
                }
            }
            reponses.add(mot);
        }
        return reponses;
    }

    //Méthode récupérant les définitions et les stockant sous la forme d'un ArrayList de String, une direction à la fois
    private ArrayList<String> recupererDefinitionsEnArrayList(boolean direction) {
        ArrayList<String> definitions = new ArrayList<>();
        for (int i = 0, nbChampsTempo = gvAdapter.getGrille().getNbChampsDirection(direction); i < nbChampsTempo; i++) {
            String tempo = "";
            for (int j = 0, nbIndicesTempo = gvAdapter.getGrille().getListeDesChampsDirection(direction).get(i).getNbIndices(); j < nbIndicesTempo; j++) {
                tempo += gvAdapter.getGrille().getListeDesChampsDirection(direction).get(i).getListeDesDefinitions().get(j);
                if (j < nbIndicesTempo - 1) {
                    tempo += " | ";
                }
            }
            definitions.add(tempo);
        }
        return definitions;
    }
}