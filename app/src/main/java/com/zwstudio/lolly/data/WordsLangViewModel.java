package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Textbook;
import com.zwstudio.lolly.domain.LangWord;

import java.util.List;

public class WordsLangViewModel {

    public RepoLangWord repoLangWord;
    public SettingsViewModel settingsViewModel;

    public List<LangWord> lstWords;

    public WordsLangViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoLangWord = new RepoLangWord(db);
        Textbook m = settingsViewModel.lstTextbooks.get(settingsViewModel.selectedTextbookIndex);
        lstWords = repoLangWord.getDataByLang(m.langid);
    }

}
