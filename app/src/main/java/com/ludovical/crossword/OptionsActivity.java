package com.ludovical.crossword;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;
import java.util.ArrayList;
import static com.ludovical.crossword.SharedPreferencesManager.*;
import static com.ludovical.crossword.Utils.determineTheme;

public class OptionsActivity extends Activity {

    private Switch skipFilledSwitch;
    private Switch skipSingleLetterSwitch;
    private Switch checkErrorSwitch;
    private Switch revealAnswerSwitch;
    private ArrayList<RadioButton> themeRadioButtonList;
    private int currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme(this, false);
        setContentView(R.layout.activity_options);
        this.skipFilledSwitch = (Switch)findViewById(R.id.skipFilledSwitch);
        this.skipSingleLetterSwitch = (Switch)findViewById(R.id.skipSingleLetterSwitch);
        this.checkErrorSwitch = (Switch)findViewById(R.id.checkErrorsSwitch);
        this.revealAnswerSwitch = (Switch)findViewById(R.id.revealAnswerSwitch);
        this.themeRadioButtonList = new ArrayList<>();
        this.themeRadioButtonList.add((RadioButton) findViewById(R.id.radioButtonTheme1));
        this.themeRadioButtonList.add((RadioButton) findViewById(R.id.radioButtonTheme2));
        this.themeRadioButtonList.add((RadioButton) findViewById(R.id.radioButtonTheme3));
        this.themeRadioButtonList.add((RadioButton) findViewById(R.id.radioButtonTheme4));
        this.currentTheme = 0;
        ouvertureOptions();
    }

    @Override
    protected void onDestroy() {
        exporterOptionBoolean(OptionsActivity.this, skipFilledSwitch.isChecked(), "optionSkipFilled");
        exporterOptionBoolean(OptionsActivity.this, skipSingleLetterSwitch.isChecked(), "optionSkipSingleLetter");
        exporterOptionBoolean(OptionsActivity.this, revealAnswerSwitch.isChecked(), "optionRevealAnswer");
        exporterOptionBoolean(OptionsActivity.this, checkErrorSwitch.isChecked(), "optionCheckError");
        for (int i = 0, taille = themeRadioButtonList.size(); i < taille; i++) {
            if (themeRadioButtonList.get(i).isChecked()) {
                exporterOptionInt(OptionsActivity.this, i, "optionTheme");
                if (i != currentTheme) {
                    Toast.makeText(OptionsActivity.this, R.string.themeChanged, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        super.onDestroy();
    }

    //Lors d'un clic sur le bouton "close"
    public void fermetureOptions(View v) {
        finish();
    }

    //Méthode appelée à l'ouverture de l'activité afin de charger les préférences enregistrées.
    private void ouvertureOptions() {
        skipFilledSwitch.setChecked(getBoolData("optionSkipFilled", OptionsActivity.this));
        skipSingleLetterSwitch.setChecked(getBoolData("optionSkipSingleLetter", OptionsActivity.this));
        revealAnswerSwitch.setChecked(getBoolData("optionRevealAnswer", OptionsActivity.this));
        checkErrorSwitch.setChecked(getBoolData("optionCheckError", OptionsActivity.this));
        currentTheme = getIntData("optionTheme", OptionsActivity.this);
        for (int i = 0, taille = themeRadioButtonList.size(); i < taille; i++) {
            if (i == currentTheme) {
                themeRadioButtonList.get(i).setChecked(true);
                currentTheme = i;
                break;
            }
        }
    }
}
