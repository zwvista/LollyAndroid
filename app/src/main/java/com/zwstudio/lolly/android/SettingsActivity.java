package com.zwstudio.lolly.android;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
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
        initSpnLanguage();
        initSpnDictionary();
    }

    private void initSpnLanguage() {
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
        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLollyViewModel().setCurrentLanguageIndex(position);
                Log.d("", String.format("Checked position:%d", position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpnDictionary() {
        final List<DictAll> lst = getLollyViewModel().lstDictAll;
        final ArrayAdapter<DictAll> adapter = new ArrayAdapter<DictAll>(this,
                R.layout.item_dictionary, android.R.id.text1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                DictAll m = lst.get(position);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(m.dictname);
                ctv.setChecked(spnDictionary.getSelectedItemPosition() == position);
                TextView tv = (TextView) v.findViewById(android.R.id.text2);
                tv.setText(m.url);
                return v;
            }
        };
        spnDictionary.setAdapter(adapter);

        spnDictionary.setSelection(getLollyViewModel().currentDictIndex);
        spnDictionary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLollyViewModel().currentDictIndex = position;
                Log.d("", String.format("Checked position:%d", position));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
