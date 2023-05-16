/*

    To import microwave coordinates as a layer, it must be a csv file.

 */
package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arcgismaps.location.LocationDisplayAutoPanMode
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.databinding.DataBindingUtil
import com.arcgismaps.ApiKey
import com.google.android.material.snackbar.Snackbar
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.data.ServiceFeatureTable
import com.arcgismaps.mapping.*
import com.arcgismaps.portal.Portal
import com.arcgismaps.mapping.layers.FeatureLayer
import com.arcgismaps.mapping.view.*
import com.example.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val sceneView: SceneView by lazy {
        activityMainBinding.sceneView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        lifecycle.addObserver(sceneView)

        setApiKey()

        setupScene()


    }

    private fun setApiKey() {
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISEnvironment.apiKey =
            ApiKey.create("AAPK7069ae50d8e14ab0a5f7e1ccc895cd55M5cijDKp0-Da0KbzF0Cuy1GsNvLpGyu1Rv22L-VT9pPjoCZp_4qRXmI5tj7a6XTP")

    }

    // set up your scene here. You will call this method from onCreate()
    private fun setupScene() {
        val webSceneURL =
            "https://www.arcgis.com/home/webscene/viewer.html?webscene=46785ddf3cb346e79af22caf599cac4d"
        val sceneItem = PortalItem(webSceneURL)
        val scene = ArcGISScene(sceneItem)
        val portal = "https://www.arcgis.com"
        // Load building scene service
        val microBuildingFootprints = PortalItem(
            Portal(portal),
            itemId = "bb69f10baf334d4c935a0fb23d758f38"
        )
        val locationDisplay = sceneView.locationDisplay

        lifecycleScope.launch {
            // listen to changes in the status of the location data source
            locationDisplay.dataSource.start()
                .onSuccess {
                    // permission already granted, so start the location display
                    locationDisplay = sceneView.
                }.onFailure {
                    // check permissions to see if failure may be due to lack of permissions
                    requestPermissions()
                }
        }
        val featureLayer1 = FeatureLayer.createWithFeatureTable(
            ServiceFeatureTable("https://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer)")
        )
        // https://basemaps3d.arcgis.com/arcgis/rest/services/OpenStreetMap3D_Buildings_v1/SceneServer
        val featureLayer2 = FeatureLayer.createWithFeatureTable(
            ServiceFeatureTable("https://basemaps3d.arcgis.com/arcgis/rest/services/OpenStreetMap3D_Buildings_v1/SceneServer")
        )
        //https://services.arcgis.com/P3ePLMYs2RVChkJx/arcgis/rest/services/MSBFP2/FeatureServer/0
        val featureLayer3 = FeatureLayer.createWithFeatureTable(
            ServiceFeatureTable("https://services.arcgis.com/P3ePLMYs2RVChkJx/arcgis/rest/services/MSBFP2/FeatureServer/0")
        )
        val featureLayer4 = FeatureLayer.createWithItem(microBuildingFootprints)
        scene.operationalLayers.addAll(
            listOf(
                featureLayer1,
                featureLayer2,
                featureLayer3,
                featureLayer4
            )
        )

        sceneView.scene = scene

    }

    /**
     * Request fine and coarse location permissions for API level 23+.
     */
    private fun requestPermissions() {
        // coarse location permission
        val permissionCheckCoarseLocation =
            ContextCompat.checkSelfPermission(this@MainActivity, ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        // fine location permission
        val permissionCheckFineLocation =
            ContextCompat.checkSelfPermission(this@MainActivity, ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED

        // if permissions are not already granted, request permission from the user
        if (!(permissionCheckCoarseLocation && permissionCheckFineLocation)) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
                2
            )
        } else {
            // permission already granted, so start the location display

        }
    }
}
