package edg.android.rciva

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

@SuppressLint("AutoboxingStateCreation")
class RCIVAViewModel : ViewModel() {
    private var rcivaPaymentCondition = 0f   // Condición para pagar RC-IVA

    val nationalMinimumWages = listOf(
        2500f, 2362f, 2250f, 2164f, 2122f, 2060f, 2000f, 1805f, 1656f,
    )

    var index by mutableStateOf(-1)

    var earnedSalary by mutableStateOf(0.0f)                    // Salario total sin descuentos afps
    var afpsContribution by mutableStateOf(0.0f)                // Descuento de AFPs
    var netSalary by mutableStateOf(0.0f)                       // Salario neto para calcular el RC-IVA
    var nationalSolidarityContribution by mutableStateOf(0.0f)  // Descuento de solidaridad nacional
    var rciva by mutableStateOf(0.0f)                           // RC-IVA a pagar

    fun updateSmnIndex(index: Int) {
        this.index = index
        rcivaB()
    }

    fun updateEarnedSalary(income: Float) {
        earnedSalary = income
        rcivaB()
    }

    private fun getAfpsAmount(): Float {
        return earnedSalary * 0.1271f
    }

    private fun getNettSalary(): Float {
        return earnedSalary - afpsContribution - nationalSolidarityContribution
    }

    /**
     * Obtener dos salario mínimos nacionales
     */
    private fun getTwoNationalMinimumWages(): Float {
        return nationalMinimumWages[index] * 2
    }

    /**
     * Base imponible del RC-IVA
     */
    private fun rcivaTaxBase(): Float {
        return getNettSalary() - getTwoNationalMinimumWages()
    }

    /**
     * RC-IVA neto
     */
    private fun rciva(): Float {
        return rcivaTaxBase() * 0.13f
    }

    /**
     * 13% de dos salarios minimos nacionales
     * para el descuento
     */
    private fun rcivaDiscount(): Float {
        return getTwoNationalMinimumWages() * 0.13f
    }

    /**
     * RC-IVA a pagar
     */
    private fun rcivaToPay(): Float {
        return rciva() - rcivaDiscount()
    }

    private fun getSolidarityFund(target: Float): Float {
        var result = 0f
        if (target > 35000f) {
            result += ((target - 35000f) * 0.10f)
        }
        if (target > 25000f) {
            result += ((target - 25000f) * 0.05f)
        }
        if (target > 13000f) {
            result += ((target - 13000f) * 0.01f)
        }
        return result
    }

    private fun rcivaB() {
        if (index > -1 && earnedSalary > 0) {
            rcivaPaymentCondition = nationalMinimumWages[index] * 4
            afpsContribution = getAfpsAmount()
            nationalSolidarityContribution = getSolidarityFund(earnedSalary)
            netSalary = getNettSalary()
            rciva = if (netSalary <= rcivaPaymentCondition) {
                0.0f
            } else {
                rcivaToPay()
            }
        }
    }
}