package com.zwstudio.lolly.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zwstudio.lolly.data.PhrasesUnitsViewModel;
import com.zwstudio.lolly.domain.UnitPhrase;

import java.util.List;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_phrases_units)
public class PhrasesUnitsActivity extends DrawerListActivity {

    PhrasesUnitsViewModel phrasesUnitsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phrasesUnitsViewModel = new PhrasesUnitsViewModel(getDBHelper(), getSettingsViewModel());
        List<UnitPhrase> lst = phrasesUnitsViewModel.lstPhrases;
        ArrayAdapter<UnitPhrase> adapter = new ArrayAdapter<UnitPhrase>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(lst.get(position).phrase);
                tv.setTextColor(Color.rgb(255, 165, 0));
                tv = (TextView) v.findViewById(android.R.id.text2);
                tv.setText(lst.get(position).translation);
                tv.setTextColor(Color.BLUE);
                return v;
            }
        };
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WordActivity.class);
                intent.putExtra("word", lst.get(position).phrase);
                startActivity(intent);
            }
        });
    }

}
