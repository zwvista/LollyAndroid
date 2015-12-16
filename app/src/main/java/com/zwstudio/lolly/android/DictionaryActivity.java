package com.zwstudio.lolly.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zwstudio.lolly.domain.DictAll;

import java.util.List;

public class DictionaryActivity extends BaseActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        lv = (ListView)findViewById(R.id.listView);
        List<DictAll> lst = getLollyViewModel().lstDictAll;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice);
        for(DictAll m : lst)
            adapter.add(m.dictname);
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
