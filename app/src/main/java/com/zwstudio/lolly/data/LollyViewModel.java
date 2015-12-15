package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.DictAll;
import com.zwstudio.lolly.domain.Language;

import java.util.ArrayList;
import java.util.List;

public class LollyViewModel {

    public RepoDictAll repoDictAll;
    public RepoDictionary repoDictionary;
    public RepoLanguage repoLanguage;

    public List<Language> lstLanguages;
    public List<DictAll> lstDictAll = new ArrayList<>();
    private int currentLanguageIndex = 2;
    private int currentDictIndex = 0;

    public LollyViewModel(DBHelper db)
    {
        repoDictAll = new RepoDictAll(db);
        repoDictionary = new RepoDictionary(db);
        repoLanguage = new RepoLanguage(db);
    }

    public int getCurrentLanguageIndex() {
        return currentLanguageIndex;
    }

    public void setCurrentLanguageIndex(int currentLanguageIndex) {
        this.currentLanguageIndex = currentLanguageIndex;
    }

    public int getCurrentDictIndex() {
        return currentDictIndex;
    }

    public void setCurrentDictIndex(int currentDictIndex) {
        this.currentDictIndex = currentDictIndex;
    }
}
