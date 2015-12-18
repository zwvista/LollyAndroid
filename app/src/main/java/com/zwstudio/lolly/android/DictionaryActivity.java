package com.zwstudio.lolly.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.zwstudio.lolly.domain.DictAll;

import java.util.List;

public class DictionaryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        final ListView lv = (ListView) findViewById(R.id.listView);
        final List<DictAll> lst = getLollyViewModel().lstDictAll;
        final ArrayAdapter<DictAll> adapter = new ArrayAdapter<DictAll>(this,
                R.layout.item_dictionary, android.R.id.text1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
                View v = DictionaryActivity.this.getLayoutInflater().inflate(
                        R.layout.item_dictionary, parent, false
                );
                DictAll m = lst.get(position);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(m.dictname);
                ctv.setChecked(lv.isItemChecked(position));
                TextView tv = (TextView) v.findViewById(android.R.id.text2);
                tv.setText(m.url);
                return v;
            }
        };
        lv.setAdapter(adapter);

        lv.setItemChecked(getLollyViewModel().currentDictIndex, true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getLollyViewModel().currentDictIndex = position;
                Log.d("", String.format("Checked position:%d", position));
                adapter.notifyDataSetChanged();
            }
        });
    }

}
