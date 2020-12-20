package com.faith.perseverance.hackernews.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.faith.perseverance.hackernews.R
import com.faith.perseverance.hackernews.model.ArticleViewModel
import com.faith.perseverance.hackernews.model.ArticlesApplication

/**
 * WebViewFragment displays selected url from articles
 * displayed in MainActivitiy. The article URL and Title
 * are passed to the Fragment using a Bundle.
 *
 * Share and Bookmark buttons are displayed the in the ActionBar
 * so an article can be saved or shared using Sharesheet.
 *
 */
class WebViewFragment(application: ArticlesApplication) : Fragment() {

    private var TAG = "WebViewFragment: "

    private val viewModel: ArticleViewModel by activityViewModels()

    companion object {

        fun newInstance(application: ArticlesApplication): WebViewFragment {
            return WebViewFragment(application)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //3
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val article = viewModel.getArticleSelected()

        //inflate web_view_fragment.xml in to the Fragment
        val view: View = inflater.inflate(R.layout.web_view_fragment, container, false)

        val webView = view.findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.canGoBack()

        webView.setWebViewClient(object : WebViewClient() {

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                Log.wtf(TAG, "onReceivedError: ${error?.description}")

                if (error != null) {
                    Toast.makeText(
                        activity?.baseContext,
                        "FAILED: ${error.description}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                activity?.supportFragmentManager?.beginTransaction()?.remove(this@WebViewFragment)
                    ?.commit()
            }
        })


        webView.loadUrl(article?.url)

        return view
    }

    /**
     * onPrepareOptionsMenu provides functionality for the menu buttons.
     * When WebViewFragment is displayed the ActionBar is set to display
     * a share and bookmark button. The ActionBar should hide the bookmarks
     * button.
     *
     * @param menu
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPrepareOptionsMenu(menu: Menu) {
        //Display share button
        menu.findItem(R.id.action_share).isVisible = true
        //hide bookmarks button
        menu.findItem(R.id.display_bookmarks).isVisible = false
        //display save/bookmark button
        menu.findItem(R.id.action_bookmark).isVisible = true

        val share = menu.findItem(R.id.action_share)

        val article = viewModel.getArticleSelected()
        //set the onclick listener of the share menu button to use Sharesheet

        share.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if(article != null) {
                    val shareSheet= Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, article.url)
                        putExtra(Intent.EXTRA_TITLE, article.title)
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        type="text/plain"
                    }, null)
                    startActivity(shareSheet)
                }
                return true
            }
        })

        val save = menu.findItem(R.id.action_bookmark)

        save.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                viewModel.bookMarkSelectedArticle()

                Toast.makeText(context, "Bookmarked: ${article?.title}", Toast.LENGTH_SHORT).show()
                return true
            }
        })
        super.onPrepareOptionsMenu(menu)

    }

}