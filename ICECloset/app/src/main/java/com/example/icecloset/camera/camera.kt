package com.example.icecloset.camera

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.example.icecloset.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_camera.*
import okio.Utf8
import org.json.JSONObject
import java.io.*
import java.net.Socket
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

class camera : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"
    var userTimeStamp: String = ""

    val REQUEST_IMAGE_CAPTURE = 1 // Camera Permission code
    lateinit var curPhotoPath: String// String type Photo path setting with null pointer

    var userToken: String = ""

    var timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var fileName = "${timestamp}.jpeg"

    lateinit var bitmap: Bitmap
//    var bitmap :Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        userToken = intent.getStringExtra(TOKEN).toString()
        Log.d(TOKEN, userToken)

        setPermission()

        capture.setOnClickListener {
            takeCapture()
        }

        save.setOnClickListener {
//            val file = File(curPhotoPath)
//            sendPhoto(fileName, file)
            sendPhoto()

        }
    }


    // Camera 촬영
    private fun takeCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                }
                catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.icecloset.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    // Create Image Files
    private fun createImageFile(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
            .apply { curPhotoPath = absolutePath }
    }


    // Ted permission setting (Camera Permission setting)
    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {    // Permission OK
                Toast.makeText(this@camera, "모든 권한이 허용되었습니다.\n사진을 촬영해 주새요.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {  // Permission denied
                Toast.makeText(this@camera, "촬영 권한이 거부되었습니다.\n잠시후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("옷을 촬영하기 위해 권한을 허용해주세요.")
            .setDeniedMessage("카메라 권한을 거부하였습니다. \n [앱 설정] -> [권한] 에서 카메라 권한을 허용할 수 있습니다.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
            .check()
    }

    // 사진 결과값
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   // 사진 찍고 난 후 결과물
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {     // 이미지를 성공적으로 받을 때
//            val bitmap: Bitmap
            val file = File(curPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                imageView.setImageBitmap(bitmap)
//                Glide.with(this)
//                    .asBitmap()
//                    .load(bitmap)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .into(imageView)
            }
            else {
                val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
                )

                bitmap = ImageDecoder.decodeBitmap(decode)
                imageView.setImageBitmap(bitmap)
//                Glide.with(this)
//                    .asBitmap()
//                    .load(bitmap)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .into(imageView)
            }
            savePhoto(file, bitmap)
        }
    }


    private fun savePhoto(file: File, bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/"
//        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if (!folder.isDirectory)  {     // 해당 경로에 folder가 존재 하지 않는다면
            folder.mkdirs()
        }

        // 실제 저장
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

//        Toast.makeText(this@camera, "촬영한 옷이 앨범에 저장되었습니다", Toast.LENGTH_SHORT).show()
//        sendPhoto(fileName, file)
    }

    private fun sendPhoto() {
        var thread = NetworkThread()
        thread.start()
    }



    inner class NetworkThread : Thread() {
        @ExperimentalStdlibApi
        override fun run() {
            try {
                val socket = Socket("220.67.124.120", 30000)  //220.67.124.185:65000
                Log.d("NetworkThread", "서버 접속 성공")

                Log.d("BITMAP", bitmap.toString())


                val image : ImageView = findViewById(R.id.imageView)
                val bmp : Bitmap = image.drawable.toBitmap()

                val stream = ByteArrayOutputStream()
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                val byteArray = stream.toByteArray()

                val aa : String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                Log.d("AA", aa)


                var output = socket.getOutputStream()
                var dos = DataOutputStream(output)


                // Create JSON
                var rootObject = JSONObject()
                rootObject.put("token", userToken)
                rootObject.put("check", "")
//                rootObject.put("img", byteArray.decodeToString())
                rootObject.put("img", aa)
                
                stream.flush()
                stream.close()


                var jsonLength: Int = rootObject.toString().length

                //
                var temp: String = String.format("%d", jsonLength)
                var length: Int = temp.length

                var space = "                "
                space = space.substring(length)
//                Log.d("JSON", String.format("%d", space.length))
//
                var json : String = jsonLength.toString() + space
                Log.d("JSON", json)

                dos.writeBytes(json)
                dos.writeBytes(rootObject.toString())

                Log.d("NetworkThread", "JSON Length : " + jsonLength)
                Log.d("NetworkThread", rootObject.toString())

                Log.d("NetworkThread", "Send Complete")

                socket.close()

            } catch (e : Exception) {
                e.printStackTrace()
            }
            super.run()
        }
    }
}