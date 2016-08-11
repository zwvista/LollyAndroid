package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.TextBook;
import com.zwstudio.lolly.domain.LangWord;

import java.util.List;

public class WordsLangViewModel {

    public RepoLangWord repoLangWord;
    public SettingsViewModel settingsViewModel;

    public List<LangWord> lstWords;

    public WordsLangViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoLangWord = new RepoLangWord(db);
        TextBook m = settingsViewModel.lstTextBooks.get(settingsViewModel.currentTextBookIndex);
        lstWords = repoLangWord.getDataByLang(m.langid);
    }

}
