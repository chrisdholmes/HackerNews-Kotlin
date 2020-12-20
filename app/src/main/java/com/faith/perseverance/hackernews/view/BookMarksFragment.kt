package com.faith.perseverance.hackernews.view

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.Article
import com.faith.perseverance.hackernews.model.ArticleViewModel
import com.faith.perseverance.hackernews.model.ArticlesApplication

class BookMarksFragment(val application: Application, val supportFragmentManager: FragmentManager) : Fragment(), CellClickListener {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.bookmarks_fragment, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.book_marks_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val observer : Observer<List<Article>> =
                object : Observer<List<Article>> {
                    override fun onChanged(bookMarks: List<Article>) {

                        articleAdapter = ArticleAdapter(
                                bookMarks,
                                this@BookMarksFragment
                        )
                        recyclerView.adapter = articleAdapter
                    }
                }
        viewModel.bookMarks.observe(viewLifecycleOwner, observer)

        return view
    }

    override fun onCellClickListener(article: Article) {
        // initialize webfragment
        val webfragment = WebViewFragment.newInstance(application as ArticlesApplication)
        // generate fragment with bundle that contains url and title of article selected

        viewModel.setArticleSelected(article)

        //push fragment to display if only 1 fragment is displayed (1 webviewfrag at at time)
        if(supportFragmentManager.backStackEntryCount < 1) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_activity_layout, webfragment, "webView")
                .commit()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        //hide share button
        menu.findItem(R.id.action_share).isVisible = false
        //hide bookmarks button
        menu.findItem(R.id.display_bookmarks).isVisible = false
        //hide save/bookmark button
        menu.findItem(R.id.action_bookmark).isVisible = false


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.run{

        }
    }

}