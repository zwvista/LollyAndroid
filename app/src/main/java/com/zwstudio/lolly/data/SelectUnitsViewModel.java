package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Book;
import com.zwstudio.lolly.domain.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SelectUnitsViewModel {

    public RepoLanguage repoLanguage;
    public RepoBook repoBook;

    public List<Language> lstLanguages;
    public List<Book> lstBooks = new ArrayList<>();
    private int currentLanguageIndex;
    public int currentBookIndex;

    public SelectUnitsViewModel(DBHelper db) {
        repoBook = new RepoBook(db);
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
        lstBooks = repoBook.getDataByLang(m.langid);
        currentBookIndex = IntStream.range(0, lstBooks.size())
            .filter(i -> lstBooks.get(i).bookid == m.curbookid)
            .findFirst().getAsInt();
    }

    public Book getCurrentBook() {
        return lstBooks.get(currentBookIndex);
    }
}
