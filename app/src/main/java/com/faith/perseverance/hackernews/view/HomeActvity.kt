package com.faith.perseverance.hackernews.view

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.Article
import com.faith.perseverance.hackernews.model.ArticleViewModel
import com.faith.perseverance.hackernews.model.ArticleViewModelFactory
import com.faith.perseverance.hackernews.model.ArticlesApplication

/**
 *  HomeActivity is the home page of the HackerNews App.
 *
 *  With a network connection, the app displays a list of
 *  articles from the front page of the hn.algolia.com site
 *  with a recyclerview that links to url in a WebViewFragment.
 *
 *  Home Activity displays a bookmarks button in the actionbar
 *  for storing book marks.
 *
 *  @property viewModel ArticleViewModel
 *  @property articleAdapter ArticleAdapter
 *  @property TAG String
 */
class HomeActivity : AppCompatActivity(), CellClickListener {

    private val TAG: String = "HomeActivity"
    private val viewModel: ArticleViewModel by viewModels {
        ArticleViewModelFactory((application as ArticlesApplication).repository)
    }
    private lateinit var articleAdapter: ArticleAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.my_toolbar))

        supportActionBar?.title = getString(R.string.app_name)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        val adapter = ArticleAdapter(this)
        recyclerView.adapter = adapter

        //set the article of the adapter with updated data
        val observer: Observer<List<Article>?> =
            Observer<List<Article>?> { hits -> adapter.addHits(hits?.toMutableList()) }
        viewModel.articles.observe(this, observer)
    }

    /**
     * onCellClickListener provides access to the article that is selected
     * from the recyclerview's adapter. When a user selects a article
     * in the adapter, this method provides the HomeActivity with access ot the article.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCellClickListener(article: Article) {

        // initialize webfragment
        val webFragment = WebViewFragment.newInstance()

        viewModel.setArticleSelected(article)

        //push fragment to display if only 1 fragment is displayed (1 webviewfrag at at time)
        if (supportFragmentManager.backStackEntryCount < 1) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_activity_layout, webFragment, getString(R.string.webviewtag))
                    .commit()
        }
    }


    //on back pressed removes displayed fragment and returns to home page
    override fun onBackPressed() {
        hideFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.display_bookmarks -> {
            val bookMarksFragment = BookMarksFragment.newInstance(application, supportFragmentManager)
            bookMarksFragment.tag

            showFragment(bookMarksFragment, "bookmark")
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_items, menu)
        val share = menu?.findItem(R.id.action_share)
        val bookmark = menu?.findItem(R.id.action_bookmark)

        // set book mark and share buttons to hide when HomeActivity is displayed
        bookmark?.isVisible = false
        share?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.backStackEntryCount < 1) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_placeholder, fragment, tag)
                    .commit()
        }
    }

    //TODO replace with a NavigationComponent
    /*
       hideFragment() - sets the action bar title based on the number of fragments
       that are in the supportFragmentManager.

       If there are two fragments in the stack - then the title is set to bookmarks when the user
       goes back because that's the only state that would allow this to happen.

       If there is one fragment and the user goes back then the app name is displayed
       in the in action.

       This needs to be replaced with something more scalable.
     */
    private fun hideFragment() {
        val count = supportFragmentManager.fragments.size

        if(count == 0 )
        {
            return
        }
        val fragment = supportFragmentManager.fragments[count - 1]

        if(fragment != null) {
            supportFragmentManager.beginTransaction()
                    .detach(fragment)
                    .commit()
        }

        if(count == 0) {
            return
        }
        else if(count == 1)
        {
            setActionBarTitle("HackerNews")
        } else if(count == 2)
        {
            setActionBarTitle("Bookmarks")
        }


    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}