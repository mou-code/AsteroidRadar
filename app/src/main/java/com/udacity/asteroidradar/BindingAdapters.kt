package com.udacity.asteroidradar

import android.icu.number.NumberFormatter.with
import android.view.KeyCharacterMap.load
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.MainViewModel
import java.lang.System.load

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription="Hazardous"
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription="Not Hazardous"
        }
    }

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}


@BindingAdapter("url")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Picasso.get().load(imgUri).into(imgView);
    }
}


@BindingAdapter("NetworkError")
fun NetworkError(imgView: ImageView, status: MainViewModel.Status) {
   when(status){
       MainViewModel.Status.ERROR ->{
           imgView.visibility = View.VISIBLE
           imgView.setImageResource(R.drawable.ic_baseline_wifi_off_24)
   }
       else -> imgView.visibility = View.GONE
       }
   }


@BindingAdapter("Loading")
fun Loading(view: View, status: MainViewModel.Status) {

    when(status){
        MainViewModel.Status.LOADING -> {
            view.visibility = View.VISIBLE
        }
        else -> view.visibility = View.GONE
    }
}
