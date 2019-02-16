package com.zwstudio.lolly.android

import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ProgressBar
import com.woxthebox.draglistview.DragListView
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_drawer)
class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @ViewById
    lateinit var toolbar: Toolbar
    @ViewById
    lateinit var fab: FloatingActionButton
    @ViewById(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout
    @ViewById(R.id.nav_view)
    lateinit var navigationView: NavigationView

    private lateinit var drawerToggle: ActionBarDrawerToggle

    @AfterViews
    fun afterViews() {
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        drawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        showFragment(SearchFragment_())
    }

    private fun showFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_search -> showFragment(SearchFragment_())
            R.id.nav_settings -> showFragment(SettingsFragment_())
            R.id.nav_words_unit -> showFragment(WordsUnitFragment_())
            R.id.nav_words_textbook -> showFragment(WordsTextbookFragment_())
            R.id.nav_words_lang -> showFragment(WordsLangFragment_())
            R.id.nav_phrases_unit -> showFragment(PhrasesUnitFragment_())
            R.id.nav_phrases_lang -> showFragment(PhrasesLangFragment_())
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

@EFragment
class DrawerListFragment : Fragment() {

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout
    @ViewById
    lateinit var progressBar1: ProgressBar

    val compositeDisposable = CompositeDisposable()

}
