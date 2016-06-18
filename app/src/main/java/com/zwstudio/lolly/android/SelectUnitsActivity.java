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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.lolly.domain.Book;
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
    @InjectView(R.id.edtUnitFrom)
    EditText edtUnitFrom;
    @InjectView(R.id.edtUnitTo)
    EditText edtUnitTo;
    @InjectView(R.id.spnUnitFrom)
    Spinner spnUnitFrom;
    @InjectView(R.id.spnUnitTo)
    Spinner spnUnitTo;
    @InjectView(R.id.chkUnitTo)
    CheckBox chkUnitTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSpnLanguage();
    }

    private void initSpnLanguage() {
        List<Language> lst = getSelectUnitsViewModel().lstLanguages;
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
                getSelectUnitsViewModel().setCurrentLanguageIndex(position);
                Log.d("", String.format("Checked position:%d", position));
                initSpnBook();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpnBook() {
        List<Book> lst = getSelectUnitsViewModel().lstBooks;
        ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(this,
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
                initUnits();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initUnits() {
        Book currentBook = getSelectUnitsViewModel().getCurrentBook();
        edtUnitFrom.setText(String.format("%d", currentBook.unitfrom));
        edtUnitTo.setText(String.format("%d", currentBook.unitto));
        chkUnitTo.setChecked(currentBook.unitfrom != currentBook.unitto);

        String[] lst = currentBook.parts.split(" ");
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
        spnUnitFrom.setAdapter(adapter);
        spnUnitTo.setAdapter(adapter);

        spnUnitFrom.setSelection(currentBook.partfrom - 1);
        spnUnitTo.setSelection(currentBook.partto - 1);
    }

}
