package com.ajshahariar.loandiary.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import com.ajshahariar.loandiary.data.Loan
import com.ajshahariar.loandiary.ui.viewmodel.LoanViewModel
import java.text.SimpleDateFormat
import java.util.*

val IconArrowBackDetail: ImageVector
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

data class LocalRepayment(val amount: Double, val date: Long)

fun parseRepaymentsList(json: String): List<LocalRepayment> {
    val result = mutableListOf<LocalRepayment>()
    try {
        val clean = json.replace("[", "").replace("]", "").trim()
        if (clean.isEmpty()) return result
        val chunks = clean.split("},")
        for (chunk in chunks) {
            val normalized = chunk.replace("{", "").replace("}", "")
            val pairs = normalized.split(",")
            var amount = 0.0
            var date = 0L
            for (pair in pairs) {
                val kv = pair.split(":")
                if (kv.size >= 2) {
                    val k = kv[0].replace("\"", "").trim()
                    val v = kv[1].trim()
                    if (k == "amount") amount = v.toDoubleOrNull() ?: 0.0
                    if (k == "date") date = v.toLongOrNull() ?: 0L
                }
            }
            result.add(LocalRepayment(amount, date))
        }
    } catch (_: Exception) {
        // Fallback
    }
    return result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDetailScreen(
    loan: Loan,
    viewModel: LoanViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val repayments = remember(loan.repaymentsJson) { parseRepaymentsList(loan.repaymentsJson) }
    val totalRepaid = remember(repayments) { repayments.sumOf { it.amount } }
    
    var repaymentAmountStr by remember { mutableStateOf("") }
    var repaymentDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val symbol = if (loan.currency == "USD") "$" else "৳"

    val daysDiff = remember(loan.dueDate) {
        val diffMs = loan.dueDate - System.currentTimeMillis()
        (diffMs / (1000 * 60 * 60 * 24)).toInt()
    }

    val statusText = when {
        loan.isRepaid -> "Settled"
        daysDiff < 0 -> "${-daysDiff} days overdue"
        daysDiff == 0 -> "Due today"
        else -> "$daysDiff days left"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(IconArrowBackDetail, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Info Header
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(loan.personName, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        SuggestionChip(
                            onClick = {},
                            label = { Text(if (loan.type == "LENT") "Lent" else "Borrowed") }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total Value: $symbol${String.format(Locale.US, "%.2f", loan.amount)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Logged Date: ${dateFormat.format(Date(loan.date))}", fontSize = 13.sp)
                    Text("Target Due Date: ${dateFormat.format(Date(loan.dueDate))}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Status: $statusText",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (loan.isRepaid) Color.Gray else if (daysDiff < 0) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
                    )

                    if (loan.note.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Notes:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(loan.note, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Quick Actions: WhatsApp / SMS Reminders
            if (!loan.isRepaid) {
                Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            val msg = "Hi ${loan.personName}, just a friendly reminder regarding our pending record of $symbol${String.format(Locale.US, "%.2f", loan.amount)} due on ${dateFormat.format(Date(loan.dueDate))}. Thanks!"
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = ("https://api.whatsapp.com/send?text=" + Uri.encode(msg)).toUri()
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Text("WhatsApp", color = Color.White)
                    }
                    Button(
                        onClick = {
                            val msg = "Hi ${loan.personName}, just a friendly reminder for record of $symbol${String.format(Locale.US, "%.2f", loan.amount)} due on ${dateFormat.format(Date(loan.dueDate))}. Thanks!"
                            val intent = Intent(Intent.ACTION_VIEW, ("sms:?body=" + Uri.encode(msg)).toUri())
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Send SMS")
                    }
                }
            }

            // Repayment Progress Bar
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Payment Progress", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val progress = if (loan.amount > 0) (totalRepaid / loan.amount).toFloat().coerceIn(0f, 1f) else 0f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Repaid: $symbol${String.format(Locale.US, "%.2f", totalRepaid)}", fontSize = 13.sp)
                        Text("Remaining: $symbol${String.format(Locale.US, "%.2f", (loan.amount - totalRepaid).coerceAtLeast(0.0))}", fontSize = 13.sp)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = loan.isRepaid, onCheckedChange = { viewModel.toggleLoanRepaid(loan) })
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mark as Fully Settled", fontWeight = FontWeight.Medium)
            }

            Text("Repayment Entries Log", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            if (repayments.isEmpty()) {
                Text("No payment installments recorded yet.", fontSize = 14.sp, color = Color.Gray)
            } else {
                repayments.forEachIndexed { index, entry ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Installment #${index + 1}: $symbol${String.format(Locale.US, "%.2f", entry.amount)}", fontWeight = FontWeight.Medium)
                            Text(dateFormat.format(Date(entry.date)), fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Log Partial Payment Entry", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    OutlinedTextField(
                        value = repaymentAmountStr,
                        onValueChange = { repaymentAmountStr = it },
                        label = { Text("Repayment Amount ($symbol)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dateFormat.format(Date(repaymentDate)),
                        onValueChange = {},
                        label = { Text("Repayment Entry Date") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }
                    )
                    Button(
                        onClick = {
                            val amt = repaymentAmountStr.toDoubleOrNull()
                            if (amt != null && amt > 0) {
                                viewModel.addRepayment(loan, amt, repaymentDate)
                                repaymentAmountStr = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Record Installment Payment")
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = repaymentDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { repaymentDate = it }
                    showDatePicker = false
                }) { Text("Select") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
