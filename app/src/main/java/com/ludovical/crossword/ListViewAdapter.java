package com.ludovical.crossword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    public ArrayList<GrilleMenuItem> listeDesGrilles;
    private Context context;

    public ListViewAdapter(Context context, ArrayList<GrilleMenuItem> listeDesGrilles) {
        this.context = context;
        this.listeDesGrilles = listeDesGrilles;
    }

    @Override
    public int getCount() {
        return listeDesGrilles.size();
    }

    @Override
    public Object getItem(int position) {
        return listeDesGrilles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View elementLayout = convertView;
        if (elementLayout == null) {
            LayoutInflater li;
            li = LayoutInflater.from(context);
            elementLayout = li.inflate(R.layout.list_item, null);
        }
        TextView tv = (TextView)elementLayout.findViewById(R.id.genericTextView);
        tv.setText(listeDesGrilles.get(position).getNomGrille());
        final int pos = position;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demarerJeu(pos);
            }
        });

        return elementLayout;
    }

    //Méthode appelée afin de charger la grille choisie et ouvrir la fenêtre de jeu
    public void demarerJeu(int position) {
        Intent nouveau = new Intent(context, GrilleActivity.class);
        nouveau.putExtra("nomGrille", listeDesGrilles.get(position).getNomGrille());
        ((Activity) context).startActivityForResult(nouveau, 1);
    }
}