package com.example.moviesapp.ui.adapters

import android.content.Context
import android.net.Uri
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.network.models.Profile
import com.example.moviesapp.utils.CircleProgressDrawable
import com.example.moviesapp.utils.Constants
import com.example.moviesapp.utils.ItemClickListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.photo_row_layout.view.*

class PhotosAdapter(var images: List<Profile>?,  mContext: Context) :
    RecyclerView.Adapter<PhotosAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_row_layout, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images!!.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindTo(images!!.get(position))
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(profile: Profile) =
            with(itemView) {
                val url = Constants().BASE_IMAGE_URL + profile.filePath
                val uri = Uri.parse(url)
                card_image.hierarchy.setProgressBarImage(CircleProgressDrawable())
                val imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .build()
                val controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(card_image.getController())
                    .setAutoPlayAnimations(true)
                    .setImageRequest(imageRequest)
                    .build()
                card_image.setController(controller)

                card_image.setOnClickListener {
                    if (card_image.context is ItemClickListener)
                        (card_image.context as ItemClickListener).OnItemClick(profile)
                }
            }

    }
}