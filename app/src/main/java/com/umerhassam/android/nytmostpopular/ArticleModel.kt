package com.umerhassam.android.nytmostpopular

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.LiveData
import android.util.Log
import org.json.JSONObject
import java.io.Serializable
import java.net.URL

public class Article(var title : String = "", var abstract : String = "", var url : String ="", var date : String ="", var imageUrl : String = "") : Serializable

public class ArticleViewModel : ViewModel() {
    private val apiSection = "all-sections"
    private val apiLimit = "7"

    val articles = MutableLiveData< MutableList<Article> >()

    val url = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/7.json?api-key=cw0v7CuD58CAvh46wynLdzuUhNq6GmAO"

    companion object {
        fun articlesFromJSONString(json : String) : MutableList<Article> {
            val tempArticleArray = mutableListOf<Article>()

            // Parse the returned result
            val result = JSONObject(json)

            // Fail safe
            if(result.has("results")){
                // Parse the returned result
                val jA = result.getJSONArray("results")

                // Iterate through each article and save it in LiveData
                for(i in 0..(jA.length() - 1) ){
                    val jO = jA.getJSONObject(i)

                    // Find Image URL
                    val mA = jO.getJSONArray("media") // Media Array
                    val mT = mA.getJSONObject(0) // Media Type
                    val mm = mT.getJSONArray("media-metadata") // MetaData Array
                    val mmO = mm.getJSONObject(0) // Media-Metadata object which contains the image

                    tempArticleArray.add( Article(jO["title"].toString(), jO["abstract"].toString(), jO["url"].toString(), jO["published_date"].toString(), mmO["url"].toString()) )

                }
            }

            return tempArticleArray
        }
    }
    init {
        // trigger article load.
        updateArticles()
    }

    fun updateArticles(){

        articles?.let{
            if(it.value == null){
                it.value = mutableListOf()
            }
        }

        // We load NYT Articles data here
        // Network calls must be made on a background threads
        Thread(Runnable {

            // Load data from NYT's Api
            val u = URL(url)
            val t = u.readText()

            // We use postValue instead of directly assigning the value because setValue cannot be called from background thread
            articles.postValue( articlesFromJSONString(t) )

        }).start()
    }



}