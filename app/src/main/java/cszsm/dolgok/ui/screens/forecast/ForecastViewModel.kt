package cszsm.dolgok.ui.screens.forecast

import androidx.lifecycle.ViewModel
import cszsm.dolgok.domain.usecases.GetForecastUseCase

class ForecastViewModel(
    private val getForecastUseCase: GetForecastUseCase,
) : ViewModel() {

    suspend fun getForecast() = getForecastUseCase()
}