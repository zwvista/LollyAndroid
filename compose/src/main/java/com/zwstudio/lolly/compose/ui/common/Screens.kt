package com.zwstudio.lolly.compose.ui.common

sealed class TopScreens(val route: String) {
    object AppMain : TopScreens("AppMain")
    object Login : TopScreens("Login")
}

sealed class WordsScreens(val route: String) {
    object WordsUnitMain : WordsScreens("WordsUnitMain")
    object WordsUnitDetail : WordsScreens("WordsUnitDetail")
    object WordsUnitAdd : WordsScreens("WordsUnitAdd")
    object WordsUnitBatchEdit : WordsScreens("WordsUnitBatchEdit")
    object WordsTextbookMain : WordsScreens("WordsTextbookMain")
    object WordsTextbookDetail : WordsScreens("WordsTextbookDetail")
    object WordsLangMain : WordsScreens("WordsLangMain")
    object WordsLangDetail : WordsScreens("WordsLangDetail")
    object WordsLangAdd : WordsScreens("WordsLangAdd")
    object WordsDict : WordsScreens("WordsDict")
}

sealed class PhrasesScreens(val route: String) {
    object PhrasesUnitMain : PhrasesScreens("PhrasesUnitMain")
    object PhrasesUnitDetail : PhrasesScreens("PhrasesUnitDetail")
    object PhrasesUnitAdd : PhrasesScreens("PhrasesUnitAdd")
    object PhrasesUnitBatchEdit : WordsScreens("PhrasesUnitBatchEdit")
    object PhrasesTextbookMain : PhrasesScreens("PhrasesTextbookMain")
    object PhrasesTextbookDetail : PhrasesScreens("PhrasesTextbookDetail")
    object PhrasesLangMain : PhrasesScreens("PhrasesLangMain")
    object PhrasesLangDetail : PhrasesScreens("PhrasesLangDetail")
    object PhrasesLangAdd : PhrasesScreens("PhrasesLangAdd")
}

sealed class PatternsScreens(val route: String) {
    object PatternsMain : PatternsScreens("PatternsMain")
    object PatternsDetail : PatternsScreens("PatternsDetail")
    object PatternsWebPage : PatternsScreens("PatternsWebPage")
}

sealed class ReviewScreens(val route: String) {
    object WordsReviewMain : ReviewScreens("WordsReviewMain")
    object PhrasesReviewMain : ReviewScreens("PhrasesReviewMain")
    object ReviewOptions : ReviewScreens("ReviewOptions")
}

sealed class OnlineTextbooksScreens(val route: String) {
    object OnlineTextbooksMain : PatternsScreens("OnlineTextbooksMain")
    object OnlineTextbooksDetail : PatternsScreens("OnlineTextbooksDetail")
    object OnlineTextbooksWebPage : PatternsScreens("OnlineTextbooksWebPage")
}

sealed class UnitBlogPostsScreens(val route: String) {
    object UnitBlogPosts : UnitBlogPostsScreens("UnitBlogPosts")
}

sealed class LangBlogGroupsScreens(val route: String) {
    object LangBlogGroups : LangBlogGroupsScreens("LangBlogGroups")
    object LangBlogGroupsDetail : LangBlogGroupsScreens("LangBlogGroupsDetail")
    object LangBlogPostsContent : LangBlogGroupsScreens("LangBlogPostsContent")
    object LangBlogPostsDetail : LangBlogGroupsScreens("LangBlogPostsDetail")
    object LangBlogPostsList : LangBlogGroupsScreens("LangBlogPostsList")
}

const val INDEX_KEY = "INDEX_KEY"
