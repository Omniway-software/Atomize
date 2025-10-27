package com.infinitysoftware.atomize.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.theme.White

@Composable
fun MoreOptionsItemMenu(onEdit: () -> Unit, onDelete: () -> Unit){
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.cd_more_options)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(White)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dialog_edit)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.cd_edit)
                    )
                },
                onClick = {
                    onEdit()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dialog_delete)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cd_delete)
                    )
                },
                onClick = {
                    onDelete()
                    expanded = false
                }
            )
        }
    }
}