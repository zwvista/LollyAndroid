package com.zwstudio.lolly.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zwstudio.lolly.domain.DictAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryActivity extends BaseActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        lv = (ListView)findViewById(R.id.listView);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        List<DictAll> lst = getLollyViewModel().lstDictAll;
        final List<Map<String, String>> lst2 = new ArrayList<Map<String, String>>();
        for (DictAll m : lst) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", m.dictname);
            map.put("url", m.url);
            lst2.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, lst2, R.layout.item_dictionary,
                new String[] {"name", "url"},
                new int[] {android.R.id.text1, android.R.id.text2}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setChecked(lv.isItemChecked(position));
                return v;
            }
        };
        lv.setAdapter(adapter);

        lv.setItemChecked(getLollyViewModel().currentDictIndex, true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getLollyViewModel().currentDictIndex = position;
                Log.d("", String.format("Checked position:%d", lv.getCheckedItemPosition()));
            }
        });
    }

}
