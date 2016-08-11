package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.TBWord;
import com.zwstudio.lolly.domain.TextBook;

import java.util.List;

public class WordsTBViewModel {

    public RepoTBWord repoTBWord;
    public SettingsViewModel settingsViewModel;

    public List<TBWord> lstWords;

    public WordsTBViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoTBWord = new RepoTBWord(db);
        TextBook m = settingsViewModel.lstTextBooks.get(settingsViewModel.currentTextBookIndex);
        lstWords = repoTBWord.getDataByLang(m.langid);
    }

}
