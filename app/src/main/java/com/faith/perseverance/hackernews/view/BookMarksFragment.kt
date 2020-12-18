package com.faith.perseverance.hackernews.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.Article
import com.faith.perseverance.hackernews.model.ArticleViewModel

class BookMarksFragment: Fragment(), CellClickListener {

    private val viewModel by viewModels< ArticleViewModel>()
    private lateinit var articleAdapter: ArticleAdapter
    private val TAG: String = "BookMarksFragment"

    companion object {

        fun newInstance(): BookMarksFragment {
            return BookMarksFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.bookmarks_fragment, container, false)



        val recyclerView = view.findViewById<RecyclerView>(R.id.book_marks_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val observer : Observer<List<Article>> =
                object : Observer<List<Article>> {
                    override fun onChanged(hits: List<Article>) {
                        Log.v(TAG, "on changed: ${hits}")

                        articleAdapter = ArticleAdapter(
                                hits,
                                this@BookMarksFragment
                        )
                        recyclerView.adapter = articleAdapter
                    }
                }
        viewModel.articles.observe(viewLifecycleOwner, observer)

        return view
    }

    override fun onCellClickListener(article: Article) {
        Toast.makeText(context,"${article.title}",Toast.LENGTH_SHORT).show()
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