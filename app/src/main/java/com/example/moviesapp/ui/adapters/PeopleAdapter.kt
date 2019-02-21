package com.example.moviesapp.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.utils.CircleProgressDrawable
import com.example.moviesapp.utils.Constants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.people_list_item.view.*

class PeopleAdapter(var result: List<Result?>,val listener: (Result?) -> Unit ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_PROG = 0
    private val VIEW_ITEM1 = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM1) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.people_list_item, parent, false)
            vh = peopleViewHolder(itemView)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.item_loading, parent, false
            )
            vh = ProgressViewHolder(v)
        }
        return vh
    }


    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar

        init {
            with(itemView) {
                progressBar = progress_bar
            }
        }
    }

    class peopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(result: Result?, listener: (Result?) -> Unit) =
            with(itemView){
                people_name.text=result?.name
                val imageData = Uri.parse(Constants().BASE_IMAGE_URL + result?.profilePath)
                people_image.hierarchy.setProgressBarImage(CircleProgressDrawable())
                val imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(imageData)
                    .build()
                val controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(people_image.controller)
                    .setAutoPlayAnimations(true)
                    .setImageRequest(imageRequest)
                    .build()
                people_image.controller = controller
                setOnClickListener { listener(result) }
            }
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is peopleViewHolder) {
            holder.bindTo(result.get(position),listener)
        } else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (result.get(position) == null)
            VIEW_PROG
        else
            VIEW_ITEM1
    }


}