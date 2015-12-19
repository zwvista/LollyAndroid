package com.zwstudio.lolly.android;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.zwstudio.lolly.domain.Language;

import java.util.List;

public class LanguageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        final ListView lv = (ListView) findViewById(R.id.listView);
        final List<Language> lst = getLollyViewModel().lstLanguages;
        ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(this,
                android.R.layout.simple_list_item_single_choice, android.R.id.text1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
                View v = LanguageActivity.this.getLayoutInflater().inflate(
                        android.R.layout.simple_list_item_single_choice, parent, false
                );
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(lst.get(position).langname);
                ctv.setTextColor(Color.BLUE);
                return v;
            }
        };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice);
//        for(Language m : lst)
//            adapter.add(m.langname);
        lv.setAdapter(adapter);

        lv.setItemChecked(getLollyViewModel().getCurrentLanguageIndex(), true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getLollyViewModel().setCurrentLanguageIndex(position);
                Log.d("", String.format("Checked position:%d", position));
            }
        });
    }

}
