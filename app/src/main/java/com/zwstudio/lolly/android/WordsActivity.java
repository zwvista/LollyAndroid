package com.zwstudio.lolly.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zwstudio.lolly.data.WordsViewModel;
import com.zwstudio.lolly.domain.WordUnit;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_words)
public class WordsActivity extends DrawerActivity {

    @InjectView(R.id.listView)
    ListView lv;

    WordsViewModel wordsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wordsViewModel = new WordsViewModel(getDBHelper(), getSettingsViewModel());
        List<WordUnit> lst = wordsViewModel.lstWords;
        ArrayAdapter<WordUnit> adapter = new ArrayAdapter<WordUnit>(this,
                android.R.layout.simple_list_item_1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(lst.get(position).word);
                tv.setTextColor(Color.BLUE);
                return v;
            }
        };
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),WordActivity.class);
                intent.putExtra("word", lst.get(position).word);
                startActivity(intent);
            }
        });
    }

}
