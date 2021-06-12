package com.example.zoom2u.DataBindingAdapter

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.io.IOException


@BindingAdapter("imageName")
fun setImageFromAssets(view: ImageView, fileName: String) {

    try {
        val bitmap = BitmapFactory.decodeStream(view.context?.assets?.open(fileName))
        view.setImageBitmap(bitmap)
    } catch (e: IOException) {
        e.printStackTrace()
    }


}

@BindingAdapter("imagefor")
fun setImageFromAssets(view: ImageView, image: Int) {
    view.setImageResource(image)
}