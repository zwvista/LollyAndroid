package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Book;
import com.zwstudio.lolly.domain.WordUnit;

import java.util.List;

public class WordsViewModel {

    public RepoWordUnit repoWordUnit;
    public SettingsViewModel settingsViewModel;

    public List<WordUnit> lstWords;

    public WordsViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoWordUnit = new RepoWordUnit(db);
        Book m = settingsViewModel.lstBooks.get(settingsViewModel.currentBookIndex);
        lstWords = repoWordUnit.getDataByBookUnitParts(m.bookid, m.unitfrom * 10 + m.partfrom,
                m.unitto * 10 + m.partto);
    }

}
