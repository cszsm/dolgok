package cszsm.dolgok.forecast.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cszsm.dolgok.R
import cszsm.dolgok.forecast.presentation.WeatherVariable

// TODO: rethink this once it's not experimental anymore

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeatherVariableButtonGroup(
    weatherVariables: List<WeatherVariable>,
    selectedWeatherVariable: WeatherVariable,
    onSelect: (WeatherVariable) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        modifier = modifier,
    ) {
        weatherVariables.forEachIndexed { index, variable ->
            ToggleButton(
                modifier = Modifier.weight(1f),
                shapes = getToggleButtonShape(
                    currentIndex = index,
                    lastIndex = weatherVariables.lastIndex
                ),
                checked = selectedWeatherVariable == variable,
                onCheckedChange = { onSelect(variable) }
            ) {
                Text(text = variable.getLabel())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun getToggleButtonShape(
    currentIndex: Int,
    lastIndex: Int,
) = when (currentIndex) {
    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
    lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
}

@Composable
private fun WeatherVariable.getLabel() =
    when (this) {
        WeatherVariable.TEMPERATURE -> stringResource(R.string.forecast_weather_variable_temperature)
        WeatherVariable.RAIN -> stringResource(R.string.forecast_weather_variable_rain)
        WeatherVariable.PRESSURE -> stringResource(R.string.forecast_weather_variable_pressure)
    }
