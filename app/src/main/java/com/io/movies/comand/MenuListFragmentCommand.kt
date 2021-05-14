package com.io.movies.comand

import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.io.movies.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MenuListFragmentCommand {

    private lateinit var pairFavoriteButton: Pair<MenuItem, MenuItem>
    private var isFavorite: Boolean = false


    fun searchViewListener(searchView: SearchView, listener: (String) -> Unit){
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
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        onQueryTextSubmit(newText)
                    }
                return true
            }

        })
    }

    fun favoriteButtonInit(itemNotSelected: MenuItem, itemSelected: MenuItem){
        if (!isFavorite) {
            itemSelected.isVisible = false
        } else {
            itemNotSelected.isVisible = false
        }
        pairFavoriteButton = Pair(itemNotSelected,itemSelected)
    }

    fun onClickFavoriteButton(idMenuButton: Int, isFavoriteMode: (Boolean) -> Unit){
        when (idMenuButton){
            pairFavoriteButton.first.itemId -> {
                Log.e("Menu", "Selected")
                pairFavoriteButton.second.isVisible = true
                pairFavoriteButton.first.isVisible = false
                isFavorite = true
                isFavoriteMode(isFavorite)
            }
            pairFavoriteButton.second.itemId  -> {
                Log.e("Menu", "Not Selected")
                pairFavoriteButton.second.isVisible = false
                pairFavoriteButton.first.isVisible = true
                isFavorite = false
                isFavoriteMode(isFavorite)
            }
            else -> {
                Log.e("Menu", "Empty")
            }
        }
    }
}