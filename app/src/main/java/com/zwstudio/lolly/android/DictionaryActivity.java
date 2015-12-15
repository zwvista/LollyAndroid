package com.zwstudio.lolly.android;

import android.os.Bundle;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for(DictAll m : lst)
            adapter.add(m.dictname);
        lv.setAdapter(adapter);
    }

}
