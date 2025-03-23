package cszsm.dolgok.ui.screens.forecast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cszsm.dolgok.domain.usecases.GetForecastUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val getForecastUseCase: GetForecastUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ForecastState())
    val state: StateFlow<ForecastState> = _state.asStateFlow()

    fun loadForecast() {
        viewModelScope.launch {
            try {
                val forecast = getForecastUseCase(
                    latitude = LATITUDE,
                    longitude = LONGITUDE,
                )
                _state.update { state ->
                    state.copy(forecast = forecast)
                }
            } catch (e: Exception) {
                Log.e(ForecastViewModel::class.simpleName, e.localizedMessage ?: "unknown error")
            }
        }
    }

    private companion object {
        const val LATITUDE: Float = 47.50f
        const val LONGITUDE: Float = 19.04f
    }
}