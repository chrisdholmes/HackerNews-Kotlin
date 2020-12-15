package com.faith.perseverance.hackernews.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.Article


class ArticleAdapter(private val dataSet: List<Article>?, private val context: Context): RecyclerView.Adapter<ArticleAdapter.ViewHolder>()
{

    private var TAG = "ArticleAdapter"

    private var url: String = ""



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private var TAG = "ViewHolder"
        val article_title: TextView
        val article_points: TextView


        init {
            // Define a click listener for hte ViewHolder's View ...
            article_title = view.findViewById(R.id.article_title)
            article_points = view.findViewById(R.id.article_points)

        }

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
        var url = ""
        if(dataSet != null) {

            title = dataSet.get(position).title
            points = "Points: ${dataSet.get(position).points}"
            url = dataSet.get(position).url


            Log.v(TAG,"ran $title + $points")

        } else {
            title = "$position title"

        }

        viewHolder.article_title.text = title
        viewHolder.article_points.text = points

    }


    override fun getItemCount(): Int {
        if (dataSet != null) {
            return dataSet.size
        } else {
            return 5
        }
    }

}

