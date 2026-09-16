package com.ajshahariar.loandiary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.ajshahariar.loandiary.data.Loan
import com.ajshahariar.loandiary.ui.screens.HomeScreen
import com.ajshahariar.loandiary.ui.screens.AddLoanScreen
import com.ajshahariar.loandiary.ui.screens.SettingsScreen
import com.ajshahariar.loandiary.ui.screens.LoanDetailScreen
import com.ajshahariar.loandiary.ui.theme.LoanDiaryTheme
import com.ajshahariar.loandiary.ui.viewmodel.LoanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: LoanViewModel = hiltViewModel()
            val isBiometricEnabled by viewModel.isBiometricEnabled.collectAsState()
            val currentThemeIndex by viewModel.currentThemeIndex.collectAsState()
            
            var isUnlocked by remember { mutableStateOf(false) }

            LaunchedEffect(isBiometricEnabled) {
                if (isBiometricEnabled) {
                    showBiometricPrompt {
                        isUnlocked = true
                    }
                } else {
                    isUnlocked = true
                }
            }

            LoanDiaryTheme(themeIndex = currentThemeIndex) {
                if (isUnlocked) {
                    AppNavGraph(viewModel)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { showBiometricPrompt { isUnlocked = true } }) {
                            Text("Unlock LoanDiary")
                        }
                    }
                }
            }
        }
    }

    private fun showBiometricPrompt(onSuccess: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock LoanDiary")
            .setSubtitle("Authenticate to access your secure financial logs")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}

@Composable
fun AppNavGraph(viewModel: LoanViewModel) {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedLoan by remember { mutableStateOf<Loan?>(null) }

    when (currentScreen) {
        "home" -> HomeScreen(
            viewModel = viewModel,
            onNavigateToAddLoan = { currentScreen = "add_loan" },
            onNavigateToSettings = { currentScreen = "settings" },
            onNavigateToDetail = { loan ->
                selectedLoan = loan
                currentScreen = "loan_detail"
            }
        )
        "add_loan" -> AddLoanScreen(
            viewModel = viewModel,
            onNavigateBack = { currentScreen = "home" }
        )
        "settings" -> SettingsScreen(
            viewModel = viewModel,
            onNavigateBack = { currentScreen = "home" }
        )
        "loan_detail" -> {
            val loanId = selectedLoan?.id ?: 0
            val currentLoanList by viewModel.allLoans.collectAsState()
            val freshLoan = currentLoanList.find { it.id == loanId } ?: selectedLoan
            
            if (freshLoan != null) {
                LoanDetailScreen(
                    loan = freshLoan,
                    viewModel = viewModel,
                    onNavigateBack = { currentScreen = "home" }
                )
            } else {
                currentScreen = "home"
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Preview stub
}
