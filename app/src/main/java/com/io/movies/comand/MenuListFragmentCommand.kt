package com.io.movies.comand

import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MenuListFragmentCommand {

    private var favoriteButton: MenuItem? = null
    private var favoriteButtonSelected: MenuItem? = null


    fun searchViewListener(searchView: SearchView, searchText: String, listener: (String) -> Unit){
        if (searchText.isNotEmpty()) {
            searchView.setQuery(searchText, false)
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private var subscription: Disposable? = null

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e("TAG", "submit $query")
                listener(query)
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

    fun favoriteButtonInit(favoriteButton: MenuItem, favoriteButtonSelected: MenuItem, isFavorite: Boolean?){
        if (isFavorite != null && isFavorite) {
            favoriteButton.isVisible = false
        } else {
            favoriteButtonSelected.isVisible = false
        }

        this.favoriteButton = favoriteButton
        this.favoriteButtonSelected = favoriteButtonSelected
    }

    fun onClickFavoriteButton(idMenuButton: Int, isFavoriteMode: (Boolean) -> Unit){
        when (idMenuButton){
            favoriteButton?.itemId -> {
                Log.e("Menu", "Selected")
                favoriteButtonSelected!!.isVisible = true
                favoriteButton!!.isVisible = false
                isFavoriteMode(true)
            }
            favoriteButtonSelected?.itemId  -> {
                Log.e("Menu", "Not Selected")
                favoriteButtonSelected!!.isVisible = false
                favoriteButton!!.isVisible = true
                isFavoriteMode(false)
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