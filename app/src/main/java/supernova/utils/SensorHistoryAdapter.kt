package supernova.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import supernova.ui.R
import supernova.network.SensorData
import supernova.utils.TimeUtils.formatTimestamp

class SensorHistoryAdapter : ListAdapter<SensorData, SensorHistoryAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val temperature: TextView = view.findViewById(R.id.tvTemperature)
        val humidity: TextView = view.findViewById(R.id.tvHumidity)
        val motion: TextView = view.findViewById(R.id.tvMotion)
        val flame: TextView = view.findViewById(R.id.tvFlame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sensor_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        // ✅ `timestamp` を `+7時間` に変換
        val formattedTimestamp = formatTimestamp(data.timestamp)

        holder.timestamp.text = "\n$formattedTimestamp"

        val paddingTop = 40
        holder.temperature.text = "${data.temperature}°C"
        holder.temperature.setPadding(0, paddingTop, 0, 0)
        holder.humidity.text = "${data.humidity}%"
        holder.humidity.setPadding(0, paddingTop, 0, 0)
        holder.motion.text = if (data.motion) "Detected" else "None"
        holder.motion.setPadding(0, paddingTop, 0, 0)
        holder.flame.text = if (data.flame) "Detected" else "None"
        holder.flame.setPadding(0, paddingTop, 0, 0)
    }


    class DiffCallback : DiffUtil.ItemCallback<SensorData>() {
        override fun areItemsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
            return oldItem == newItem
        }
    }
}
