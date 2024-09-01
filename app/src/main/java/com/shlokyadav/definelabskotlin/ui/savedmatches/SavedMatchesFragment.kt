package com.shlokyadav.definelabskotlin.ui.savedmatches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shlokyadav.definelabskotlin.R
import com.shlokyadav.definelabskotlin.ui.allmatches.VenueAdapter
import java.util.ArrayList

class SavedMatchesFragment : Fragment(), VenueAdapter.UpdateListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var venueAdapter: VenueAdapter
    private lateinit var savedMatchesViewModel: SavedMatchesViewModel
    private lateinit var emptyView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedMatchesViewModel = ViewModelProvider(this).get(SavedMatchesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        emptyView = root.findViewById(R.id.empty_view)
        recyclerView = root.findViewById(R.id.recycler_view_saved_matches)
        recyclerView.layoutManager = LinearLayoutManager(context)

        venueAdapter = VenueAdapter(requireContext(), ArrayList(), this)
        recyclerView.adapter = venueAdapter

        savedMatchesViewModel.getVenues().observe(viewLifecycleOwner) { venues ->
            if (venues != null && venues.isNotEmpty()) {
                venueAdapter.setVenues(venues)
                emptyView.visibility = View.GONE
            }else {
                venueAdapter.setVenues(venues)
                emptyView.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onUpdate() {
        Log.d("SavedMatchesFragment", "onUpdate called")
        savedMatchesViewModel.loadSavedVenues()
    }

    override fun onResume() {
        super.onResume()
        savedMatchesViewModel.loadSavedVenues()
    }
}