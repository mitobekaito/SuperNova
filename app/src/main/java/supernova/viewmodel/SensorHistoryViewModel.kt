package supernova.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.SensorData

class SensorHistoryViewModel : ViewModel() {
    private val _sensorHistory = MutableLiveData<List<SensorData>>()
    val sensorHistory: LiveData<List<SensorData>> get() = _sensorHistory

    fun fetchSensorHistory(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val history = ApiClient.instance.getSensorData()
                _sensorHistory.postValue(history)
                onComplete(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }
}
