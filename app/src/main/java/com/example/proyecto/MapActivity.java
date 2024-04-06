package com.example.proyecto;

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() ,  OnMapReadyCallback, LocationListener {

private var mFusedLocationClient: FusedLocationProviderClient? = null
private var mLocationManager: LocationManager? = null
private var mGoogleMap: GoogleMap? = null
private var mCurrentLocationMarker: Marker? = null
private var mCurrentLatLng: LatLng? = null
private val MY_PERMISSIONS_REQUEST_LOCATION = 123

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)


        val mapFragment = supportFragmentManager
        .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        // Obtener una instancia del FusedLocationProviderClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Obtener una instancia del LocationManager
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Verificar si la aplicación tiene permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
        // Si no se tienen permisos, solicitarlos
        ActivityCompat.requestPermissions(
        this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
        123
        )
        } else {
        // Si se tienen permisos, iniciar la escucha de actualizaciones de ubicación
        mLocationManager!!.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        0,  // mínimo tiempo entre actualizaciones (milisegundos)
        0f,  // mínimo cambio de distancia para recibir actualizaciones (metros)
        this
        ) // listener de actualizaciones de ubicación
        }
        }

        override fun onMapReady(googleMap: GoogleMap) {
        // Guardar la instancia del GoogleMap para usarla más adelante
        mGoogleMap = googleMap

        // Establecer el tipo de mapa que se quiere mostrar (Normal, Híbrido y Terreno)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Obtener la ubicación actual y actualizar el marcador en el mapa
        obtenerUbicacionActual()
        }

        fun obtenerUbicacionActual() {
        // Verificar si los permisos de ubicación están habilitados
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
        ) {
        // Si los permisos están habilitados, obtener la ubicación actual del usuario
        mFusedLocationClient!!.lastLocation
        .addOnSuccessListener(
        this
        ) { location: Location? ->
        // La última ubicación conocida del usuario se puede obtener de la API de ubicación de Google
        if (location != null) {
        val latitud = location.latitude
        val longitud = location.longitude

        // Actualizar el marcador en el mapa con la ubicación actual del usuario
        mCurrentLatLng = LatLng(latitud, longitud)
        if (mCurrentLocationMarker == null) {
        mCurrentLocationMarker = mGoogleMap!!.addMarker(
        MarkerOptions()
        .position(mCurrentLatLng!!)
        .title("Ubicación actual")
        )
        } else {
        mCurrentLocationMarker!!.setPosition(mCurrentLatLng!!)
        }

        // Mover la cámara hacia la ubicación actual del usuario
        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng!!))
        mGoogleMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
        }
        }
        }
        }

        override fun onLocationChanged(location: Location) {
        // Actualizar la ubicación del usuario en el mapa

        // Actualizar la ubicación del usuario en el mapa
        if (location != null) {
        val latitud = location.latitude
        val longitud = location.longitude
        mCurrentLatLng = LatLng(latitud, longitud)
        if (mCurrentLocationMarker == null) {
        mCurrentLocationMarker = mGoogleMap!!.addMarker(
        MarkerOptions()
        .position(mCurrentLatLng!!)
        .title("Ubicación actual")
        )
        } else {
        mCurrentLocationMarker!!.position = mCurrentLatLng!!
        }

        // Mover la cámara del mapa hacia la nueva ubicación del usuario
        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng!!))
        mGoogleMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
        }
        }
        }