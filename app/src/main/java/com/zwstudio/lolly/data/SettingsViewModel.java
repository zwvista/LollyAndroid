package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Dictionary;
import com.zwstudio.lolly.domain.Language;
import com.zwstudio.lolly.domain.TextBook;
import com.zwstudio.lolly.domain.UserSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SettingsViewModel {

    public RepoDictionary repoDictionary;
    public RepoLanguage repoLanguage;
    public RepoTextBook repoTextBook;
    public RepoUserSetting repoUserSetting;

    public List<Language> lstLanguages;
    private int currentLangIndex;
    public List<Dictionary> lstDictionaries = new ArrayList<>();
    public int currentDictIndex;
    public String word = "";
    public List<TextBook> lstTextBooks = new ArrayList<>();
    public int currentTextBookIndex;

    public SettingsViewModel(DBHelper db) {
        repoDictionary = new RepoDictionary(db);
        repoLanguage = new RepoLanguage(db);
        repoTextBook = new RepoTextBook(db);
        repoUserSetting = new RepoUserSetting(db);

        lstLanguages = repoLanguage.getData();
        UserSetting m = repoUserSetting.getData();
        int currentLangIndex = IntStream.range(0, lstLanguages.size())
                .filter(i -> lstLanguages.get(i).id == m.uslangid)
                .findFirst().orElse(-1);
        setCurrentLanguageIndex(currentLangIndex);
    }

    public int getCurrentLanguageIndex() {
        return currentLangIndex;
    }

    public void setCurrentLanguageIndex(int currentLangIndex) {
        this.currentLangIndex = currentLangIndex;
        Language m = lstLanguages.get(currentLangIndex);
        lstDictionaries = repoDictionary.getDataByLang(m.id);
        currentDictIndex = IntStream.range(0, lstDictionaries.size())
                .filter(i -> lstDictionaries.get(i).id == m.usdictid)
                .findFirst().orElse(-1);
        lstTextBooks = repoTextBook.getDataByLang(m.id);
        currentTextBookIndex = IntStream.range(0, lstTextBooks.size())
                .filter(i -> lstTextBooks.get(i).id == m.ustextbookid)
                .findFirst().orElse(-1);
    }

    public Dictionary getCurrentDict() {
        return lstDictionaries.get(currentDictIndex);
    }

    public TextBook getCurrentTextBook() {
        return lstTextBooks.get(currentTextBookIndex);
    }
}
