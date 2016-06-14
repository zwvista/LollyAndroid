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

import com.zwstudio.lolly.domain.Book;
import com.zwstudio.lolly.domain.DictAll;
import com.zwstudio.lolly.domain.Language;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_select_units)
public class SelectUnitsActivity extends BaseActivity {

    @InjectView(R.id.spnLanguage)
    Spinner spnLanguage;
    @InjectView(R.id.spnBook)
    Spinner spnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSpnLanguage();
    }

    private void initSpnLanguage() {
        final List<Language> lst = getSelectUnitsViewModel().lstLanguages;
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

        spnLanguage.setSelection(getSelectUnitsViewModel().getCurrentLanguageIndex());
        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWordsOnlineViewModel().setCurrentLanguageIndex(position);
                Log.d("", String.format("Checked position:%d", position));
                initSpnBook();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpnBook() {
        final List<Book> lst = getSelectUnitsViewModel().lstBooks;
        final ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(this,
                android.R.layout.simple_spinner_item, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Book m = lst.get(position);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(m.bookname);
                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Book m = lst.get(position);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(m.bookname);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnBook.setAdapter(adapter);

        spnBook.setSelection(getSelectUnitsViewModel().currentBookIndex);
        spnBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectUnitsViewModel().currentBookIndex = position;
                Log.d("", String.format("Checked position:%d", position));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}
