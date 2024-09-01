package com.shlokyadav.definelabskotlin.ui.savedmatches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shlokyadav.definelabskotlin.VenueDatabaseHelper
import com.shlokyadav.definelabskotlin.ui.allmatches.Venue

class SavedMatchesViewModel(application: Application) : AndroidViewModel(application) {

    private val venuesLiveData: MutableLiveData<List<Venue>> = MutableLiveData()
    private val databaseHelper: VenueDatabaseHelper = VenueDatabaseHelper(application.applicationContext)

    init {
        loadSavedVenues()
    }

    fun loadSavedVenues() {
        val savedVenues: List<Venue> = databaseHelper.getAllSavedVenues()
        venuesLiveData.value = savedVenues
    }

    fun getVenues(): LiveData<List<Venue>> {
        return venuesLiveData
    }
}