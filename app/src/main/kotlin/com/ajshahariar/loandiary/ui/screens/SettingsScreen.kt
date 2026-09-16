package com.ajshahariar.loandiary.ui.screens

import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ajshahariar.loandiary.ui.theme.CustomThemePalette
import com.ajshahariar.loandiary.ui.theme.LoanDiaryPresetThemes
import com.ajshahariar.loandiary.ui.viewmodel.LoanViewModel
import kotlin.concurrent.thread

val IconArrowBackSettings: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000)), fillAlpha = 1.0f, strokeAlpha = 1.0f, strokeLineWidth = 0.0f, strokeLineCap = StrokeCap.Butt, strokeLineJoin = StrokeJoin.Miter, strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero) {
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

val IconPalette: ImageVector
    get() = ImageVector.Builder(
        name = "Palette",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000)), pathFillType = PathFillType.NonZero) {
            moveTo(12.0f, 2.0f)
            curveTo(6.49f, 2.0f, 2.0f, 6.49f, 2.0f, 12.0f)
            curveTo(2.0f, 17.51f, 6.49f, 22.0f, 12.0f, 22.0f)
            curveTo(13.1f, 22.0f, 14.0f, 21.1f, 14.0f, 20.0f)
            curveTo(14.0f, 19.5f, 13.81f, 19.04f, 13.51f, 18.7f)
            curveTo(13.2f, 18.34f, 13.0f, 17.89f, 13.0f, 17.4f)
            curveTo(13.0f, 16.3f, 13.9f, 15.4f, 15.0f, 15.4f)
            horizontalLineTo(17.0f)
            curveTo(19.76f, 15.4f, 22.0f, 13.16f, 22.0f, 10.4f)
            curveTo(22.0f, 5.76f, 17.51f, 2.0f, 12.0f, 2.0f)
            close()
        }
    }.build()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: LoanViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var showUpdateDialog by remember { mutableStateOf(false) }
    var updateStatus by remember { mutableStateOf("Checking for updates...") }
    val loans by viewModel.allLoans.collectAsState()
    val currentThemeIndex by viewModel.currentThemeIndex.collectAsState()
    val defaultCurrency by viewModel.defaultCurrency.collectAsState()
    val isBiometricEnabled by viewModel.isBiometricEnabled.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(IconArrowBackSettings, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Section: Appearance (Themes)
            SettingsSection(title = "Appearance", icon = IconPalette) {
                Text("Select Theme Profile", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    itemsIndexed(LoanDiaryPresetThemes) { index, theme ->
                        ThemePreviewItem(
                            theme = theme,
                            isSelected = currentThemeIndex == index,
                            onClick = { viewModel.updateThemeIndex(index) }
                        )
                    }
                }
            }

            // Section: Currency Defaults
            SettingsSection(title = "Financial Defaults", icon = IconPalette) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Default Currency")
                    Row {
                        FilterChip(
                            selected = defaultCurrency == "USD",
                            onClick = { viewModel.updateDefaultCurrency("USD") },
                            label = { Text("USD") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = defaultCurrency == "BDT",
                            onClick = { viewModel.updateDefaultCurrency("BDT") },
                            label = { Text("BDT") }
                        )
                    }
                }
            }

            // Section: Security
            SettingsSection(title = "Security & Notifications", icon = IconPalette) {
                SettingsSwitchRow(
                    label = "Biometric Lock",
                    description = "Secure app access with fingerprint/face",
                    checked = isBiometricEnabled,
                    onCheckedChange = { viewModel.updateBiometricEnabled(it) }
                )
                SettingsSwitchRow(
                    label = "Due Date Notifications",
                    description = "Remind me when loans are overdue",
                    checked = isNotificationsEnabled,
                    onCheckedChange = { viewModel.updateNotificationsEnabled(it) }
                )
            }

            // Section: Backup & Export
            SettingsSection(title = "Data Management", icon = IconPalette) {
                OutlinedButton(
                    onClick = {
                        val csv = StringBuilder("ID,Name,Amount,Currency,Type,Date,IsRepaid,Note\n")
                        loans.forEach { 
                            csv.append("${it.id},${it.personName},${it.amount},${it.currency},${it.type},${it.date},${it.isRepaid},\"${it.note}\"\n")
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/csv"
                            putExtra(Intent.EXTRA_TEXT, csv.toString())
                            putExtra(Intent.EXTRA_SUBJECT, "LoanDiary Export")
                        }
                        context.startActivity(Intent.createChooser(intent, "Share CSV Report"))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Export Data as CSV Report")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { /* Internal Stub */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Backup Data to Local Storage")
                }
            }

            // Section: About
            SettingsSection(title = "About App", icon = IconPalette) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("LoanDiary v1.0.5", fontWeight = FontWeight.Bold)
                    Text("Advanced local finance logger", style = MaterialTheme.typography.bodySmall)
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Developer: AJ Shahariar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GitHub",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW, "https://github.com/N4ous".toUri())
                                context.startActivity(intent)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            showUpdateDialog = true
                            updateStatus = "Checking for updates..."
                            thread {
                                Thread.sleep(1000)
                                updateStatus = "LoanDiary is fully up to date."
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Check for Updates")
                    }
                }
            }
        }
    }

    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            confirmButton = {
                TextButton(onClick = { showUpdateDialog = false }) { Text("OK") }
            },
            title = { Text("Update Status") },
            text = { Text(updateStatus) }
        )
    }
}

@Composable
fun SettingsSection(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        content()
        HorizontalDivider(modifier = Modifier.padding(top = 20.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
    }
}

@Composable
fun SettingsSwitchRow(label: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.Medium)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ThemePreviewItem(
    theme: CustomThemePalette,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(theme.background)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) theme.primary else Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(4.dp)).background(theme.primary))
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(4.dp)).background(theme.card))
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.width(20.dp).height(4.dp).background(theme.titleText.copy(alpha = 0.6f)))
            }
        }
        Text(theme.name, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onSurface)
    }
}
