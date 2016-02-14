package com.ludovical.crossword;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import static com.ludovical.crossword.Utils.showNeutralMessage;

public class GridViewAdapterChamp extends BaseAdapter {
    private Context context;
    private TextView tvDefinition;
    private Champ champ;

    public GridViewAdapterChamp(Context context, Champ champ, Activity activity) {
        this.context = context;
        this.tvDefinition = (TextView) activity.findViewById(R.id.definition);
        this.champ = champ;
    }

    @Override
    public int getCount() {
        return champ.getNbLettres();
    }

    @Override
    public Object getItem(int position) {
        return champ.getListeDesCasesDuChamp().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View elementLayout;
        Case temp = champ.getListeDesCasesDuChamp().get(position);
        if (convertView == null) {
            if (champ.getListeDesCasesDuChamp().get(position).getView() != null) {
                return champ.getListeDesCasesDuChamp().get(position).getView();
            } else {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                elementLayout = inflater.inflate(R.layout.case_blanche, parent, false);
                EditText tv = (EditText) elementLayout.findViewById(R.id.genericEditText);
                tv.setShowSoftInputOnFocus(false);
                temp.setEt(tv);
                final int pos = position;
                temp.getEt().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            //gagneFocus(pos);
                        } else {
                            //perdFocus(pos);
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
                        /*
                        GridViewAdapterChamp.this.notifyDataSetChanged();
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
                        */
                    }
                });
                champ.getListeDesCasesDuChamp().get(position).setView(elementLayout);
            }
        } else {
            elementLayout = convertView;
        }
        return elementLayout;
    }
}