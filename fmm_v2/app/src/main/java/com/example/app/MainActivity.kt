/*

    To import microwave coordinates as a layer, it must be a csv file.

 */
package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.portal.Portal
import com.arcgismaps.mapping.PortalItem
import com.example.app.databinding.ActivityMainBinding
import com.arcgismaps.mapping.view.MapView
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.arcgismaps.location.LocationDisplayAutoPanMode
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.mapping.layers.FeatureLayer
import com.arcgismaps.mapping.view.LocationDisplay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val mapView: MapView by lazy {
        activityMainBinding.mapView
    }

    private val locationDisplay: LocationDisplay by lazy { mapView.locationDisplay }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(mapView)

        setApiKey()

        setupMap()

    }

    private fun setApiKey(){
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISEnvironment.apiKey = ApiKey.create("AAPK7069ae50d8e14ab0a5f7e1ccc895cd55M5cijDKp0-Da0KbzF0Cuy1GsNvLpGyu1Rv22L-VT9pPjoCZp_4qRXmI5tj7a6XTP")

    }

    // set up your scene here. You will call this method from onCreate()
    private fun setupMap() {
        //ArcGISEnvironment.applicationContext = applicationContext

        val portal = Portal("https://csusbgis.maps.arcgis.com/", Portal.Connection.Anonymous)

        val itemId = "ea4f2728a917465e98933fef69ce0ace"
        val portalItem = PortalItem(portal, itemId)

        val map = ArcGISMap(portalItem)

        val featureLayer = FeatureLayer.createWithItem(portalItem)

        mapView.map?.operationalLayers?.add(featureLayer)
        // LocationProvider requires an Android Context to properly interact with Android system
        ArcGISEnvironment.applicationContext = applicationContext
        // set the autoPanMode
        locationDisplay.setAutoPanMode(LocationDisplayAutoPanMode.Recenter)

        lifecycleScope.launch {
            // start the map view's location display
            locationDisplay.dataSource.start()
                .onFailure {
                    // check permissions to see if failure may be due to lack of permissions
                    requestPermissions()
                }
        }
        mapView.map = map
    }
    private fun requestPermissions() {
        // coarse location permission
        val permissionCheckCoarseLocation =
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
                    PackageManager.PERMISSION_GRANTED
        // fine location permission
        val permissionCheckFineLocation =
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
                    PackageManager.PERMISSION_GRANTED

        // if permissions are not already granted, request permission from the user
        if (!(permissionCheckCoarseLocation && permissionCheckFineLocation)) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                2
            )
        } else {
            // permission already granted, so start the location display
            lifecycleScope.launch {
                locationDisplay.dataSource.start()
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // if request is cancelled, the results array is empty
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            lifecycleScope.launch {
                locationDisplay.dataSource.start()
            }
        }

        else {
            val errorMessage ="Location Permission Denied"
            showError(errorMessage)
        }

    }
    private fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        Log.e(localClassName, message)
    }
}
