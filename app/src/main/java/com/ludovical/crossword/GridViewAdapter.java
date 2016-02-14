package com.ludovical.crossword;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import static com.ludovical.crossword.SharedPreferencesManager.*;
import static com.ludovical.crossword.Utils.*;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private boolean victoire;
    private boolean chariotHorizontal;
    private TextView tvDefinition;
    private int positionActuelle;
    private Grille grille;
    private boolean skipFilled;
    private boolean skipSingleLetter;
    private Button hintButton;
    private Chronometer chronometer;
    private int couleurPrimaire;
    private int couleurSecondaire;

    public GridViewAdapter(Context context, Grille grille, Activity activity, Button hintButton, Chronometer chronometer){
        this.context = context;
        this.tvDefinition = (TextView)activity.findViewById(R.id.definition);
        this.positionActuelle = 0;
        this.victoire = false;
        this.chariotHorizontal = true;
        this.grille = grille;
        this.skipFilled = getBoolData("optionSkipFilled", context);
        this.skipSingleLetter = getBoolData("optionSkipSingleLetter", context);
        this.hintButton = hintButton;
        this.chronometer = chronometer;
        chronometer.start();
        preparerCouleurs();
    }

    @Override
    public int getCount() {
        return grille.getNbCases();
    }

    @Override
    public Object getItem(int position) {
        return grille.getListeDesCases().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View elementLayout;
        Case temp = grille.getListeDesCases().get(position);
        if (convertView == null) {
            if (grille.getListeDesCases().get(position).getView() != null) {
                return grille.getListeDesCases().get(position).getView();
            } else {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                if (temp.isNoire()) {
                    elementLayout = inflater.inflate(R.layout.case_noire, parent, false);
                } else {
                    elementLayout = inflater.inflate(R.layout.case_blanche, parent, false);
                }
                EditText tv = (EditText)elementLayout.findViewById(R.id.genericEditText);
                tv.setShowSoftInputOnFocus(false);
                temp.setEt(tv);
                if (!temp.isNoire()) {
                    final int pos = position;
                    temp.getEt().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                gagneFocus(pos);
                            } else {
                                perdFocus(pos);
                            }
                        }
                    });
                    temp.getEt().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            GridViewAdapter.this.notifyDataSetChanged();
                            if (s.length() > 0) {
                                goNext(pos);
                                if (!victoire && testVictory()) {
                                    victoire = true;
                                    chronometer.stop();
                                    showNeutralMessage(context.getResources().getString(R.string.victoryTitle).toString(), context.getResources().getString(R.string.victoryMessage).toString(), context);
                                }
                            } else {
                                victoire = false;
                                goPrevious(pos);
                            }
                        }
                    });
                }
                grille.getListeDesCases().get(position).setView(elementLayout);
            }
        } else {
            elementLayout = convertView;
        }
        return elementLayout;
    }

    //Prépare les couleurs de surlignage en fonction du theme choisi
    private void preparerCouleurs() {
        int currentTheme = getIntData("optionTheme", context);
        switch (currentTheme) {
            case 1:
                couleurPrimaire = R.color.primary_orange;
                couleurSecondaire = R.color.secondary_green;
                break;
            case 2:
                couleurPrimaire = R.color.primary_pink;
                couleurSecondaire = R.color.secondary_indigo;
                break;
            case 3:
                couleurPrimaire = R.color.primary_blue_grey;
                couleurSecondaire = R.color.secondary_blue_grey;
                break;
            default:
                couleurPrimaire = R.color.primary_red;
                couleurSecondaire = R.color.secondary_blue_grey;
        }
    }

    //Teste si les conditions gagnantes sont réunies
    private boolean testVictory() {
        boolean victoire = true;
        for (int i = 0; i < grille.getNbCases(); i++) {
            if (!grille.getListeDesCases().get(i).isNoire()) {
                if (!grille.getListeDesCases().get(i).getEt().getText().toString().equalsIgnoreCase(grille.getListeDesCases().get(i).getBonneReponse())) {
                    victoire = false;
                    break;
                }
            }
        }
        return victoire;
    }

    //Méthode appelée lorsqu'une case de la grille gagne le focus
    public void gagneFocus(int position) {
        positionActuelle = position;
        if (grille.getListeDesCases().get(position).getChampParent(chariotHorizontal) != null) {
            for (int i = 0; i < grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getNbLettres(); i++) {
                grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getListeDesCasesDuChamp().get(i).getEt().setBackgroundColor(context.getResources().getColor(couleurSecondaire));
            }
            if (!grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getListeDesDefinitions().isEmpty()) {
                String str = "(" + String.valueOf(grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getNbLettres()) + ") ";
                for (int i = 0; i < grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getNbDefinitions() && i < grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getNbIndices(); i++) {
                    if (i > 0) {
                        str += " | ";
                    }
                    str += grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getListeDesDefinitions().get(i);
                }
                tvDefinition.setText(str);
                if (grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getNbIndices() < grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getNbDefinitions()) {
                    hintButton.setEnabled(true);
                }
            }
        }
        grille.getListeDesCases().get(position).getEt().setBackgroundColor(context.getResources().getColor(couleurPrimaire));
    }

    //Méthode appelée lorsqu'une case de la grille perd le focus
    public void perdFocus(int position) {
        if (grille.getListeDesCases().get(position).getChampParent(chariotHorizontal) != null) {
            for (int i = 0; i < grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getNbLettres(); i++) {
                grille.getListeDesCases().get(position).getChampParent(chariotHorizontal).getListeDesCasesDuChamp().get(i).getEt().setBackgroundColor(Color.WHITE);
            }
        }
        grille.getListeDesCases().get(position).getEt().setBackgroundColor(Color.WHITE);
        tvDefinition.setText("");
        hintButton.setEnabled(false);
    }

    //Renvoit l'index de la case suivante
    private int getNextPosition(int positionTemp) {
        int pos = positionTemp;
        if (chariotHorizontal) {
            pos++;
        } else {
            int i = pos - ((pos / grille.getLargeur()) * grille.getLargeur());
            int j = (pos / grille.getLargeur());
            if (j < grille.getHauteur() -1) {
                j++;
            } else {
                if (i < grille.getLargeur() - 1) {
                    j = 0;
                    i++;
                }
            }
            pos = i + (j * grille.getLargeur());
        }
        return pos;
    }

    //Sélectionne la case suivante
    public void goNext(int position) {
        int positionTemp = getNextPosition(position);
        Case c = null;
        try{
            c = grille.getListeDesCases().get(positionTemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (positionTemp < grille.getNbCases() - 1 && (c.isNoire() || (skipFilled && c.getEt().getText().length() > 0) || (skipSingleLetter && c.getChampParent(chariotHorizontal) == null))) {
            positionTemp = getNextPosition(positionTemp);
            try{
                c = grille.getListeDesCases().get(positionTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (positionTemp < grille.getNbCases() && !grille.getListeDesCases().get(positionTemp).isNoire()){
            grille.getListeDesCases().get(positionTemp).getEt().requestFocus();
        }
    }

    //Sélectionne la case précédente
    public void goPrevious(int position) {
        int positionTemp = position;
        if (chariotHorizontal) {
            positionTemp--;
            while (positionTemp > 0 && grille.getListeDesCases().get(positionTemp).isNoire()) {
                positionTemp--;
            }
            if (positionTemp >= 0 && !grille.getListeDesCases().get(positionTemp).isNoire()){
                grille.getListeDesCases().get(positionTemp).getEt().requestFocus();
            }
        } else {
            int i = positionTemp - ((positionTemp / grille.getLargeur()) * grille.getLargeur());
            int j = (positionTemp / grille.getLargeur()) - 1;
            boolean trouve = false;
            while (i >= 0 && !trouve) {
                while (j >= 0 && !trouve) {
                    if (!grille.getListeDesCases().get(i + j * grille.getLargeur()).isNoire()) {
                        grille.getListeDesCases().get(i + j * grille.getLargeur()).getEt().requestFocus();
                        trouve = true;
                    }
                    j--;
                }
                j = grille.getHauteur() - 1;
                i--;
            }
        }
    }

    //GETTERS AND SETTERS

    public void setChariotHorizontal(boolean chariotHorizontal) {
        this.chariotHorizontal = chariotHorizontal;
    }

    public boolean isChariotHorizontal() {
        return chariotHorizontal;
    }

    public int getPositionActuelle() {
        return positionActuelle;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isVictoire() {
        return victoire;
    }

    public void setVictoire(boolean victoire) {
        this.victoire = victoire;
    }

    public TextView getTvDefinition() {
        return tvDefinition;
    }

    public void setTvDefinition(TextView tvDefinition) {
        this.tvDefinition = tvDefinition;
    }

    public void setPositionActuelle(int positionActuelle) {
        this.positionActuelle = positionActuelle;
    }

    public Grille getGrille() {
        return grille;
    }

    public void setGrille(Grille grille) {
        this.grille = grille;
    }
}