package com.cellulant.myweatherforcast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cellulant.myweatherforcast.databinding.ItemForecastBinding
import com.cellulant.myweatherforcast.models.onecall.Hourly
import com.cellulant.myweatherforcast.utils.Constants.IMAGE_BASE_URL

class OneCallAdapter : RecyclerView.Adapter<OneCallAdapter.AnnouncementViewHolder>() {

    inner class AnnouncementViewHolder(val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        return AnnouncementViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val data = differ.currentList[position]

        val wind = "Wind: ${data.wind_speed} m/s"
        val humid = "Humidity: ${data.humidity} %"
        val pre = "Pressure: ${data.pressure} hPa"
        val temperature = "${data.temp} °C"
        val imageUrl = "$IMAGE_BASE_URL${data.weather[0].icon}.png"


        val desc = data.weather[0].description
        holder.binding.apply {
            date.text = data.dt.toString()
            skyStatus.text = desc
            windSpeed.text = wind
            humidity.text = humid
            pressure.text = pre
            temp.text = temperature
            Glide.with(holder.itemView).load(imageUrl).into(icon)
        }
    }
}