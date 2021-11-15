package com.jongsip.streetstall.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jongsip.streetstall.R
import com.jongsip.streetstall.util.PermissionUtil
import java.text.SimpleDateFormat
import java.util.*

class AddFoodActivity : AppCompatActivity() {
    lateinit var editFoodName: EditText
    private lateinit var imgFood: ImageButton
    lateinit var editFoodInfo: EditText
    lateinit var editFoodPrice: EditText
    lateinit var btnAddFood: Button
    var imgFoodUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        editFoodName = findViewById(R.id.edit_food_name)
        editFoodInfo = findViewById(R.id.edit_food_info)
        editFoodPrice = findViewById(R.id.edit_food_price)
        imgFood = findViewById(R.id.img_food)
        btnAddFood = findViewById(R.id.btn_add_food)

        imgFood.setOnClickListener {
            selectGallery()
        }

        btnAddFood.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("name", editFoodName.text.toString())
            resultIntent.putExtra("imgUrl",imgFoodUri.toString())
            resultIntent.putExtra("price", editFoodPrice.text.toString().toInt())
            resultIntent.putExtra("extraInfo", editFoodInfo.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        PermissionUtil.requestStoragePermission(this) //저장소 접근 권한요청
    }

    //갤러리 실행
    private fun selectGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, REQ_GALLERY)
    }

    //갤러리에서 다시 돌아옴
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_GALLERY -> data?.data?.let {
                    imgFood.setImageURI(it)
                }
            }
        }
    }

    companion object {
        const val REQ_GALLERY = 101
    }
}



