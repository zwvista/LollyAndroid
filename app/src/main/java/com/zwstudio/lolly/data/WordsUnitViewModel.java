package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Textbook;
import com.zwstudio.lolly.domain.UnitWord;

import java.util.List;

public class WordsUnitViewModel {

    public RepoUnitWord repoUnitWord;
    public SettingsViewModel settingsViewModel;

    public List<UnitWord> lstWords;

    public WordsUnitViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoUnitWord = new RepoUnitWord(db);
        Textbook m = settingsViewModel.lstTextbooks.get(settingsViewModel.currentTextbookIndex);
        lstWords = repoUnitWord.getDataByTextbookUnitParts(m.id, m.usunitfrom * 10 + m.uspartfrom,
                m.usunitto * 10 + m.uspartto);
    }

}
