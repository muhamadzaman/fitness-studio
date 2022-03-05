package com.test.fitnessstudios.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.util.MapUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.test.fitnessstudios.R
import com.test.fitnessstudios.constants.IntentKeys
import com.test.fitnessstudios.data.Business
import com.test.fitnessstudios.databinding.FragmentMapBinding
import com.test.fitnessstudios.ui.activity.DetailActivity
import com.test.fitnessstudios.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {

    private lateinit var mBinding: FragmentMapBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var location: Location? = null
    private var businessList: List<Business> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentMapBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location = homeViewModel.locationFlow.value
        businessList = homeViewModel.businessListFlow.value

        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment

        mapFragment?.getMapAsync { map ->
            prepareMap(map)
        }
    }

    @SuppressLint("MissingPermission")
    private fun prepareMap(map: GoogleMap) {

        map.uiSettings.isMyLocationButtonEnabled = true
        map.isMyLocationEnabled = true

        addMarkers(map)

        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map.animateCamera(cameraUpdate)
        }

        //TODO get rid of the warning
        map.setOnMarkerClickListener { marker ->

            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(IntentKeys.business, marker.tag as Business)
            intent.putExtra(IntentKeys.location, location as Location)
            requireActivity().startActivity(intent)
            true
        }
    }


    /**
     * Adds marker representations of the places list on the provided GoogleMap object
     */
    private fun addMarkers(map: GoogleMap) {
        businessList.forEach { business ->
            business.coordinates?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                val marker = map.addMarker(
                    MarkerOptions()
                        .title(business.name)
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                )
                marker?.tag = business
            }
        }
    }
}