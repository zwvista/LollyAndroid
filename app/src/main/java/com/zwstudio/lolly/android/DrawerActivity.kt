package com.zwstudio.lolly.android

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity
open class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    @AfterViews
    fun afterViews() {

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        })

        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_search) {
            val intent = Intent(this, SearchActivity_::class.java)
            startActivityForResult(intent, 0)
        } else if (id == R.id.nav_settings) {
            val intent = Intent(this, SettingsActivity_::class.java)
            startActivityForResult(intent, 0)
        } else if (id == R.id.nav_words_unit) {
            val intent = Intent(this, WordsUnitActivity_::class.java)
            startActivityForResult(intent, 0)
        } else if (id == R.id.nav_words_textbook) {
            val intent = Intent(this, WordsTextbookActivity_::class.java)
            startActivityForResult(intent, 0)
        } else if (id == R.id.nav_words_lang) {
            val intent = Intent(this, WordsLangActivity_::class.java)
            startActivityForResult(intent, 0)
        } else if (id == R.id.nav_phrases_unit) {
            val intent = Intent(this, PhrasesUnitActivity_::class.java)
            startActivityForResult(intent, 0)
        } else if (id == R.id.nav_phrases_lang) {
            val intent = Intent(this, PhrasesLangActivity_::class.java)
            startActivityForResult(intent, 0)
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

@EActivity
open class DrawerListActivity : DrawerActivity() {

    @ViewById
    lateinit var listView: ListView

}
