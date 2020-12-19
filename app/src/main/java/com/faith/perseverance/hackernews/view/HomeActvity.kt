package com.faith.perseverance.hackernews.view

import android.os.Build
import android.os.Bundle
import android.util.Log
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
 *  @property viewModel
 *  @property articleAdapter
 *  @property TAG
 */
class HomeActvity : AppCompatActivity(), CellClickListener {

    private var TAG: String = "OnCreate"
    private val viewModel: ArticleViewModel by viewModels {
        ArticleViewModelFactory((application as ArticlesApplication).repository)
    }
    private lateinit var articleAdapter: ArticleAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = "Hacker News"

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)

        val observer : Observer<List<Article>> =
            object : Observer<List<Article>> {
            override fun onChanged(hits: List<Article>) {
                Log.v(TAG, "on changed: ${hits}")

                articleAdapter = ArticleAdapter(
                    hits,
                    this@HomeActvity
                )
                recyclerView.adapter = articleAdapter
            }
        }
        viewModel.articles.observe(this, observer)
    }

    override fun onCellClickListener(article: Article) {

        //initialize a Bundle for storing article information
        //function inherits from the custom OnCellClickListener interface in ArticleAdapter class
        val bundle = Bundle()
        bundle.putString("url", article.url)
        bundle.putString("title",article.title)

        // initialize webfragment
        var webfragment = WebViewFragment.newInstance()
        // generate fragment with bundle that contains url and title of article selected
        webfragment.arguments = bundle

        //push fragment to display if only 1 fragment is displayed (1 webviewfrag at at time)
        if(supportFragmentManager.backStackEntryCount < 1) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_activity_layout, webfragment, "webView")
                    .commit()
        }
    }

    //on back pressed removes displayed fragment and returns to home page
    override fun onBackPressed() {
        hideFragment()
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.display_bookmarks -> {
            var bookMarksFragment = BookMarksFragment.newInstance()
            showFragment(bookMarksFragment)
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

    private fun showFragment(fragment: Fragment)
    {
        if(supportFragmentManager.backStackEntryCount < 1) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_activity_layout, fragment)
                    .commit()
        }

    }

    private fun hideFragment()
    {
        if(supportFragmentManager.backStackEntryCount <= 1) {
            var fragment = supportFragmentManager.fragments.get(0)

            Log.v(TAG, "# of frags: ${supportFragmentManager.fragments.size}")

            if (fragment != null) {
                supportFragmentManager.beginTransaction().detach(fragment).commit()
            }
        }
    }
}