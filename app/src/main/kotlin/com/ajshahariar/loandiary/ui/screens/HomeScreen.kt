package com.ajshahariar.loandiary.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ajshahariar.loandiary.data.Loan
import com.ajshahariar.loandiary.ui.theme.LocalCustomColors
import com.ajshahariar.loandiary.ui.viewmodel.LoanViewModel
import java.text.SimpleDateFormat
import java.util.*

val IconAdd: ImageVector
    get() = ImageVector.Builder(
        name = "Add",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(19.0f, 13.0f)
            horizontalLineTo(13.0f)
            verticalLineTo(19.0f)
            horizontalLineTo(11.0f)
            verticalLineTo(13.0f)
            horizontalLineTo(5.0f)
            verticalLineTo(11.0f)
            horizontalLineTo(11.0f)
            verticalLineTo(5.0f)
            horizontalLineTo(13.0f)
            verticalLineTo(11.0f)
            horizontalLineTo(19.0f)
            verticalLineTo(13.0f)
            close()
        }
    }.build()

val IconDelete: ImageVector
    get() = ImageVector.Builder(
        name = "Delete",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(6.0f, 19.0f)
            curveTo(6.0f, 20.1f, 6.9f, 21.0f, 8.0f, 21.0f)
            horizontalLineTo(16.0f)
            curveTo(17.1f, 21.0f, 18.0f, 20.1f, 18.0f, 19.0f)
            verticalLineTo(7.0f)
            horizontalLineTo(6.0f)
            verticalLineTo(19.0f)
            close()
            moveTo(19.0f, 4.0f)
            horizontalLineTo(15.5f)
            lineTo(14.5f, 3.0f)
            horizontalLineTo(9.5f)
            lineTo(8.5f, 4.0f)
            horizontalLineTo(5.0f)
            verticalLineTo(6.0f)
            horizontalLineTo(19.0f)
            verticalLineTo(4.0f)
            close()
        }
    }.build()

val IconSettings: ImageVector
    get() = ImageVector.Builder(
        name = "Settings",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(19.43f, 12.98f)
            curveTo(19.47f, 12.66f, 19.5f, 12.34f, 19.5f, 12.0f)
            curveTo(19.5f, 11.66f, 19.47f, 11.34f, 19.43f, 11.02f)
            lineTo(21.54f, 9.37f)
            curveTo(21.73f, 9.22f, 21.78f, 8.95f, 21.66f, 8.73f)
            lineTo(19.66f, 5.27f)
            curveTo(19.54f, 5.05f, 19.27f, 4.97f, 19.05f, 5.05f)
            lineTo(16.56f, 6.05f)
            curveTo(16.04f, 5.65f, 15.47f, 5.32f, 14.86f, 5.07f)
            lineTo(14.48f, 2.42f)
            curveTo(14.44f, 2.18f, 14.24f, 2.0f, 14.0f, 2.0f)
            horizontalLineTo(10.0f)
            curveTo(9.76f, 2.0f, 9.56f, 2.18f, 9.52f, 2.42f)
            lineTo(9.14f, 5.07f)
            curveTo(8.53f, 5.32f, 7.96f, 5.66f, 7.44f, 6.05f)
            lineTo(4.95f, 5.05f)
            curveTo(4.73f, 4.97f, 4.46f, 5.05f, 4.34f, 5.27f)
            lineTo(2.34f, 8.73f)
            curveTo(2.22f, 8.95f, 2.27f, 9.22f, 2.46f, 9.37f)
            lineTo(4.57f, 11.02f)
            curveTo(4.53f, 11.34f, 4.5f, 11.66f, 4.5f, 12.0f)
            curveTo(4.5f, 12.34f, 4.53f, 12.66f, 4.57f, 12.98f)
            lineTo(2.46f, 14.63f)
            curveTo(2.27f, 14.78f, 2.22f, 15.05f, 2.34f, 15.27f)
            lineTo(4.34f, 18.73f)
            curveTo(4.46f, 18.95f, 4.73f, 19.03f, 4.95f, 18.95f)
            lineTo(7.44f, 17.95f)
            curveTo(7.96f, 18.35f, 8.53f, 18.68f, 9.14f, 18.93f)
            lineTo(9.52f, 21.58f)
            curveTo(9.56f, 21.82f, 9.76f, 22.0f, 10.0f, 22.0f)
            horizontalLineTo(14.0f)
            curveTo(14.24f, 22.0f, 14.44f, 21.82f, 14.48f, 21.58f)
            lineTo(14.86f, 18.93f)
            curveTo(15.47f, 18.68f, 16.04f, 18.34f, 16.56f, 17.95f)
            lineTo(19.05f, 18.95f)
            curveTo(19.27f, 19.03f, 19.54f, 18.95f, 19.66f, 18.73f)
            lineTo(21.66f, 15.27f)
            curveTo(21.78f, 15.05f, 21.73f, 14.78f, 21.54f, 14.63f)
            lineTo(19.43f, 12.98f)
            close()
        }
    }.build()

