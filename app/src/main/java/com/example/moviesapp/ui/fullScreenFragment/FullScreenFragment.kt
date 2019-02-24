package com.example.moviesapp.ui.fullScreenFragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.moviesapp.R
import com.example.moviesapp.utils.Constants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_full_screen.*
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class FullScreenFragment : Fragment() {
    internal var imageUrl: String? = null
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(Constants().IMAGE_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listener = object : BaseControllerListener<ImageInfo>() {
            override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                updateViewSize(imageInfo)
            }

            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                updateViewSize(imageInfo)
            }
        }
        val uri = Uri.parse(imageUrl)
        val controller = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setControllerListener(listener)
            .build()

        person_image.controller = controller
        download_image.setOnClickListener {
            if (!isPermissionGranted())
                showRequestPermissionsInfoAlertDialog()
            else
                downloadImage()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            Objects.requireNonNull<Context>(context),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {

        }
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants().STORAGE_PERMISSION)
    }

    private fun showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true)
    }

    private fun showRequestPermissionsInfoAlertDialog(makeSystemRequest: Boolean) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.ibtakar)
        builder.setMessage(R.string.permission_dialog_message)
        builder.setPositiveButton(R.string.yes) { dialog, which ->
            dialog.dismiss()
            if (makeSystemRequest) {
                requestPermission()
            }
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun downloadImage() {
        try {
            val url = URL(imageUrl)
//            DownloadTask().execute(url)
            updateView(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants().STORAGE_PERMISSION && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadImage()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }


    private fun updateViewSize(imageInfo: ImageInfo?) {
        if (imageInfo != null) {
            person_image.layoutParams.width = imageInfo.width
            person_image.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            person_image.aspectRatio = imageInfo.width.toFloat() / imageInfo.height
        }
    }


    private fun download(url: URL): Observable<Bitmap> {
        return Observable.create { emitter ->
            var connection: HttpURLConnection? = null

            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                emitter.onNext(BitmapFactory.decodeStream(bufferedInputStream))
                emitter.onComplete()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error Has Occurred", Toast.LENGTH_SHORT).show()
                emitter.onError(e)
                emitter.onComplete()
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun updateView(url: URL) {
        val disposable: Disposable = download(url)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe({ bitmap ->
                saveImageToInternalStorage(bitmap)
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show()
            }, { throwable -> Toast.makeText(context, "Error Has Occurred", Toast.LENGTH_SHORT).show() })

        compositeDisposable.add(disposable)
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(
            context!!.contentResolver,
            bitmap,
            "Bird",
            "Image of bird"
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FullScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants().IMAGE_URL, param1)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

}
