package com.faith.perseverance.hackernews.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.Article
import com.faith.perseverance.hackernews.model.ArticleViewModel
import com.faith.perseverance.hackernews.model.NetworkManager




class MainActivity : AppCompatActivity(), CellClickListener {

    private var TAG: String = "OnCreate"

    private val viewModel  by viewModels<ArticleViewModel>()
    private lateinit var articleAdapter: ArticleAdapter


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v(TAG, "onCreate")

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val button: Button = findViewById(R.id.button)

        var networkManager = NetworkManager(context = baseContext)

        button.setOnClickListener {
            Log.v(TAG, "button clicked")
            if(networkManager.connected)
            {

                viewModel.getArticles()
            } else {
                var toast: Toast = Toast.makeText(this, "Connect to the network.",Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        val observer : Observer<List<Article>> =
            object : Observer<List<Article>> {
            override fun onChanged(hits: List<Article>) {
                Log.v(TAG, "on changed: ${hits}")
                recyclerView.layoutManager = LinearLayoutManager(baseContext)
                articleAdapter = ArticleAdapter(hits, context = this@MainActivity, this@MainActivity)
                recyclerView.adapter = articleAdapter
            }
        }

        viewModel.articles.observe(this, observer)

    }

    override fun onCellClickListener(article: Article) {
        Toast.makeText(this, article.url, Toast.LENGTH_SHORT).show()
        var intent = Intent(android.content.Intent.ACTION_VIEW)
        intent.data = Uri.parse(article.url)
        startActivity(intent)
    }


}