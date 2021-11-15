package com.jongsip.streetstall.fragment

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.SellerMainActivity


class MapsFragment : Fragment(), OnMapReadyCallback {
    lateinit var mLocationManager: LocationManager
    lateinit var mLocationListener: LocationListener
    var lat = 0.0
    var lng = 0.0

    private lateinit var mView: MapView
    lateinit var gMap: GoogleMap
    lateinit var btnMoveHere: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =
            inflater.inflate(com.jongsip.streetstall.R.layout.fragment_maps, container, false)
        val mContext: Context = container!!.context
        mView = rootView.findViewById(com.jongsip.streetstall.R.id.mapView) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        mLocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
        //최신 위치가 갱신될 때 호출
        mLocationListener = LocationListener { location ->
            lat = location.latitude
            lng = location.longitude
            Log.d("GmapViewFragment", "Lat: ${lat}, lon: $lng")
            val currentLocation = LatLng(lat, lng)
            gMap.addMarker(MarkerOptions().position(currentLocation).title("현재위치"))
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

            //DB 저장을 위해 SellerMainActivity에 위도 경도 값 전달
            dataPassListener.onDataPass(lat, lng)
        }
        /*위치 정보 구할 때 필요없음
        //위치 공급자의 상태가 바뀔 때 호출
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        //위치 공급자가 사용 가능해질 때 호출
        override fun onProviderEnabled(provider: String?) {}
        //위치 공급자가 사용 불가능해질 때 호출
        override fun onProviderDisabled(provider: String?) {}*/
        //현재 위치 버튼
        btnMoveHere = rootView.findViewById(com.jongsip.streetstall.R.id.btn_move_here)
        btnMoveHere.setOnClickListener {
            if (ContextCompat.checkSelfPermission(//퍼미션 관련
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationManager.requestLocationUpdates(//사용자 위치 업데이트 요청
                    LocationManager.GPS_PROVIDER,
                    3000L,
                    30f,
                    mLocationListener
                )
            }
        }
        return rootView
    }

    //SellerMainActivity로 위도 경도 보내기 위한 부분
    interface onDataPassListener {
        fun onDataPass(latitude : Double,longitude : Double)
    }
    lateinit var dataPassListener : onDataPassListener
    //SellerMainActivity로 위도 경도 보내기 위한 부분
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassListener = context as onDataPassListener //형변환
    }

    override fun onStart() {
        super.onStart()
        mView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        btnMoveHere.performClick()
    }

}