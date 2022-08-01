package com.faith.perseverance.hackernews.view

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.Article
import com.faith.perseverance.hackernews.model.ArticleViewModel

/**
 * BookMarksFragment displays a recyclerview of saved bookmarks that are stored in the Room
 * database locally on the device. The Application parameter is required so that the Fragment can access the
 * viewmodel class. The supportFragmentManager parameter allows the Fragment to display a
 * WebViewFragment with a link to the saved article.
 *
 * @param application
 * @param supportFragmentManager
 * @property viewModel
 * @property articleAdapter
 *
 */

class BookMarksFragment(val application: Application, val supportFragmentManager: FragmentManager) :
        Fragment(), CellClickListener {

    private val viewModel by activityViewModels<ArticleViewModel>()
    private lateinit var articleAdapter: ArticleAdapter
    private val TAG: String = "BookMarksFragment"

    companion object {

        fun newInstance(application: Application, supportFragmentManager: FragmentManager): BookMarksFragment {
            return BookMarksFragment(application, supportFragmentManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.bookmarks_fragment, container, false)

        (activity as HomeActivity).setActionBarTitle("Bookmarks")

        val recyclerView = view.findViewById<RecyclerView>(R.id.book_marks_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        articleAdapter = ArticleAdapter(this@BookMarksFragment)

        recyclerView.adapter = articleAdapter


        //call back to observe changes in room database and update the UI
        val observer: Observer<List<Article>> =
                object : Observer<List<Article>> {
                    override fun onChanged(bookMarks: List<Article>) {
                        articleAdapter.data = bookMarks.toMutableList()
                        recyclerView.adapter = articleAdapter

                    }
                }

        val swipeHandler = object : SwipeToDeleteCallback(context = application.applicationContext) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)

                //retrieve reference to the article was removed from the recyclerview after being swiped.
                var article = articleAdapter.removeAt(viewHolder.adapterPosition)

                //if the article is not null, delete the article from the local Room DB
                article?.let { viewModel.deleteBookMark(it) }

            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel.bookMarks.observe(viewLifecycleOwner, observer)

        return view
    }


    /* onCellClickListener passes the article selected from the recyclerview to the view model
    * and then to webfrgament where it can be displayed. */
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCellClickListener(article: Article) {
        // initialize webfragment
        val webfragment = WebViewFragment.newInstance()

        // generate fragment with bundle that contains url and title of article selected
        viewModel.setArticleSelected(article)

        //push fragment to display if only 1 fragment is displayed (1 webviewfrag at at time)
        if (supportFragmentManager.backStackEntryCount < 1) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_activity_layout, webfragment, "webView")
                    .commit()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.actionBar?.title = "Bookmarks"
    }


    // onPrepareOptionsMenu is used to hide the bottom available when the BookMarks are displayed
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        //hide share button
        menu.findItem(R.id.action_share).isVisible = false
        //hide bookmarks button
        menu.findItem(R.id.display_bookmarks).isVisible = false
        //hide save/bookmark button
        menu.findItem(R.id.action_bookmark).isVisible = false

    }

}