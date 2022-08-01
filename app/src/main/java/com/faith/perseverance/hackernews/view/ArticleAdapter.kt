package com.faith.perseverance.hackernews.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.Article

/**
 * ArticleAdapter displays a list of articles in the recyclerview
 * of HomeActivity. Provides the article selected to the HomeActivity
 * with the delegate pattern using the CellClickListener interface
 *
 * @param cellClickListener CellClickListener
 */

class ArticleAdapter(private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    var data: MutableList<Article>? = null


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //private var TAG = "ViewHolder"
        val articleTitle: TextView
        val articlePoints: TextView
        val linearlayout: LinearLayoutCompat

        init {
            articleTitle = view.findViewById(R.id.article_title)
            articlePoints = view.findViewById(R.id.article_points)
            linearlayout = view.findViewById(R.id.linearlayout)
        }

    }

    fun addHits(hits: MutableList<Article>?) {
        if (hits != null) {
            data = hits
        }
        notifyDataSetChanged()
    }

    //Creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        var title = ""
        var points = ""
        var article = Article(url = "https://www.google.com")

        if (data != null) {
            title = data!![position].title
            points = "Points: ${data!![position].points}"
            article = data!![position]
        } else {
            title = "Something went wrong ... "
        }
        viewHolder.articleTitle.text = title
        viewHolder.articlePoints.text = points

        viewHolder.linearlayout.setOnClickListener {
            cellClickListener.onCellClickListener(article)
        }

    }

    override fun getItemCount(): Int {
        if (data != null) {
            return data!!.size
        } else {
            return 1
        }
    }

    fun removeAt(position: Int): Article? {
        //retrieve reference to article from data
        val article = data?.get(position)
        //remove article from data
        data?.removeAt(position)
        //update recyclerView
        notifyDataSetChanged()

        return article
    }

}

/**
 * CellClickListener interface provides delegate pattern between the article
 */
interface CellClickListener {
    fun onCellClickListener(article: Article)

}