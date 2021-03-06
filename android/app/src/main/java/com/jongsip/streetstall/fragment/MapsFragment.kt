package com.jongsip.streetstall.fragment

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.HashMap
import android.content.Intent
import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.StallInfoActivity


class MapsFragment : Fragment(), OnMapReadyCallback {
    lateinit var mLocationManager: LocationManager
    lateinit var mLocationListener: LocationListener
    var lat = 0.0
    var lng = 0.0

    private lateinit var mView: MapView
    lateinit var cardView : LinearLayout
    lateinit var gMap: GoogleMap
    lateinit var btnMoveHere: FloatingActionButton

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        uid = arguments?.getString("uid")!!
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =
            inflater.inflate(R.layout.fragment_maps, container, false)
        val mContext: Context = container!!.context
        mView = rootView.findViewById(R.id.map_view) as MapView
        cardView = rootView.findViewById(R.id.card_view) as LinearLayout
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)


        cardView.visibility = View.GONE
        //????????? ???????????? ?????? ?????? ??????
        firestore.collection("working").get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.data["latitude"] != null) {
                    lat = document.data["latitude"].toString().toDouble()
                    lng = document.data["longitude"].toString().toDouble()
                    val stallUid = document.id
                    val currentLocation = LatLng(lat, lng)//?????? ?????? ??? ??????
                    var foodName : String? = null
                    var foodPrice : String? = null

                    //?????? ?????? ???????????? ??????
                    firestore.collection("stall").document(document.id).get().addOnSuccessListener {
                        val markerOptions = MarkerOptions() //???
                        markerOptions.title(it.data?.get("name").toString()) //?????????
                        markerOptions.snippet(it.data?.get("brief").toString()) //????????????
                        markerOptions.position(currentLocation) //??????(??????, ?????? ???)

                        val marker: Marker? = gMap.addMarker(markerOptions)

                        //??????????????? ?????? - ?????? ???????????? ??????
                        var foodMenu: ArrayList<HashMap<String, *>>? = null
                        foodMenu = it.data!!["foodMenu"] as ArrayList<HashMap<String, *>>?
                        if (foodMenu != null) {
                            for (item in foodMenu) {
                                foodName = item["name"] as String
                                foodPrice = (item["price"] as Long).toString()
                                break
                            }
                        }
                        if (marker != null) {
                            marker.tag = "$foodName/$foodPrice/$stallUid"
                        }
                    }
                }
            }
        }

        mLocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
        //?????? ????????? ????????? ??? ??????
        mLocationListener = LocationListener { location ->

            //???????????? ????????????????????? ?????????
            lat = arguments?.getDouble("latitude") ?:0.0
            lng = arguments?.getDouble("longitude")?:0.0
            arguments?.remove("latitude")
            arguments?.remove("longitude")

            //????????? ?????? ???
            if(lat==0.0 && lng == 0.0) {
                lat = location.latitude
                lng = location.longitude
            }

            Log.d("GmapViewFragment", "Lat: ${lat}, lon: $lng")
            val currentLocation = LatLng(lat, lng)
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))

            //DB ????????? ?????? SellerMainActivity ??? ?????? ?????? ??? ??????
            dataPassListener.onDataPass(lat, lng)

            val storeUid = rootView.findViewById<TextView>(R.id.store_uid)
            gMap.setOnMarkerClickListener { marker ->
                storeUid.visibility = View.INVISIBLE
                cardView.visibility = View.VISIBLE
                val storeName = rootView.findViewById<TextView>(R.id.text_store_name)
                val introStore = rootView.findViewById<TextView>(R.id.text_introduce_store)
                val bestMenu = rootView.findViewById<TextView>(R.id.text_best_menu)

                var arr = marker.tag.toString().split("/") //????????? ?????? ??????
                storeName.text = marker.title
                introStore.text = marker.snippet
                bestMenu.text = arr[0] + "  " + arr[1] + "???"
                storeUid.text = arr[2]

                cardView.visibility = View.VISIBLE

                //Log.d("parkinfo", "parkname->"+marker.title+"___pakrwhat->")
                false
            }

            //??? ?????? ?????????-??? ???????????? ????????? ?????????
            gMap.setOnMapClickListener { cardView.visibility = View.GONE }

            //????????? ????????? StallInfoActivity ??? ?????? ??????
            cardView.setOnClickListener {
                //?????? ?????? ????????? ????????? ?????? StallInfoActivity ??? uid ??? ??????
                val stallUid = storeUid.text.toString()
                val intent = Intent(activity, StallInfoActivity::class.java)
                intent.putExtra("stallUid", stallUid)
                intent.putExtra("uid",uid)
                requireActivity().startActivity(intent)
            }
        }

        //?????? ?????? ??????
        btnMoveHere = rootView.findViewById(com.jongsip.streetstall.R.id.btn_move_here)
        btnMoveHere.setOnClickListener {
            if (ContextCompat.checkSelfPermission(//????????? ??????
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                mLocationManager.requestLocationUpdates(//gps ????????? ??????????????? ????????? ?????? ???????????? ??????
                    LocationManager.NETWORK_PROVIDER,
                    3000L,
                    30f,
                    mLocationListener
                )
            }
        }
        return rootView
    }

    //SellerMainActivity ??? ?????? ?????? ????????? ?????? ??????
    interface OnDataPassListener {
        fun onDataPass(latitude: Double, longitude: Double)
    }

    private lateinit var dataPassListener: OnDataPassListener

    //SellerMainActivity ??? ?????? ?????? ????????? ?????? ??????
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassListener = context as OnDataPassListener //?????????
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