package com.zwstudio.lolly.android;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zwstudio.lolly.domain.Language;

import java.util.List;

public class LanguageActivity extends BaseActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        lv = (ListView)findViewById(R.id.listView);
        List<Language> lst = getLollyViewModel().lstLanguages;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for(Language m : lst)
            adapter.add(m.langname);
        lv.setAdapter(adapter);
    }

}
