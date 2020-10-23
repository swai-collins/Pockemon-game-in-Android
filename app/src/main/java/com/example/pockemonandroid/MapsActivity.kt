package com.example.pockemonandroid

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
    }


    var AccessLocation = 123
    fun checkPermission(){
        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),AccessLocation)
                return
            }
        }
        getLocation()
    }

    fun getLocation(){
        Toast.makeText(this, "User location access on",Toast.LENGTH_LONG).show()


        var myLocation = MylocationListener()

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

        var mythread =myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            AccessLocation -> {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getLocation()
                }else{
                    Toast.makeText(this, "User location cannot be found",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }


    var location:Location?= null

    inner class MylocationListener:LocationListener{
        constructor(){
            location = Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location) {
            location=p0
        }
    }

    inner class myThread:Thread{
        constructor():super(){

        }

        override fun run() {
            while (true){
                try {
                    runOnUiThread{
                        mMap.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Me")
                            .snippet("Here is my location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))
                    }

                    Thread.sleep(1000)

                }catch (ex:Exception){}
            }
        }
    }
}