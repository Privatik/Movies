package com.io.movies.comand

import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MenuListFragmentCommand(
    private val favoriteLiveData: MutableLiveData<Boolean>,
    private val queryLiveData: MutableLiveData<String>)
{

    private var favoriteButton: MenuItem? = null
    private var favoriteButtonSelected: MenuItem? = null

    fun searchViewListener(searchView: SearchView){
        val queryText = queryLiveData.value!!

        if (queryText.isNotEmpty()) {
            searchView.setQuery(queryText, false)
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private var subscription: Disposable? = null

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e("TAG", "submit $query")
                queryLiveData.postValue(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (subscription != null) {
                    subscription!!.dispose()
                }

                subscription = Observable.timer(800, TimeUnit.MILLISECONDS)
                    .subscribe {
                        onQueryTextSubmit(newText)
                    }
                return true
            }

        })
    }

    fun favoriteButtonInit(favoriteButton: MenuItem, favoriteButtonSelected: MenuItem){
        if (this.favoriteButton == null) {
            if (favoriteLiveData.value!!) {
                favoriteButton.isVisible = false
            } else {
                favoriteButtonSelected.isVisible = false
            }

            this.favoriteButton = favoriteButton
            this.favoriteButtonSelected = favoriteButtonSelected
        }
        Log.e("isFavoriteMenu","init favoritebutton")
    }

    fun onClickFavoriteButton(idMenuButton: Int){
        when (idMenuButton){
            favoriteButton?.itemId -> {
                Log.e("Menu", "Selected")
                favoriteButtonSelected!!.isVisible = true
                favoriteButton!!.isVisible = false
                favoriteLiveData.postValue(true)
                Log.e("isFavoriteMenu","is null-? $favoriteButtonSelected")
            }
            favoriteButtonSelected?.itemId  -> {
                Log.e("Menu", "Not Selected")
                favoriteButtonSelected!!.isVisible = false
                favoriteButton!!.isVisible = true
                favoriteLiveData.postValue(false)
            }
            else -> {
                Log.e("Menu", "Empty")
            }
        }
    }

    fun clear(){
        favoriteButton = null
        favoriteButtonSelected = null
    }
}