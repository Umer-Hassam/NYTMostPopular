package com.umerhassam.android.nytmostpopular

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.umerhassam.android.nytmostpopular.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_article_list.*
import kotlinx.android.synthetic.main.article_list_content.view.*
import kotlinx.android.synthetic.main.article_list.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.net.URL


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ArticleDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ArticleListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    /*
     * The refence to ArticleViewModel which handles all the articles
     */
    private val articleModel = ArticleViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (article_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }


        //When the ViewModel is done loading articles it informs the Activity through below observer
        val viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        viewModel.articles.observe(this, Observer {

            // Hide the loading message
            loadingTextView.visibility = View.GONE

            // Update recycler view with the new articles
            val adptr = article_list.adapter as SimpleItemRecyclerViewAdapter

            adptr.values = mutableListOf()

            it?.let{ ma -> adptr.values = ma }

            adptr.notifyDataSetChanged()

        })

        setupRecyclerView(article_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, articleModel.articles.value ?: mutableListOf(), twoPane)
    }

    // We don't need anything more then a barebones Adapter for the List we require
    class SimpleItemRecyclerViewAdapter(private val parentActivity: ArticleListActivity,
                                        public var values: MutableList<Article>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Article

                // TwoPane view for tablets
                if (twoPane) {
                    val fragment = ArticleDetailFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable(ArticleDetailFragment.ARG_ITEM_ID, item)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.article_detail_container, fragment)
                            .commit()
                } else { // Single view for mobiles
                    val intent = Intent(v.context, ArticleDetailActivity::class.java).apply {
                        // Article is serializable so it is directly passed through to the next activity
                        // This is better approach because it doen't force us to keep a Singelton or static reference to the list in memory
                        putExtra(ArticleDetailFragment.ARG_ITEM_ID, item)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.article_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]

            // In Android OS 4.x there is known bug which causes the https requests to fail sometimes
            // The proper way to prevent this issue is to introduce an OS check but the below code will solve our issue for quick demo
            val finalURL = item.imageUrl.replace("https", "http")
            Glide.with(holder.itemView).load(finalURL).into(holder.sampleImage);

            holder.idView.text = item.title
            holder.contentView.text = item.abstract

            holder.dateView.text = item.date

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val sampleImage : ImageView = view.sampleImage
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
            val dateView : TextView = view.date_text
        }
    }
}

// TODO Internet permission check