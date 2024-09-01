package com.shlokyadav.definelabskotlin.ui.allmatches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shlokyadav.definelabskotlin.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AllMatchesFragment : Fragment(), VenueAdapter.UpdateListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var venueAdapter: VenueAdapter
    private lateinit var allMatchesViewModel: AllMatchesViewModel
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        progressBar = root.findViewById(R.id.progress_bar)
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        venueAdapter = VenueAdapter(requireContext(), emptyList(), this)
        recyclerView.adapter = venueAdapter


        allMatchesViewModel = ViewModelProvider(this).get(AllMatchesViewModel::class.java)

        allMatchesViewModel.getVenues().observe(viewLifecycleOwner, Observer { venues ->
            venueAdapter.setVenues(venues)
            progressBar.visibility = View.GONE
        })


        fetchData()

        return root
    }

    private fun fetchData() {
        progressBar.visibility = View.VISIBLE
        val url = "https://api.foursquare.com/v2/venues/search?ll=40.7484,-73.9857&oauth_token=NPKYZ3WZ1VYMNAZ2FLX1WLECAWSMUVOQZOIDBN53F3LVZBPQ&v=20180616"

        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val venues: JSONArray = response.getJSONObject("response").getJSONArray("venues")
                    val fetchedVenues = mutableListOf<Venue>()
                    for (i in 0 until venues.length()) {
                        val venue: JSONObject = venues.getJSONObject(i)
                        val id = venue.getString("id")
                        val name = venue.getString("name")

                        val formattedPhone = if (venue.has("contact") && venue.getJSONObject("contact").has("formattedPhone")) {
                            venue.getJSONObject("contact").getString("formattedPhone")
                        } else {
                            "NA"
                        }

                        val address = if (venue.has("location") && venue.getJSONObject("location").has("address")) {
                            venue.getJSONObject("location").getString("address")
                        } else {
                            "NA"
                        }

                        val crossStreet = if (venue.has("location") && venue.getJSONObject("location").has("crossStreet")) {
                            venue.getJSONObject("location").getString("crossStreet")
                        } else {
                            "NA"
                        }

                        fetchedVenues.add(Venue(id, name, formattedPhone, address, crossStreet))
                    }
                    allMatchesViewModel.setVenues(fetchedVenues)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    progressBar.visibility = View.GONE
                }
            },
            { error: VolleyError ->
                error.printStackTrace()
                progressBar.visibility = View.GONE
            })

        queue.add(jsonObjectRequest)
    }

    override fun onUpdate() {
        // Handle updates if necessary
    }
}