package com.shlokyadav.definelabskotlin.ui.allmatches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AllMatchesViewModel : ViewModel() {

    private val venueList = MutableLiveData<List<Venue>>()

    fun setVenues(venues: List<Venue>) {
        venueList.value = venues
    }

    fun getVenues(): LiveData<List<Venue>> = venueList
}