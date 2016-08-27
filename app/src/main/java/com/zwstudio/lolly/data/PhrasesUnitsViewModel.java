package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Textbook;
import com.zwstudio.lolly.domain.UnitPhrase;

import java.util.List;

public class PhrasesUnitsViewModel {

    public RepoUnitPhrase repoUnitPhrase;
    public SettingsViewModel settingsViewModel;

    public List<UnitPhrase> lstPhrases;

    public PhrasesUnitsViewModel(DBHelper db, SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        repoUnitPhrase = new RepoUnitPhrase(db);
        Textbook m = settingsViewModel.lstTextbooks.get(settingsViewModel.currentTextbookIndex);
        lstPhrases = repoUnitPhrase.getDataByTextbookUnitParts(m.id, m.usunitfrom * 10 + m.uspartfrom,
                m.usunitto * 10 + m.uspartto);
    }

}
