package com.ludovical.crossword;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import static com.ludovical.crossword.Utils.determineTheme;

public class GrilleSimplifieeActivity extends Activity {

    private ArrayList<Champ> listeChampsHorizontaux;
    private ArrayList<Champ> listeChampsVerticaux;
    private int nbChampsHorizontaux;
    private int nbChampsVerticaux;
    private boolean chariotHorizontal;
    private LinearLayout ll;
    private ArrayList<GridViewAdapterChamp> listeGridViewAdapterChamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme(this, true);
        setContentView(R.layout.activity_grille_simplifiee);
        Intent intent = getIntent();
        this.chariotHorizontal = intent.getBooleanExtra("direction", true);
        this.listeChampsHorizontaux = importerChamps(intent, true, "reponsesHorizontales", "definitionsHorizontales");
        this.listeChampsVerticaux = importerChamps(intent, false, "reponsesVerticales", "definitionsVerticales");
        this.nbChampsHorizontaux = listeChampsHorizontaux.size();
        this.nbChampsVerticaux = listeChampsVerticaux.size();
        this.ll = (LinearLayout)findViewById(R.id.LinearLayout);
        insererLayouts(chariotHorizontal);
    }

    @Override
    protected void onPause() {
        Intent i = new Intent();
        i.putStringArrayListExtra("reponsesHorizontales", recupererReponsesEnArrayList(true));
        i.putStringArrayListExtra("reponsesHorizontales", recupererReponsesEnArrayList(false));
        setResult(RESULT_OK, i);
        super.onPause();
    }

    private void insererLayouts(boolean direction) {
        for (int i = 0, nbChampsDansDirection = getNbChampsDirection(direction); i < nbChampsDansDirection; i++) {
            LayoutInflater inflater = (LayoutInflater) GrilleSimplifieeActivity.this.getSystemService(GrilleSimplifieeActivity.this.LAYOUT_INFLATER_SERVICE);
            View elementLayout = inflater.inflate(R.layout.champ, null);
            ll.addView(elementLayout);
            TextView tv = (TextView)elementLayout.findViewById(R.id.genericTextViewChamp);
            tv.setText(getListeDesChampsDirection(direction).get(i).getListeDesDefinitions().get(0));
            GridView gvChamp = (GridView)elementLayout.findViewById(R.id.genericGridViewChamp);
            GridViewAdapterChamp gvAdapterChamp = new GridViewAdapterChamp(this, getListeDesChampsDirection(direction).get(i), this);
            gvChamp.setAdapter(gvAdapterChamp);
            listeGridViewAdapterChamp.add(gvAdapterChamp);
        }
    }

    private void viderLayouts() {
        ll.removeAllViews();
    }

    //Méthode renvoyant sous forme d'objets "Champ" peuplés d'objets "Case" les informations de la grille transmises via l'Intent.
    private ArrayList<Champ> importerChamps(Intent intent, boolean horizontal, String reponses, String definitions) {
        ArrayList<String> listeChampsStr = intent.getStringArrayListExtra(reponses);
        ArrayList<String> listeDefinitionsStr = intent.getStringArrayListExtra(definitions);
        ArrayList<Champ> listeChampsSimplifiee = new ArrayList<>();
        for (int i = 0, taille = listeChampsStr.size(); i < taille; i++) {
            ArrayList<Case> listeCasesSimplifiee = new ArrayList<>();
            int longueur = listeChampsStr.get(i).length();
            for (int j = 0; j < longueur; j++) {
                Case lettre = new Case(listeChampsStr.get(i).substring(j, j + 1));
                listeCasesSimplifiee.add(lettre);
            }
            Champ champ = new Champ(horizontal, listeCasesSimplifiee, longueur, listeDefinitionsStr.get(i));
            listeChampsSimplifiee.add(champ);
        }
        return listeChampsSimplifiee;
    }

    //Lors d'un clic sur le bouton "Suivant"
    public void buttonSuivantSimplifiee(View v) {

    }

    //Lors d'un clic sur le bouton "Précédent"
    public void buttonPrecedentSimplifiee(View v) {

    }

    //Lors d'un clic sur le bouton de direction
    public void buttonDirectionSimplifiee(View v) {

    }

    //Lors d'un clic sur le bouton "close"
    public void fermetureSimplifiee(View v) {
        finish();
    }

    //Lors d'un clic sur l'un des boutons du clavier (lettre)
    public void lettreClickSimplifiee(View v) {

    }

    //Lors d'un clic sur le bouton "backspace"
    public void backspaceClickSimplifiee (View v) {

    }

    //Lors d'un clic sur le bouton "vérifier les erreurs"
    public void checkErrorsClickSimplifiee (View v) {

    }

    //Lors d'un clic sur le bouton "indice"
    public void hintClickSimplifiee(View v) {

    }

    //Lors d'un clic sur le bouton "révéler une réponse"
    public void answerClickSimplifiee(View v) {

    }

    //Méthode récupérant les réponses saisies par l'utilisateur et les stockant sous la forme d'un ArrayList de String
    private ArrayList<String> recupererReponsesEnArrayList(boolean direction) {
        ArrayList<String> reponses = new ArrayList<>();
        int nbChampsTempo = getNbChampsDirection(direction);
        for (int i = 0; i < nbChampsTempo; i++) {
            String mot = "";
            for (int j = 0; j < getListeDesChampsDirection(direction).get(i).getNbLettres(); j++) {
                String lettre = getListeDesChampsDirection(direction).get(i).getListeDesCasesDuChamp().get(j).getEt().getText().toString();
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

    private int getNbChampsDirection(boolean direction){
        if (direction) {
            return nbChampsHorizontaux;
        } else {
            return nbChampsVerticaux;
        }
    }

    private ArrayList<Champ> getListeDesChampsDirection(boolean direction) {
        if (direction) {
            return listeChampsHorizontaux;
        } else {
            return listeChampsVerticaux;
        }
    }
}
