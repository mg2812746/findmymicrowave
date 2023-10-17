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
import com.arcgismaps.mapping.view.SceneView
import com.arcgismaps.portal.Portal
import com.arcgismaps.mapping.PortalItem
import com.arcgismaps.mapping.layers.ArcGISSceneLayer
import com.arcgismaps.mapping.layers.FeatureLayer
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

        // loadPortalItem()

    }

    private fun setApiKey(){
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISEnvironment.apiKey = ApiKey.create("")

    }

    // set up your scene here. You will call this method from onCreate()
    private fun setupScene() {

        val portal = Portal("https://www.arcgis.com", Portal.Connection.Anonymous)

        val itemId = "46785ddf3cb346e79af22caf599cac4d"
        val portalItem = PortalItem(portal, itemId)

        val scene = ArcGISScene(portalItem)

        // set the scene on the scene view
        sceneView.scene = scene

    }
    /*
     * Sets the map using the [layer] at the given [viewpoint]
     */
    private fun setFeatureLayer(layer: FeatureLayer) {
        activityMainBinding.sceneView.apply {
            // adds the new layer to the map
            // sceneView.buildLayer(layer)
        }
    }

    private fun loadPortalItem() {
        // set the portal
        val portal = Portal("https://www.arcgis.com")
        // create the portal item with the item ID for the Portland tree service data
        val portalItem = PortalItem(portal, "ca0470dbbddb4db28bad74ed39949e25")
        try{
            // create the feature layer with the item
            val featureLayer = FeatureLayer.createWithItem(portalItem)
            // set the feature layer on the map
        }
        catch(ex: Exception){
            showError("Error loading portal item:")
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        Log.e(localClassName, message)
    }

}
