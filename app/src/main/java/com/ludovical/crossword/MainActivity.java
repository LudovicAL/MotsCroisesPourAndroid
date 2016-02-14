package com.ludovical.crossword;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import static com.ludovical.crossword.Utils.*;

public class MainActivity extends Activity {
    private ListView lv;
    private ListViewAdapter aAdapter;
    public ArrayList<GrilleMenuItem> listeDesGrilles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme(this, false);
        setContentView(R.layout.activity_main);
        listeDesGrilles = recupererGrilles();
        lv = (ListView) findViewById(R.id.listView);
        aAdapter = new ListViewAdapter(this, listeDesGrilles);
        lv.setAdapter(aAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aAdapter.demarerJeu(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent nouveau = new Intent(this, OptionsActivity.class);
            ((Activity) this).startActivity(nouveau);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Lorsque l'utilisateur ferme l'activité contenant la fenêtre de jeu (la fenêtre avec la grille)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Vérifier le code de l’activité
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                String message = data.getStringExtra("retour");
                //Faire quelque chose ici avec le message de victoire ou défaite...
            }
        }
    }

    //Méthode appelée au démarrage de l'application afin de récupérer la liste des toutes les grilles disponibles
    private ArrayList<GrilleMenuItem> recupererGrilles() {
        ArrayList<GrilleMenuItem> ldg = new ArrayList<>();
        AssetManager am = this.getAssets();
        String[] grilles;
        try {
            grilles = am.list("grilles");
            for (int i = 0, nbGrilles = grilles.length; i < nbGrilles; i++) {
                ldg.add(new GrilleMenuItem(grilles[i].substring(0, grilles[i].length() - 4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ldg;
    }
}