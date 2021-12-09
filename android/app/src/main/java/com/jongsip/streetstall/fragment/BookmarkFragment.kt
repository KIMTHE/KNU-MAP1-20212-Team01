package com.jongsip.streetstall.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.adapter.BookmarkListAdapter

class BookmarkFragment : Fragment() {
    lateinit var listBookmark: ListView
    private lateinit var layoutNoBookmark: LinearLayout

    lateinit var uid: String
    lateinit var auth: FirebaseAuth
    lateinit var fireStore: FirebaseFirestore

    private var mActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = arguments?.getString("uid")!!
        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_bookmark, container, false)

        listBookmark = rootView.findViewById(R.id.list_bookmark)
        layoutNoBookmark = rootView.findViewById(R.id.layout_no_bookmark)

        fireStore.collection("bookmark").document(uid).get().addOnSuccessListener {
            val bookmarkList = it.data!!["uidArray"] as ArrayList<String>

            if (mActivity != null)
                listBookmark.adapter =
                    BookmarkListAdapter(mActivity!!.applicationContext, bookmarkList, uid)

            if (bookmarkList.size != 0) layoutNoBookmark.visibility = View.INVISIBLE
        }

        return rootView
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