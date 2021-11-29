package com.jongsip.streetstall.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import com.google.firebase.firestore.FirebaseFirestore
import com.jongsip.streetstall.R
import com.jongsip.streetstall.adapter.SearchListAdapter
import com.jongsip.streetstall.model.SearchFood
import com.jongsip.streetstall.model.WorkingPosition
import com.jongsip.streetstall.util.FirebaseUtil
import java.util.HashMap

class SearchFragment : Fragment() {

    lateinit var searchViewFood: SearchView
    lateinit var listSearchFood: ListView

    lateinit var uid: String
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = arguments?.getString("uid")!!
        firestore = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)
        listSearchFood = rootView.findViewById(R.id.list_search_food)
        searchViewFood = rootView.findViewById(R.id.search_view_food)

        searchViewFood.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 누를 때, 검색내용이 있으면 호출
                query?.let { searchFood(query) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색창에서 글자가 변경이 일어날 때마다 호출
                return true
            }
        })

        return rootView
    }

    fun searchFood(searchFoodName: String) {
        val searchData = ArrayList<SearchFood>()

        firestore.collection("working").get().addOnSuccessListener { documents ->
            for (document in documents) {

                firestore.collection("stall").document(document.id).get().addOnSuccessListener {
                    val menuData =
                        FirebaseUtil.convertToFood(it.data!!["foodMenu"] as ArrayList<HashMap<String, *>>)
                    for (foodData in menuData) {
                        //찾고있는 상품이 존재할 때, 검색열에 추가
                        if (foodData.name == searchFoodName) {
                            searchData.add(
                                SearchFood(
                                    document.id,
                                    it.data!!["name"] as String,
                                    foodData,
                                    WorkingPosition(
                                        document.data["latitude"].toString().toDouble(),
                                        document.data["longitude"].toString().toDouble()
                                    )
                                )
                            )
                            break
                        }
                    }
                }
            }
            listSearchFood.adapter = SearchListAdapter(this@SearchFragment,getActivity(), searchData)
        }

    }


    companion object {
        const val TAG = "SearchFragment"
    }
}