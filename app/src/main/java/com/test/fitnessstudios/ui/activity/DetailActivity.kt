package com.test.fitnessstudios.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.test.fitnessstudios.R
import com.test.fitnessstudios.constants.IntentKeys
import com.test.fitnessstudios.data.Business
import com.test.fitnessstudios.databinding.ActivityDetailBinding
import com.test.fitnessstudios.googleAPIKey
import com.test.fitnessstudios.network.WebConfig
import com.test.fitnessstudios.repository.Resource
import com.test.fitnessstudios.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var pointsList: List<LatLng> = emptyList()
    private var location: Location? = null
    private var business: Business? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val bundle = intent.extras
        bundle?.let {
            if (Build.VERSION.SDK_INT >= 33) {
                business = intent.getParcelableExtra(IntentKeys.business, Business::class.java)
                location = intent.getParcelableExtra(IntentKeys.location, Location::class.java)
            } else {
                business = intent.getParcelableExtra(IntentKeys.business)
                location = intent.getParcelableExtra(IntentKeys.location)
            }
        }

        initViews()
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.details)
    }

    private fun getDirections(map: GoogleMap) {
        val url = String.format(
            Locale.getDefault(),
            WebConfig.DIRECTIONS_URL,
            "${location?.latitude},${location?.longitude}",
            "${business?.coordinates?.latitude},${business?.coordinates?.longitude}",
            googleAPIKey
        )
        detailViewModel.getDirections(url)
        lifecycleScope.launchWhenCreated {
            detailViewModel.directionsFlow.collect { event ->
                when (event) {
                    is Resource.Success -> {
                        mBinding.showLoading = false
                        val routes = event.result.routes
                        if (routes.isEmpty())
                            return@collect
                        val points = routes[0].overviewPolyline.points
                        pointsList = PolyUtil.decode(points)
                        val polyline = map.addPolyline(
                            PolylineOptions()
                                .color(R.color.purple_700)
                                .clickable(false)
                                .addAll(pointsList)
                        )

                        val builder = LatLngBounds.Builder()
                        builder.include(LatLng(location!!.latitude, location!!.longitude))
                        builder.include(LatLng(business!!.coordinates!!.latitude, business!!.coordinates!!.longitude))
                        val bounds = builder.build()
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50)
                        map.animateCamera(cameraUpdate)
                    }
                    is Resource.Error -> {
                        Log.d("Usman", "getDirections: Failed to fetch Directions")
                        mBinding.showLoading = false
                    }
                    is Resource.Loading -> {
                        mBinding.showLoading = true
                    }
                    else -> {
                        mBinding.showLoading = true
                    }
                }
            }
        }
    }

    private fun initViews() {
        Glide
            .with(this)
            .load(business?.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(mBinding.ivBusinessImage)

        mBinding.businessName = business?.name
        mBinding.btnCallBusiness.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${business?.phone}")
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment

        mapFragment?.getMapAsync { map ->
            getDirections(map)
            prepareMap(map)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val id: Int = item.itemId
        if (id == R.id.action_share) {

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/html"
            sharingIntent.putExtra(
                Intent.EXTRA_TEXT,
                business?.url
            )
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun prepareMap(map: GoogleMap) {

        map.uiSettings.isMyLocationButtonEnabled = true
        map.isMyLocationEnabled = true

        addMarker(map)

        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map.moveCamera(cameraUpdate)
        }
    }

    private fun addMarker(map: GoogleMap) {
        business?.coordinates?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            val marker = map.addMarker(
                MarkerOptions()
                    .title(business?.name)
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
            )
        }
    }
}