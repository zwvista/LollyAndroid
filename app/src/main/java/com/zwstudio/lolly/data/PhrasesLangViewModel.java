package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.LangPhrase;
import com.zwstudio.lolly.domain.TextBook;

import java.util.List;

public class PhrasesLangViewModel {

    public RepoLangPhrase repoLangPhrase;
    public SettingsViewModel settingsViewModel;

    public List<LangPhrase> lstPhrases;

    public PhrasesLangViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoLangPhrase = new RepoLangPhrase(db);
        TextBook m = settingsViewModel.lstTextBooks.get(settingsViewModel.currentTextBookIndex);
        lstPhrases = repoLangPhrase.getDataByLang(m.langid);
    }

}
