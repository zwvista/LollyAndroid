package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.TextbookWord;
import com.zwstudio.lolly.domain.Textbook;

import java.util.List;

public class WordsTextbookViewModel {

    public RepoTextbookWord repoTextbookWord;
    public SettingsViewModel settingsViewModel;

    public List<TextbookWord> lstWords;

    public WordsTextbookViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoTextbookWord = new RepoTextbookWord(db);
        Textbook m = settingsViewModel.lstTextbooks.get(settingsViewModel.selectedTextbookIndex);
        lstWords = repoTextbookWord.getDataByLang(m.langid);
    }

}
