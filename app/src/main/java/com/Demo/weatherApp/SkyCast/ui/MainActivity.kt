package com.Demo.weatherApp.SkyCast.ui

import android.content.SharedPreferences
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.drawerlayout.widget.DrawerLayout

import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.Demo.weatherApp.SkyCast.MainNavGraphDirections
import com.Demo.weatherApp.SkyCast.R
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

/*
 * Often, we'll have sensitive values associated with our code, like API keys, that we'll want to
 * keep out of our git repo, so random GitHub users with permission to view our repo can't see them.
 * The OpenWeather API key is like this.  We can keep our API key out of source control using the
 * technique described below.  Note that values configured in this way can still be seen in the
 * app bundle installed on the user's device, so this isn't a safe way to store values that need
 * to be kept secret at all costs.  This will only keep them off of GitHub.
 *
 * The Gradle scripts for this app are set up to read your API key from a special Gradle file
 * that lives *outside* your project directory.  This file called `gradle.properties`, and it
 * should live in your GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in
 * MacOS/Linux and `$USER_HOME/.gradle/` in Windows).  To store your API key in `gradle.properties`,
 * make sure that file exists in the correct location, and then add the following line:
 *
 *   OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
 *
 * If your API key is stored in that way, the Gradle build for this app will grab it and write it
 * into the string resources for the app with the resource name "openweather_api_key".  You'll be
 * able to access your key in the app's Kotlin code the same way you'd access any other string
 * resource, e.g. `getString(R.string.openweather_api_key)`.  This is what's done in the code below
 * when the OpenWeather API key is needed.
 *
 * If you don't mind putting your OpenWeather API key on GitHub, then feel free to just hard-code
 * it in the app. 🤷‍
 */

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfig: AppBarConfiguration
    private val citySearchedViewModel: CitySearchedViewModel by viewModels()
    private lateinit var prefs: SharedPreferences


    fun fetchCityName(context: Context, onResult: (String?) -> Unit) {
        // Initialize FusedLocationProviderClient
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted, handle accordingly
            onResult(null)
            return
        }

        // Get the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                // Convert the location to a city name
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = addresses?.get(0)?.locality
                onResult(cityName)
            } ?: run {
                // Location was null, handle accordingly
                onResult(null)
            }
        }.addOnFailureListener {
            // Failed to get location, handle accordingly
            onResult(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)

        val appBar: MaterialToolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(appBar)
        setupActionBarWithNavController(navController, appBarConfig)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setupWithNavController(navController)

        // Setting up a manual listener for item selections in the NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val handled = when (menuItem.itemId) {
                R.id.current_location -> {
                    fetchCityName(this) { cityName ->
                        cityName?.let {
                            prefs.edit().putString(getString(R.string.pref_city_key), cityName).apply()

                            // Navigate to CurrentWeatherFragment
                            val navController = findNavController(R.id.nav_host_fragment)
                            navController.navigate(R.id.navigate_to_current_weather)
                        }
                    }
                    true
                }
                R.id.current_weather -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.current_weather)
                    true
                }
                R.id.five_day_forecast -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.five_day_forecast)
                    true
                }
                R.id.settings -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.settings)
                    true
                }
                else -> false
            }

            if (handled) {
                drawerLayout.closeDrawers()
            }
            handled
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        addCities()
    }

    override fun onResume() {
        super.onResume()

        prefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == getString(R.string.pref_city_key)) {
                val city = sharedPreferences.getString(key, "Corvallis,OR,US")
                city?.let { nonNullCity ->
                    citySearchedViewModel.addSearchedCity(nonNullCity)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    private fun addCities() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val subMenu = navView.menu.findItem(R.id.programmatically_inserted).subMenu

        citySearchedViewModel.cities.observe(this, Observer { cities ->
            subMenu?.clear()

            val sortedCitiesList = cities.sortedByDescending { it.lastUpdated }

            sortedCitiesList.forEach { city ->
                subMenu?.add(city.name)?.setOnMenuItemClickListener { menuItem ->
                    Snackbar.make(
                        navView,
                        "This city was clicked: ${city.name}",
                        Snackbar.LENGTH_LONG
                    ).show()
                    findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()


                    val action = MainNavGraphDirections.navigateToCurrentWeather()
                    findNavController(R.id.nav_host_fragment).navigate(action)

                    citySearchedViewModel.updateLastViewed(city.name)

                    prefs.edit().putString(getString(R.string.pref_city_key), city.name).apply()

                    true
                }

            }
        })
    }
}
