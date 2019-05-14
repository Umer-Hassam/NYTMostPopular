package com.umerhassam.android.nytmostpopular

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umerhassam.android.nytmostpopular.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.android.synthetic.main.article_detail.view.*
import java.net.URL


/**
 * A fragment representing a single Article detail screen.
* This fragment is either contained in a [ArticleListActivity]
* in two-pane mode (on tablets) or a [ArticleDetailActivity]
* on handsets.
*/
class ArticleDetailFragment : Fragment() {

    /**
     * The article that will be displayed
     * This is passed as a serializable object from ListActivity
     */
    private var item: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Article is serializable so it is directly passed through to the next activity
                // This is better approach because it doen't force us to keep a Singelton or static reference to the list in memory
                item = it.getSerializable(ARG_ITEM_ID) as Article

                activity?.toolbar_layout?.title = item?.title
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.article_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.article_detail.text = it.abstract
        }

        return rootView
    }

    companion object {
        //The key used to pass in the Article to be displayed
        const val ARG_ITEM_ID = "item_id"
    }
}
