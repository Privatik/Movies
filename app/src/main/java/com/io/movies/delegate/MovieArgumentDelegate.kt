package com.io.movies.delegate

import android.os.BaseBundle
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MovieArgumentDelegate<T : Any>: ReadWriteProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val key = property.name
        return thisRef.requireArguments().get(key) as? T ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val args = thisRef.arguments
            ?: Bundle().also(thisRef::setArguments)
        val key = property.name
        args.put(key, value)
    }
}

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> =
    MovieArgumentDelegate()

fun <T> Bundle.put(key: String, value: T){
    when(value){
        is Int -> putInt(key, value)
        is Boolean-> putBoolean(key, value)
        else -> { Log.e("Delegate","Error add delegate")}
    }
}