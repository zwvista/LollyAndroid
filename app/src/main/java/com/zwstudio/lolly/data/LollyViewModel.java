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
    private int currentLanguageIndex;
    public int currentDictIndex;
    public String word = "";

    public LollyViewModel(DBHelper db) {
        repoDictAll = new RepoDictAll(db);
        repoDictionary = new RepoDictionary(db);
        repoLanguage = new RepoLanguage(db);

        lstLanguages = repoLanguage.getData();
        setCurrentLanguageIndex(2);
    }

    public int getCurrentLanguageIndex() {
        return currentLanguageIndex;
    }

    public void setCurrentLanguageIndex(int currentLanguageIndex) {
        this.currentLanguageIndex = currentLanguageIndex;
        Language m = lstLanguages.get(currentLanguageIndex);
        lstDictAll = repoDictAll.getDataByLang(m.langid);
        currentDictIndex = 0;
    }

    public DictAll getCurrentDict() {
        return lstDictAll.get(currentDictIndex);
    }
}
