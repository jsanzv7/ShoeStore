package com.udacity.shoestore

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import timber.log.Timber

enum class SaveState {
    SAVE,
    NOOP
}

class DetailsViewModel :  ViewModel() {
    private var _shoeList = MutableLiveData<MutableList<Shoe>>()
    val shoeList : LiveData<MutableList<Shoe>>
        get() = _shoeList

    private var _saveState = MutableLiveData<SaveState>()
    val saveState : LiveData<SaveState>
        get() = _saveState

    init {
        _shoeList.value = mutableListOf()
        addShoe("Shoe10", 8.0, "Company10", "desc10")
        _saveState.value = SaveState.NOOP
    }

    fun addShoe(name: String, size: Double, company: String, description: String) {
        _shoeList.value?.add(Shoe(name, size, company, description))
    }

    fun onEventSave(name: String, size: String, company: String, description: String) {
        var sizeDouble : Double = 0.0
        if(size.isNotEmpty() && size.isDigitsOnly()){
            sizeDouble = size.toDouble()
            addShoe(name, sizeDouble, company, description)
            _saveState.value = SaveState.SAVE
        }

            try {
                sizeDouble = size.toDouble()
                addShoe(name, sizeDouble, company, description)
                _saveState.value = SaveState.SAVE

        } catch (e: NumberFormatException) {
        }

    }
    fun onEventSaveComplete() {
        _saveState.value = SaveState.NOOP
    }
}