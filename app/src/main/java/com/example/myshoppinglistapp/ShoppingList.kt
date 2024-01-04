package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties


data class ShoppingItem(
    val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }

    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add an Item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item = item, onEditCompleter = { editedName, editedQty ->

                        sItems = sItems.map { it.copy(isEditing = false) }
                        val editedItem = sItems.find { it.id == item.id }

//                        item.isEditing = false
//                        item.name = editedName
//                        item.quantity = editedQty

                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQty
                        }
                    })
                } else {
                    ShoppingListItem(item = item, onEditClick = {
                        sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                            item.isEditing = true
                    }, onDeleteClick = {
                        sItems = sItems - item
                    })
                }
            }
        }
    }

    if (showDialog) {
        itemName = ""
        itemQuantity = ""
        AlertDialog(title = { Text(text = "Add Shopping Item") }, text = {
            Column {
                OutlinedTextField(value = itemName,
                    onValueChange = { itemName = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = {
                        Text(text = "Item name")
                    })
                OutlinedTextField(value = itemQuantity,
                    onValueChange = { itemQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = {
                        Text(text = "Item Quantity")
                    })
            }
        }, onDismissRequest = {
            showDialog = false
        }, confirmButton = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                        showDialog = false

                        val newItem = ShoppingItem(
                            id = sItems.size + 1,
                            name = itemName,
                            quantity = itemQuantity.toIntOrNull() ?: 1
                        )

                        sItems = sItems + newItem
                    }
                }) {
                    Text(text = "Add Item")
                }
                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        })
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(width = 2.dp, color = Color.Cyan),
                shape = RoundedCornerShape(percent = 20)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Button")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Button")
            }
        }

    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditCompleter: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQauntity by remember { mutableStateOf(item.quantity) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.wrapContentSize()) {
            BasicTextField(value = editedName, onValueChange = { editedName = it })
            BasicTextField(value = editedQauntity.toString(),
                onValueChange = { editedQauntity = it.toIntOrNull() ?: 1 })
        }

        Button(onClick = {
            isEditing = false
            onEditCompleter(editedName, editedQauntity)
        }) {
            Text(text = "Save")
        }
    }
}