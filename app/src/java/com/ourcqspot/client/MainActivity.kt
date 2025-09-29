package com.ourcqspot.client

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.ourcqspot.client.graphs.HomeNavGraph
import com.ourcqspot.client.graphs.RootNavGraph
import com.ourcqspot.client.networking.ClientHandler
import com.ourcqspot.client.ui.theme.OurcqSpotTheme
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MainActivity : ComponentActivity() {

    /**
     * ClientHandler object (singleton?) that should be initialized later.
     */
    private lateinit var clientHandler: ClientHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keepSplashScreen = false // doit rester "true" tant que de la data doit être chargée (par exemple appel au serveur)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplashScreen // !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 300L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 300L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        /* WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false */
        setContent {
            OurcqSpotTheme {
                initConnectionToServer()
                RootNavGraph(navController = rememberNavController())
                //HomeNavGraph(navController = rememberNavController())
            }
        }
    }
    
    fun initConnectionToServer() {
        Thread {
            try {
                // Getting the ClientHandler as an object (in the "clientHandler" property)
                clientHandler = ClientHandler.getInstance()
                clientHandler.initClient()


                println("[Creating a single Thread for the ClientHandler to do its things]")
                val future: Future<*> = Executors.newSingleThreadExecutor()
                    .submit(clientHandler)
                // Blocking the finally (that would close connection) until program ends
                while (!future.isDone) {} // [while the connection isn't unset]

            } catch (e: Exception) {
                Log.d("PROBLEM", "initConnectionToServer: " + e.javaClass + " | " + e.message)
                e.printStackTrace()
            } finally {
                try {
                    println("Closing connection from main()...")
                    clientHandler.closeConnection()
                } catch (e: Exception) {
                    System.err.println("[${e::class}] Connection could not be closed in main()")
                }
            }
        }.start()
    }

    @Preview(showBackground = true)
    @Composable
    fun RootElementPreview() {
        RootNavGraph()
    }
    @Preview(showBackground = true)
    @Composable
    fun HomeElementPreview() {
        HomeNavGraph()
    }
}