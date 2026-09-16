package com.ajshahariar.loandiary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajshahariar.loandiary.data.Loan
import com.ajshahariar.loandiary.data.LoanDao
import com.ajshahariar.loandiary.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoanViewModel @Inject constructor(
    private val loanDao: LoanDao,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val allLoans: StateFlow<List<Loan>> = loanDao.getAllLoans()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val defaultCurrency: StateFlow<String> = settingsRepository.defaultCurrency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "USD")

    val currentThemeIndex: StateFlow<Int> = settingsRepository.currentThemeIndex
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val isBiometricEnabled: StateFlow<Boolean> = settingsRepository.isBiometricEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isNotificationsEnabled: StateFlow<Boolean> = settingsRepository.isNotificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun updateDefaultCurrency(currency: String) {
        viewModelScope.launch { settingsRepository.updateDefaultCurrency(currency) }
    }

    fun updateThemeIndex(index: Int) {
        viewModelScope.launch { settingsRepository.updateThemeIndex(index) }
    }

    fun updateBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateBiometricEnabled(enabled) }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateNotificationsEnabled(enabled) }
    }

    fun addLoan(personName: String, amount: Double, type: String, currency: String, date: Long, dueDate: Long, note: String) {
        viewModelScope.launch {
            val newLoan = Loan(
                personName = personName,
                amount = amount,
                type = type,
                currency = currency,
                date = date,
                dueDate = dueDate,
                note = note,
                repaymentsJson = "[]"
            )
            loanDao.insertLoan(newLoan)
        }
    }

    fun toggleLoanRepaid(loan: Loan) {
        viewModelScope.launch {
            loanDao.updateLoan(loan.copy(isRepaid = !loan.isRepaid))
        }
    }

    fun addRepayment(loan: Loan, amount: Double, date: Long) {
        viewModelScope.launch {
            // Append clean pseudo-json entry
            val cleanedJson = loan.repaymentsJson.trim()
            val newEntry = "{\"amount\":$amount,\"date\":$date}"
            val finalJson = if (cleanedJson == "[]" || cleanedJson.isEmpty()) {
                "[$newEntry]"
            } else {
                cleanedJson.substring(0, cleanedJson.length - 1) + ",$newEntry]"
            }
            
            // Check if total repaid matches or exceeds the loan amount
            // Quick calculation from parsing pseudo-json string
            val updatedLoan = loan.copy(repaymentsJson = finalJson)
            loanDao.updateLoan(updatedLoan)
        }
    }

    fun deleteLoan(loan: Loan) {
        viewModelScope.launch {
            loanDao.deleteLoan(loan)
        }
    }
}
