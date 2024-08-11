package com.example.motionfinal.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.motionfinal.R
import com.example.motionfinal.adapter.ItemAdapter
import com.example.motionfinal.model.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = Firebase.firestore

    private lateinit var rvAdapter: ItemAdapter

    private var items = arrayListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getItems()

        rvAdapter = ItemAdapter(items)
        details_rv?.adapter = rvAdapter
        details_rv?.layoutManager = LinearLayoutManager(this.context)

        details_bt_refresh?.setOnClickListener {
            items.clear()
            getItems()
        }

    }

    private fun getItems() {
        db.collection("User")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val item = Item(doc["id"].toString(),
                        doc["name"].toString(),
                        doc["username"].toString(),
                        doc["email"].toString(),
                        doc["street"].toString(),
                        doc["suite"].toString(),
                        doc["city"].toString(),
                        doc["zipcode"].toString())
                    items.add(item)
                }
                rvAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w("", "Error with message: ", e)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}