package com.zwstudio.lolly.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zwstudio.lolly.data.WordsTextbookViewModel;
import com.zwstudio.lolly.domain.TextbookWord;

import java.util.List;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_words_textbook)
public class WordsTextbookActivity extends DrawerListActivity {

    WordsTextbookViewModel wordsTextbookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wordsTextbookViewModel = new WordsTextbookViewModel(getDBHelper(), getSettingsViewModel());
        List<TextbookWord> lst = wordsTextbookViewModel.lstWords;
        ArrayAdapter<TextbookWord> adapter = new ArrayAdapter<TextbookWord>(this,
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
