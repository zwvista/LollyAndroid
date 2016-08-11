package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Dictionary;
import com.zwstudio.lolly.domain.Language;
import com.zwstudio.lolly.domain.TextBook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SettingsViewModel {

    public RepoDictionary repoDictionary;
    public RepoLanguage repoLanguage;
    public RepoTextBook repoTextBook;

    public List<Language> lstLanguages;
    private int currentLanguageIndex;
    public List<Dictionary> lstDictionary = new ArrayList<>();
    public int currentDictIndex;
    public String word = "";
    public List<TextBook> lstTextBooks = new ArrayList<>();
    public int currentTextBookIndex;

    public SettingsViewModel(DBHelper db) {
        repoDictionary = new RepoDictionary(db);
        repoLanguage = new RepoLanguage(db);
        repoTextBook = new RepoTextBook(db);

        lstLanguages = repoLanguage.getData();
        setCurrentLanguageIndex(2);
    }

    public int getCurrentLanguageIndex() {
        return currentLanguageIndex;
    }

    public void setCurrentLanguageIndex(int currentLanguageIndex) {
        this.currentLanguageIndex = currentLanguageIndex;
        Language m = lstLanguages.get(currentLanguageIndex);
        lstDictionary = repoDictionary.getDataByLang(m.id);
        currentDictIndex = 0;
        lstTextBooks = repoTextBook.getDataByLang(m.id);
        currentTextBookIndex = IntStream.range(0, lstTextBooks.size())
                .filter(i -> lstTextBooks.get(i).id == m.ustextbookid)
                .findFirst().orElse(-1);

        currentTextBookIndex = -1;
        for(int i = 0; i < lstTextBooks.size(); i++){
            if(lstTextBooks.get(i).id == m.ustextbookid) {
                currentTextBookIndex = i;
                break;
            }
        }
    }

    public Dictionary getCurrentDict() {
        return lstDictionary.get(currentDictIndex);
    }

    public TextBook getCurrentTextBook() {
        return lstTextBooks.get(currentTextBookIndex);
    }
}
