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
import com.arcgismaps.mapping.ArcGISScene
import com.arcgismaps.mapping.ArcGISTiledElevationSource
import com.arcgismaps.mapping.view.SceneView
import com.arcgismaps.portal.Portal
import com.arcgismaps.mapping.PortalItem
import com.arcgismaps.mapping.layers.ArcGISSceneLayer
import com.arcgismaps.geometry.Point
import com.arcgismaps.geometry.SpatialReference
import com.arcgismaps.mapping.Surface
import com.arcgismaps.mapping.view.SurfacePlacement
import com.arcgismaps.mapping.layers.Layer
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

        lifecycle.addObserver(sceneView)

        setApiKey()

        setupScene()



    }

    private fun setApiKey(){
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISEnvironment.apiKey = ApiKey.create("AAPK7069ae50d8e14ab0a5f7e1ccc895cd55M5cijDKp0-Da0KbzF0Cuy1GsNvLpGyu1Rv22L-VT9pPjoCZp_4qRXmI5tj7a6XTP")

    }

    // set up your scene here. You will call this method from onCreate()
    private fun setupScene() {
        // Define portal url and id of item we want to load
        val portal = Portal("https://www.arcgis.com", Portal.Connection.Anonymous)
        val itemId = "46785ddf3cb346e79af22caf599cac4d"
        val portalItem = PortalItem(portal, itemId)

        val scene = ArcGISScene(portalItem)
        val elevationSource = ArcGISTiledElevationSource("https://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer")
        val surface = Surface().apply{
            elevationSources.add(elevationSource)
        }
        // set the scene on the scene view

        scene.baseSurface = surface
        scene.apply{
            val lyonBuildingsLayer = ArcGISSceneLayer(
                PortalItem(Portal.arcGISOnline(Portal.Connection.Anonymous), "ca0470dbbddb4db28bad74ed39949e25")
            ). apply {
                surfacePlacement = SurfacePlacement.Absolute
                altitudeOffset = 6.0
            }
            operationalLayers.add(lyonBuildingsLayer)
        }

        sceneView.scene = scene

    }
    // set up your scene here. You will call this method from onCreate()
    private fun addLayer() {
        // Define portal url and id of item we want to load
        val portal = Portal("https://www.arcgis.com", Portal.Connection.Anonymous)
        val itemId = "46785ddf3cb346e79af22caf599cac4d"
        val portalItem = PortalItem(portal, itemId)

        val layer = ArcGISSceneLayer(portalItem)



    }
}
