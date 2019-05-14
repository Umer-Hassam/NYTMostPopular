package com.umerhassam.android.nytmostpopular

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.NavUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_article_detail.*
import java.net.URL

/**
 * An activity representing a single Article detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ArticleListActivity].
 */
class ArticleDetailActivity : AppCompatActivity() {

    private var currentArticle : Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        setSupportActionBar(detail_toolbar)

        // floatingActionButton to read the full story
        fab.setOnClickListener { view ->

            currentArticle?.let{
                val url = it.url
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } ?: run {
                Snackbar.make(view, "Cannot load full story because URL is Not Availabel", Snackbar.LENGTH_LONG).show()
            }
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = ArticleDetailFragment().apply {
                arguments = Bundle().apply {

                    currentArticle =  intent.getSerializableExtra(ArticleDetailFragment.ARG_ITEM_ID) as Article

                    currentArticle?.let{ a ->
                        putSerializable( ArticleDetailFragment.ARG_ITEM_ID, a)
                    }

                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.article_detail_container, fragment)
                    .commit()
        }


        currentArticle?.let{
            // In Android OS 4.x there is known bug which causes the https requests to fail sometimes
            // The proper way to prevent this issue is to introduce an OS check but the below code will solve our issue for quick demo
            val finalURL = it.imageUrl.replace("https", "http")

            Glide.with(this).load(finalURL)

            Glide.with(this)
                    .asBitmap()
                    .load(finalURL)
                    .into(article_image)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. Use NavUtils to allow users
                    // to navigate up one level in the application structure. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                    NavUtils.navigateUpTo(this, Intent(this, ArticleListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
