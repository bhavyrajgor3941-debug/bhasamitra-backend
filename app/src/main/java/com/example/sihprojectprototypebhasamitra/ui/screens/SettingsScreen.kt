package com.example.sihprojectprototypebhasamitra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val languages = listOf("Hindi", "English", "Gujarati", "Marathi", "Bengali", "Tamil", "Telugu")
    val (fromExpanded, setFromExpanded) = remember { mutableStateOf(false) }
    val (toExpanded, setToExpanded) = remember { mutableStateOf(false) }
    val (fromLang, setFromLang) = remember { mutableStateOf("Hindi") }
    val (toLang, setToLang) = remember { mutableStateOf("English") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Language Settings")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ExposedDropdownMenuBox(
                expanded = fromExpanded,
                onExpandedChange = { setFromExpanded(!fromExpanded) },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    readOnly = true,
                    value = fromLang,
                    onValueChange = { },
                    label = { Text("From") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                )
                ExposedDropdownMenu(
                    expanded = fromExpanded,
                    onDismissRequest = { setFromExpanded(false) },
                ) {
                    languages.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                setFromLang(selectionOption)
                                setFromExpanded(false)
                            },
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = toExpanded,
                onExpandedChange = { setToExpanded(!toExpanded) },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    readOnly = true,
                    value = toLang,
                    onValueChange = { },
                    label = { Text("To") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded) },
                )
                ExposedDropdownMenu(
                    expanded = toExpanded,
                    onDismissRequest = { setToExpanded(false) },
                ) {
                    languages.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                setToLang(selectionOption)
                                setToExpanded(false)
                            },
                        )
                    }
                }
            }
        }
        Text(text = "From $fromLang → To $toLang")
    }
}