// Helper to sum repayments from JSON string
fun sumRepayments(json: String): Double {
    var total = 0.0
    try {
        val clean = json.replace("[", "").replace("]", "").trim()
        if (clean.isEmpty()) return 0.0
        val chunks = clean.split("},")
        for (chunk in chunks) {
            val normalized = chunk.replace("{", "").replace("}", "")
            val pairs = normalized.split(",")
            for (pair in pairs) {
                val kv = pair.split(":")
                if (kv.size >= 2) {
                    val k = kv[0].replace("\"", "").trim()
                    val v = kv[1].trim()
                    if (k == "amount") {
                        total += v.toDoubleOrNull() ?: 0.0
                    }
                }
            }
        }
    } catch (_: Exception) {}
    return total
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: LoanViewModel,
    onNavigateToAddLoan: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDetail: (Loan) -> Unit
) {
    val loans by viewModel.allLoans.collectAsState()
    val customColors = LocalCustomColors.current
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterTab by remember { mutableStateOf(0) } // 0 = All, 1 = Pending, 2 = Repaid
    var selectedAnalyticsTab by remember { mutableStateOf(0) } // 0 = Overview, 1 = Lent, 2 = Borrowed

    // Multi-currency metrics aggregation - LIVE CALCULATION
    val pendingLentUSD = remember(loans) { 
        loans.filter { it.type == "LENT" && !it.isRepaid && it.currency == "USD" }
            .sumOf { it.amount - sumRepayments(it.repaymentsJson) } 
    }
    val pendingBorrowedUSD = remember(loans) { 
        loans.filter { it.type == "BORROWED" && !it.isRepaid && it.currency == "USD" }
            .sumOf { it.amount - sumRepayments(it.repaymentsJson) } 
    }
    val pendingLentBDT = remember(loans) { 
        loans.filter { it.type == "LENT" && !it.isRepaid && it.currency == "BDT" }
            .sumOf { it.amount - sumRepayments(it.repaymentsJson) } 
    }
    val pendingBorrowedBDT = remember(loans) { 
        loans.filter { it.type == "BORROWED" && !it.isRepaid && it.currency == "BDT" }
            .sumOf { it.amount - sumRepayments(it.repaymentsJson) } 
    }

    // SETTLED (Repaid)
    val settledLentUSD = remember(loans) { 
        loans.filter { it.type == "LENT" && it.currency == "USD" }
            .sumOf { sumRepayments(it.repaymentsJson) } 
    }
    val settledLentBDT = remember(loans) { 
        loans.filter { it.type == "LENT" && it.currency == "BDT" }
            .sumOf { sumRepayments(it.repaymentsJson) } 
    }
    val settledBorrowedUSD = remember(loans) { 
        loans.filter { it.type == "BORROWED" && it.currency == "USD" }
            .sumOf { sumRepayments(it.repaymentsJson) } 
    }
    val settledBorrowedBDT = remember(loans) { 
        loans.filter { it.type == "BORROWED" && it.currency == "BDT" }
            .sumOf { sumRepayments(it.repaymentsJson) } 
    }

    // TOTALS (Original Principals)
    val totalLentUSD = remember(loans) { loans.filter { it.type == "LENT" && it.currency == "USD" }.sumOf { it.amount } }
    val totalLentBDT = remember(loans) { loans.filter { it.type == "LENT" && it.currency == "BDT" }.sumOf { it.amount } }
    val totalBorrowedUSD = remember(loans) { loans.filter { it.type == "BORROWED" && it.currency == "USD" }.sumOf { it.amount } }
    val totalBorrowedBDT = remember(loans) { loans.filter { it.type == "BORROWED" && it.currency == "BDT" }.sumOf { it.amount } }

    // Filter results
    val filteredLoans = remember(loans, searchQuery, selectedFilterTab) {
        loans.filter { loan ->
            val matchesSearch = loan.personName.contains(searchQuery, ignoreCase = true) || 
                                loan.note.contains(searchQuery, ignoreCase = true)
            val matchesTab = when (selectedFilterTab) {
                1 -> !loan.isRepaid
                2 -> loan.isRepaid
                else -> true
            }
            matchesSearch && matchesTab
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LoanDiary", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(IconSettings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddLoan) {
                Icon(IconAdd, contentDescription = "Add Loan")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Dashboard Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = customColors.card)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Analytics Sub-Tabs Row selector
                    SecondaryScrollableTabRow(
                        selectedTabIndex = selectedAnalyticsTab,
                        containerColor = Color.Transparent,
                        contentColor = customColors.primary,
                        divider = {},
                        edgePadding = 0.dp,
                        modifier = Modifier.fillMaxWidth().height(36.dp)
                    ) {
                        Tab(
                            selected = selectedAnalyticsTab == 0,
                            onClick = { selectedAnalyticsTab = 0 },
                            selectedContentColor = customColors.primary,
                            unselectedContentColor = customColors.hintText
                        ) {
                            Text("Overview", fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                        }
                        Tab(
                            selected = selectedAnalyticsTab == 1,
                            onClick = { selectedAnalyticsTab = 1 },
                            selectedContentColor = customColors.primary,
                            unselectedContentColor = customColors.hintText
                        ) {
                            Text("Lent Insights", fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                        }
                        Tab(
                            selected = selectedAnalyticsTab == 2,
                            onClick = { selectedAnalyticsTab = 2 },
                            selectedContentColor = customColors.primary,
                            unselectedContentColor = customColors.hintText
                        ) {
                            Text("Borrowed Insights", fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Numeric Labels breakdown column based on selected analytical segment
                        Column(modifier = Modifier.weight(1f)) {
                            val labelStyle = MaterialTheme.typography.labelMedium.copy(color = customColors.bodyText)
                            val titleStyle = MaterialTheme.typography.titleSmall.copy(color = customColors.titleText, fontWeight = FontWeight.Bold)
                            
                            when (selectedAnalyticsTab) {
                                1 -> {
                                    Text("Lent Stats (Receivables)", style = titleStyle, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Principal USD: $$totalLentUSD", style = labelStyle, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Received USD: $$settledLentUSD", style = labelStyle, fontSize = 11.sp, color = customColors.hintText)
                                    Text("Remaining USD: $$pendingLentUSD", style = labelStyle, fontSize = 11.sp, color = customColors.primary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Principal BDT: ৳$totalLentBDT", style = labelStyle, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Received BDT: ৳$settledLentBDT", style = labelStyle, fontSize = 11.sp, color = customColors.hintText)
                                    Text("Remaining BDT: ৳$pendingLentBDT", style = labelStyle, fontSize = 11.sp, color = customColors.primary)
                                }
                                2 -> {
                                    Text("Borrowed Stats (Payables)", style = titleStyle, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Principal USD: $$totalBorrowedUSD", style = labelStyle, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Repaid USD: $$settledBorrowedUSD", style = labelStyle, fontSize = 11.sp, color = customColors.hintText)
                                    Text("Remaining USD: $$pendingBorrowedUSD", style = labelStyle, fontSize = 11.sp, color = Color(0xFFC62828))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Principal BDT: ৳$totalBorrowedBDT", style = labelStyle, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Repaid BDT: ৳$settledBorrowedBDT", style = labelStyle, fontSize = 11.sp, color = customColors.hintText)
                                    Text("Remaining BDT: ৳$pendingBorrowedBDT", style = labelStyle, fontSize = 11.sp, color = Color(0xFFC62828))
                                }
                                else -> {
                                    Text("Unified Dashboard Overview", style = titleStyle, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Net Lent USD: $$pendingLentUSD", style = labelStyle, fontSize = 11.sp, color = customColors.primary)
                                    Text("Net Borrowed USD: $$pendingBorrowedUSD", style = labelStyle, fontSize = 11.sp, color = Color(0xFFC62828))
                                    Text("Net Lent BDT: ৳$pendingLentBDT", style = labelStyle, fontSize = 11.sp, color = customColors.primary)
                                    Text("Net Borrowed BDT: ৳$pendingBorrowedBDT", style = labelStyle, fontSize = 11.sp, color = Color(0xFFC62828))
                                }
                            }
                        }

                        // Canvas Graphics render segment matching active analytics flow tab selection
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp).padding(4.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                when (selectedAnalyticsTab) {
                                    1 -> {
                                        val active = pendingLentUSD + (pendingLentBDT / 110.0)
                                        val settled = settledLentUSD + (settledLentBDT / 110.0)
                                        val total = active + settled
                                        if (total > 0) {
                                            val sweepActive = ((active / total) * 360f).toFloat()
                                            drawArc(customColors.primary, -90f, sweepActive, false, style = Stroke(width = 14f, cap = StrokeCap.Round))
                                            drawArc(customColors.hintText.copy(alpha = 0.4f), -90f + sweepActive, 360f - sweepActive, false, style = Stroke(width = 14f, cap = StrokeCap.Round))
                                        }
                                    }
                                    2 -> {
                                        val active = pendingBorrowedUSD + (pendingBorrowedBDT / 110.0)
                                        val settled = settledBorrowedUSD + (settledBorrowedBDT / 110.0)
                                        val total = active + settled
                                        if (total > 0) {
                                            val sweepActive = ((active / total) * 360f).toFloat()
                                            drawArc(Color(0xFFC62828), -90f, sweepActive, false, style = Stroke(width = 14f, cap = StrokeCap.Round))
                                            drawArc(customColors.hintText.copy(alpha = 0.4f), -90f + sweepActive, 360f - sweepActive, false, style = Stroke(width = 14f, cap = StrokeCap.Round))
                                        }
                                    }
                                    else -> {
                                        val sumLent = pendingLentUSD + (pendingLentBDT / 110.0)
                                        val sumBorrowed = pendingBorrowedUSD + (pendingBorrowedBDT / 110.0)
                                        val totalVolume = sumLent + sumBorrowed
                                        if (totalVolume > 0) {
                                            val sweepLent = ((sumLent / totalVolume) * 360f).toFloat()
                                            drawArc(customColors.primary, -90f, sweepLent, false, style = Stroke(width = 14f, cap = StrokeCap.Round))
                                            drawArc(Color(0xFFC62828), -90f + sweepLent, 360f - sweepLent, false, style = Stroke(width = 14f, cap = StrokeCap.Round))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Search Bar Widget
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search records by name...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true
            )

            // Tabs for Filters
            PrimaryTabRow(
                selectedTabIndex = selectedFilterTab,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Tab(selected = selectedFilterTab == 0, onClick = { selectedFilterTab = 0 }) {
                    Text("All (${loans.size})", modifier = Modifier.padding(12.dp))
                }
                Tab(selected = selectedFilterTab == 1, onClick = { selectedFilterTab = 1 }) {
                    Text("Pending", modifier = Modifier.padding(12.dp))
                }
                Tab(selected = selectedFilterTab == 2, onClick = { selectedFilterTab = 2 }) {
                    Text("Repaid", modifier = Modifier.padding(12.dp))
                }
            }

            // Dynamic List
            if (filteredLoans.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No records found.",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredLoans) { loan ->
                        LoanItem(
                            loan = loan,
                            onClick = { onNavigateToDetail(loan) },
                            onDeleteClick = { viewModel.deleteLoan(loan) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoanItem(
    loan: Loan,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val dateString = remember(loan.date) { dateFormat.format(Date(loan.date)) }

    val daysDiff = remember(loan.dueDate) {
        val diffMs = loan.dueDate - System.currentTimeMillis()
        (diffMs / (1000 * 60 * 60 * 24)).toInt()
    }

    val reminderString = when {
        loan.isRepaid -> "Settled"
        daysDiff < 0 -> "${-daysDiff} days overdue"
        daysDiff == 0 -> "Due today"
        else -> "$daysDiff days left"
    }

    val reminderColor = when {
        loan.isRepaid -> Color.Gray
        daysDiff < 0 -> MaterialTheme.colorScheme.error
        daysDiff == 0 -> Color(0xFFE65100)
        else -> Color(0xFF2E7D32)
    }

    val symbol = if (loan.currency == "USD") "$" else "৳"

    // Local balance calculation for the card
    val remainingBalance = remember(loan.repaymentsJson) {
        loan.amount - sumRepayments(loan.repaymentsJson)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (loan.isRepaid) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = loan.personName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (loan.isRepaid) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    
                    SuggestionChip(
                        onClick = {},
                        label = { Text(if (loan.type == "LENT") "Lent" else "Borrowed", fontSize = 10.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = if (loan.type == "LENT") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            labelColor = if (loan.type == "LENT") Color(0xFF2E7D32) else Color(0xFFC62828)
                        ),
                        border = null,
                        modifier = Modifier.height(20.dp)
                    )
                }

                Text(
                    text = if (loan.type == "LENT") "Collect Reminder: $reminderString" else "Repay Reminder: $reminderString",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = reminderColor,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "Taken: $dateString",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$symbol${String.format(Locale.US, "%.2f", remainingBalance)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (loan.isRepaid) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else if (loan.type == "LENT") {
                        Color(0xFF2E7D32)
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    textDecoration = if (loan.isRepaid) TextDecoration.LineThrough else TextDecoration.None
                )
                if (remainingBalance != loan.amount && !loan.isRepaid) {
                    Text(
                        text = "of $symbol${String.format(Locale.US, "%.2f", loan.amount)}",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = IconDelete,
                    contentDescription = "Delete record",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
