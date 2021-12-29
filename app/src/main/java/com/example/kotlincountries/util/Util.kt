package com.example.kotlincountries.util

import android.content.Context
import android.transition.CircularPropagation
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.request.RequestOptions
import com.example.kotlincountries.R


/*
fun String.myExtension(myParametr:String){
    println(myParametr)
}
 */

fun ImageView.downloadFromUrl(url:String?,progressDrawable:CircularProgressDrawable){

 val options=RequestOptions()
     .placeholder(progressDrawable)
     .error(R.mipmap.ic_launcher_round)//default olarak ne resmi gösterecek

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}

fun placeHolderProgresBar(context: Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth=8f
        centerRadius=40f
        start()
    }
}

    @BindingAdapter("android:downloadUrl")
    fun downloadImage(view:ImageView,url:String?){
        view.downloadFromUrl(url, placeHolderProgresBar(view.context))
    }