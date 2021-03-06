package com.jongsip.streetstall.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.AddFoodActivity
import com.jongsip.streetstall.adapter.MenuListAdapter
import com.jongsip.streetstall.model.Food
import com.jongsip.streetstall.model.Stall
import com.jongsip.streetstall.util.FirebaseUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageFragment : Fragment() {
    lateinit var editStallName: EditText
    private lateinit var editStallIntro: EditText
    private lateinit var listManageMenu: ListView
    lateinit var relativeAddMenu: RelativeLayout
    lateinit var btnManageComplete: Button

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    lateinit var uid: String
    private var mActivity: Activity? = null

    lateinit var adapter: MenuListAdapter
    var foodMenu: ArrayList<Food> = ArrayList<Food>()

    companion object {
        const val ADD_REQUEST_CODE = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        uid = arguments?.getString("uid")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_manage, container, false)

        editStallName = rootView.findViewById(R.id.edit_stall_name)
        editStallIntro = rootView.findViewById(R.id.edit_stall_intro)
        listManageMenu = rootView.findViewById(R.id.list_manage_menu)
        relativeAddMenu = rootView.findViewById(R.id.layout_add_menu)
        btnManageComplete = rootView.findViewById(R.id.btn_manage_complete)

        //?????? ?????????????????? ?????? ????????? ????????? view ??? ??????
        val docRef = firestore.collection("stall").document(uid)
        docRef.get().addOnSuccessListener {
            editStallName.setText(it.data!!["name"].toString())
            editStallIntro.setText(it.data!!["brief"].toString())

            if (it.data!!["foodMenu"] != null && mActivity != null) {
                foodMenu =
                    FirebaseUtil.convertToFood(it.data!!["foodMenu"] as ArrayList<HashMap<String, *>>)
                adapter = MenuListAdapter(requireActivity().applicationContext, foodMenu, uid)
                listManageMenu.adapter = adapter
            }
        }

        //???????????? ??????
        relativeAddMenu.setOnClickListener {
            startActivityForResult(Intent(activity, AddFoodActivity::class.java), ADD_REQUEST_CODE)
        }

        //???????????? ?????? ??????
        btnManageComplete.setOnClickListener {
            firestore.collection("stall").document(uid).set(
                Stall(
                    editStallName.text.toString(),
                    editStallIntro.text.toString(),
                    foodMenu
                )
            )

            Toast.makeText(
                requireActivity().applicationContext,
                "?????? ????????? ?????????????????????.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return rootView
    }

    //???????????? ?????? ?????? ????????? ????????? ????????? ??????
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val foodName = data!!.getStringExtra("name")!!
            val foodImgUri = Uri.parse(data.getStringExtra("imgUrl"))

            //?????? ?????? ??????
            val fileName = "${foodName}_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.jpg"

            //?????? ?????????, ????????????, ??????, ??????????????? ???????????? ?????? ??????????????? ?????? ?????? ????????? ??????.
            //?????? ?????? ??????/uid/${fileName}
            val imagesRef = storageRef.child("${uid}/").child(fileName)

            //????????? ?????? ?????????
            imagesRef.putFile(foodImgUri).addOnSuccessListener {
                //????????? ?????? ??? ????????? ?????? ????????? ??? ????????? ?????? ??????
                foodMenu.add(
                    Food(
                        foodName,
                        fileName,
                        data.getIntExtra("price", 0),
                        data.getStringExtra("extraInfo")
                    )
                )

                //?????? ?????? ??? ?????? ??????
                btnManageComplete.callOnClick()

                adapter.notifyDataSetChanged()
            }

        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = requireActivity()
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }


}