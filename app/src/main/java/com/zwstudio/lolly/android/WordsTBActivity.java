package com.zwstudio.lolly.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zwstudio.lolly.data.WordsTBViewModel;
import com.zwstudio.lolly.domain.TBWord;

import java.util.List;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_words_tb)
public class WordsTBActivity extends DrawerListActivity {

    WordsTBViewModel wordsTBViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wordsTBViewModel = new WordsTBViewModel(getDBHelper(), getSettingsViewModel());
        List<TBWord> lst = wordsTBViewModel.lstWords;
        ArrayAdapter<TBWord> adapter = new ArrayAdapter<TBWord>(this,
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
