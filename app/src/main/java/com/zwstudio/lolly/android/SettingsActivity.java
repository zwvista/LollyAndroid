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
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.lolly.domain.DictAll;
import com.zwstudio.lolly.domain.Language;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {

    @InjectView(R.id.spnLanguage)
    Spinner spnLanguage;
    @InjectView(R.id.spnDictionary)
    Spinner spnDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<Language> lst = getLollyViewModel().lstLanguages;
        ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(this,
                android.R.layout.simple_list_item_single_choice, android.R.id.text1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(lst.get(position).langname);
                ctv.setTextColor(Color.BLUE);
                return v;
            }
        };
        spnLanguage.setAdapter(adapter);

        spnLanguage.setSelection(getLollyViewModel().getCurrentLanguageIndex());
        spnLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getLollyViewModel().setCurrentLanguageIndex(position);
                Log.d("", String.format("Checked position:%d", position));
            }
        });
    }

}
