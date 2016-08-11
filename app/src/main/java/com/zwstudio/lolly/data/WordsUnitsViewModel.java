package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.TextBook;
import com.zwstudio.lolly.domain.UnitWord;

import java.util.List;

public class WordsUnitsViewModel {

    public RepoUnitWord repoUnitWord;
    public SettingsViewModel settingsViewModel;

    public List<UnitWord> lstWords;

    public WordsUnitsViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoUnitWord = new RepoUnitWord(db);
        TextBook m = settingsViewModel.lstTextBooks.get(settingsViewModel.currentTextBookIndex);
        lstWords = repoUnitWord.getDataByTextBookUnitParts(m.id, m.usunitfrom * 10 + m.uspartfrom,
                m.usunitto * 10 + m.uspartto);
    }

}
