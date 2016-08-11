package com.zwstudio.lolly.android;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.lolly.domain.TextBook;
import com.zwstudio.lolly.domain.Dictionary;
import com.zwstudio.lolly.domain.Language;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_settings)
public class SettingsActivity extends DrawerActivity {

    @InjectView(R.id.spnLanguage)
    Spinner spnLanguage;
    @InjectView(R.id.spnDictionary)
    Spinner spnDictionary;
    @InjectView(R.id.spnTextBook)
    Spinner spnTextBook;
    @InjectView(R.id.spnUnitFrom)
    Spinner spnUnitFrom;
    @InjectView(R.id.spnUnitTo)
    Spinner spnUnitTo;
    @InjectView(R.id.spnPartFrom)
    Spinner spnPartFrom;
    @InjectView(R.id.spnPartTo)
    Spinner spnPartTo;
    @InjectView(R.id.chkUnitTo)
    CheckBox chkUnitTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSpnLanguage();
    }

    private void initSpnLanguage() {
        List<Language> lst = getSettingsViewModel().lstLanguages;
        ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(this,
                android.R.layout.simple_spinner_item, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(lst.get(position).langname);
                tv.setTextColor(Color.BLUE);
                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(lst.get(position).langname);
                ctv.setTextColor(Color.BLUE);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnLanguage.setAdapter(adapter);

        spnLanguage.setSelection(getSettingsViewModel().getCurrentLanguageIndex());
        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSettingsViewModel().setCurrentLanguageIndex(position);
                Log.d("", String.format("Checked position:%d", position));
                initSpnDictionary();
                initSpnTextBook();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpnDictionary() {
        List<Dictionary> lst = getSettingsViewModel().lstDictionary;
        ArrayAdapter<Dictionary> adapter = new ArrayAdapter<Dictionary>(this,
                android.R.layout.simple_spinner_item, android.R.id.text1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Dictionary m = lst.get(position);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(m.dictname);
                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Dictionary m = lst.get(position);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(m.dictname);
                ctv.setChecked(spnDictionary.getSelectedItemPosition() == position);
                TextView tv = (TextView) v.findViewById(android.R.id.text2);
                tv.setText(m.url);
                return v;
            }
        };
        adapter.setDropDownViewResource(R.layout.item_dictionary);
        spnDictionary.setAdapter(adapter);

        spnDictionary.setSelection(getSettingsViewModel().currentDictIndex);
        spnDictionary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSettingsViewModel().currentDictIndex = position;
                Log.d("", String.format("Checked position:%d", position));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpnTextBook() {
        List<TextBook> lst = getSettingsViewModel().lstTextBooks;
        ArrayAdapter<TextBook> adapter = new ArrayAdapter<TextBook>(this,
                android.R.layout.simple_spinner_item, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextBook m = lst.get(position);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(m.textbookname);
                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                TextBook m = lst.get(position);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(m.textbookname);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnTextBook.setAdapter(adapter);

        spnTextBook.setSelection(getSettingsViewModel().currentTextBookIndex);
        spnTextBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSettingsViewModel().currentTextBookIndex = position;
                Log.d("", String.format("Checked position:%d", position));
                adapter.notifyDataSetChanged();
                initUnitsAndParts();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initUnitsAndParts() {
        TextBook currentTextBook = getSettingsViewModel().getCurrentTextBook();
        chkUnitTo.setChecked(currentTextBook.usunitfrom != currentTextBook.usunitto);

        {
            List<String> lst = IntStream.rangeClosed(1, currentTextBook.units)
                    .mapToObj(i -> String.valueOf(i)).collect(Collectors.toList());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lst) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    String m = lst.get(position);
                    TextView tv = (TextView) v.findViewById(android.R.id.text1);
                    tv.setText(m);
                    return v;
                }
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    String m = lst.get(position);
                    CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                    ctv.setText(m);
                    return v;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spnUnitFrom.setAdapter(adapter);
            spnUnitTo.setAdapter(adapter);

            spnUnitFrom.setSelection(currentTextBook.usunitfrom - 1);
            spnUnitTo.setSelection(currentTextBook.usunitto - 1);
        }

        {
            String[] lst = currentTextBook.parts.split(" ");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lst) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    String m = lst[position];
                    TextView tv = (TextView) v.findViewById(android.R.id.text1);
                    tv.setText(m);
                    return v;
                }
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    String m = lst[position];
                    CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                    ctv.setText(m);
                    return v;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spnPartFrom.setAdapter(adapter);
            spnPartTo.setAdapter(adapter);

            spnPartFrom.setSelection(currentTextBook.uspartfrom - 1);
            spnPartTo.setSelection(currentTextBook.uspartto - 1);
        }
    }

}
