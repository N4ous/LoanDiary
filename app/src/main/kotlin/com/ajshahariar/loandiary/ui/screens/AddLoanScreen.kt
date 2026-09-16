package com.ajshahariar.loandiary.ui.screens

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ajshahariar.loandiary.ui.viewmodel.LoanViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val IconArrowBack: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(20.0f, 11.0f)
            horizontalLineTo(7.83f)
            lineTo(13.41f, 5.41f)
            lineTo(12.0f, 4.0f)
            lineTo(4.0f, 12.0f)
            lineTo(12.0f, 20.0f)
            lineTo(13.41f, 18.59f)
            lineTo(7.83f, 13.0f)
            horizontalLineTo(20.0f)
            verticalLineTo(11.0f)
            close()
        }
    }.build()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLoanScreen(
    viewModel: LoanViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var personName by remember { mutableStateOf("") }
    var amountString by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("LENT") } // "LENT" or "BORROWED"
    
    val defaultCurrency by viewModel.defaultCurrency.collectAsState()
    var selectedCurrency by remember(defaultCurrency) { mutableStateOf(defaultCurrency) }
    
    var note by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    var loanDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L)) }
    
    var showLoanDatePicker by remember { mutableStateOf(false) }
    var showDueDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    // Intent framework contact selection resolver contract
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val contactUri = result.data?.data
            if (contactUri != null) {
                val cursor = context.contentResolver.query(
                    contactUri, 
                    arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME), 
                    null, null, null
                )
                cursor?.use {
                    if (it.moveToFirst()) {
                        personName = it.getString(0)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Record", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(IconArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { selectedType = "LENT" },
                    modifier = Modifier.weight(1f),
                    colors = if (selectedType == "LENT") ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors()
                ) {
                    Text("I Lent Money")
                }
                Button(
                    onClick = { selectedType = "BORROWED" },
                    modifier = Modifier.weight(1f),
                    colors = if (selectedType == "BORROWED") ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary) else ButtonDefaults.filledTonalButtonColors()
                ) {
                    Text("I Borrowed")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCurrency == "USD",
                    onClick = { selectedCurrency = "USD" },
                    label = { Text("USD ($)") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedCurrency == "BDT",
                    onClick = { selectedCurrency = "BDT" },
                    label = { Text("BDT (৳)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = personName,
                    onValueChange = { personName = it },
                    label = { Text("Person Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                        contactPickerLauncher.launch(intent)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text("Pick")
                }
            }

            OutlinedTextField(
                value = amountString,
                onValueChange = { amountString = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = dateFormat.format(Date(loanDate)),
                    onValueChange = {},
                    label = { Text("Date Taken") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showLoanDatePicker = true }
                )

                OutlinedTextField(
                    value = dateFormat.format(Date(dueDate)),
                    onValueChange = {},
                    label = { Text("Due Date") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDueDatePicker = true }
                )
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            if (showError) {
                Text(
                    text = "Please enter a valid name and amount.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = {
                    val amount = amountString.toDoubleOrNull()
                    if (personName.isNotBlank() && amount != null && amount > 0) {
                        viewModel.addLoan(
                            personName = personName.trim(),
                            amount = amount,
                            type = selectedType,
                            currency = selectedCurrency,
                            date = loanDate,
                            dueDate = dueDate,
                            note = note.trim()
                        )
                        onNavigateBack()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Record")
            }
        }
    }

    if (showLoanDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = loanDate)
        DatePickerDialog(
            onDismissRequest = { showLoanDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { loanDate = it }
                    showLoanDatePicker = false
                }) { Text("Select") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showDueDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate)
        DatePickerDialog(
            onDismissRequest = { showDueDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dueDate = it }
                    showDueDatePicker = false
                }) { Text("Select") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
