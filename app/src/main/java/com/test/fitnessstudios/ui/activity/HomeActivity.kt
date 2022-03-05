package com.test.fitnessstudios.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TableLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.fitnessstudios.R
import com.test.fitnessstudios.constants.PreferenceKeys
import com.test.fitnessstudios.ui.adapter.HomeScreenAdapter
import com.test.fitnessstudios.databinding.ActivityMainBinding
import com.test.fitnessstudios.repository.Resource
import com.test.fitnessstudios.utils.StorageUtils
import com.test.fitnessstudios.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mLocation: Location? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initViews()
        //TODO improve the location fetching flow
        fetchLocationAndMakeRequest()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    private fun initViews() {
        mBinding.showLoading = true
        setupViewPager()
        setupTabs()
    }

    private fun setupViewPager() {
        val adapter = HomeScreenAdapter(this@HomeActivity)
        mBinding.pager.adapter = adapter
        mBinding.pager.isUserInputEnabled = false
    }

    private fun setupTabs() {
        TabLayoutMediator(mBinding.tabLayout, mBinding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "MAP"
                1 -> tab.text = "LIST"
            }
        }.attach()

        val lastSelectedTab = StorageUtils.getDataFromPreferences(
            this,
            PreferenceKeys.lastSelectedTab,
            0
        )
        val tab = mBinding.tabLayout.getTabAt(lastSelectedTab)
        mBinding.tabLayout.selectTab(tab)
        mBinding.pager.setCurrentItem(lastSelectedTab, false)
        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                StorageUtils.saveDataInPreferences(
                    this@HomeActivity,
                    PreferenceKeys.lastSelectedTab,
                    tab.position
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        });
    }

    private fun checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.location_permission_needed))
                    .setMessage(getString(R.string.location_rationale_message))
                    .setPositiveButton(
                        getString(R.string.ok)
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocationAndMakeRequest()
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }
        }
    }


    /**
     * call this method for receive location
     * get location and give callback when successfully retrieve
     * function itself check location permission before access related methods
     *
     */
    fun fetchLocationAndMakeRequest() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        Toast.makeText(
                            this@HomeActivity,
                            "Lat: ${location.latitude} & long: ${location.longitude}",
                            Toast.LENGTH_LONG
                        ).show()
                        mLocation = location
                        homeViewModel.updateLocation(mLocation!!)
                        searchBusiness()
                    }
                }
        }
    }

    private fun searchBusiness() {
        mLocation?.let {
            homeViewModel.searchBusiness(it.latitude, it.longitude)
            lifecycleScope.launchWhenCreated {
                homeViewModel.searchBusinessFlow.collect { event ->
                    when (event) {
                        is Resource.Success -> {
                            Toast.makeText(this@HomeActivity, "Api Success", Toast.LENGTH_LONG)
                                .show()
                            mBinding.showLoading = false
                            homeViewModel.updateBusinessList(event.result.businesses)
                        }
                        is Resource.Error -> {
                            Toast.makeText(this@HomeActivity, "Api Error!!!!!", Toast.LENGTH_LONG)
                                .show()
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
    }
}