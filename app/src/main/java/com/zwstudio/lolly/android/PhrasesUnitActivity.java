package com.zwstudio.lolly.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zwstudio.lolly.data.PhrasesUnitViewModel;
import com.zwstudio.lolly.domain.UnitPhrase;

import java.util.List;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_phrases_unit)
public class PhrasesUnitActivity extends DrawerListActivity {

    PhrasesUnitViewModel phrasesUnitViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phrasesUnitViewModel = new PhrasesUnitViewModel(getDBHelper(), getSettingsViewModel());
        List<UnitPhrase> lst = phrasesUnitViewModel.lstPhrases;
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
                Intent intent = new Intent(getApplicationContext(), WordsDictActivity.class);
                intent.putExtra("word", lst.get(position).phrase);
                startActivity(intent);
            }
        });
    }

}
