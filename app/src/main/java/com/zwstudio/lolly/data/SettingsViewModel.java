package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Book;
import com.zwstudio.lolly.domain.DictAll;
import com.zwstudio.lolly.domain.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SettingsViewModel {

    public RepoDictAll repoDictAll;
    public RepoDictionary repoDictionary;
    public RepoLanguage repoLanguage;
    public RepoBook repoBook;

    public List<Language> lstLanguages;
    private int currentLanguageIndex;
    public List<DictAll> lstDictAll = new ArrayList<>();
    public int currentDictIndex;
    public String word = "";
    public List<Book> lstBooks = new ArrayList<>();
    public int currentBookIndex;

    public SettingsViewModel(DBHelper db) {
        repoDictAll = new RepoDictAll(db);
        repoDictionary = new RepoDictionary(db);
        repoLanguage = new RepoLanguage(db);
        repoBook = new RepoBook(db);

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
        lstBooks = repoBook.getDataByLang(m.langid);
        currentBookIndex = IntStream.range(0, lstBooks.size())
                .filter(i -> lstBooks.get(i).bookid == m.curbookid)
                .findFirst().getAsInt();
    }

    public DictAll getCurrentDict() {
        return lstDictAll.get(currentDictIndex);
    }

    public Book getCurrentBook() {
        return lstBooks.get(currentBookIndex);
    }
}
