package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucianbc.receiptscan.v2.domain.Category
import com.lucianbc.receiptscan.v2.ui.components.BackButton
import com.lucianbc.receiptscan.v2.ui.components.NavigationBarParams
import com.lucianbc.receiptscan.v2.ui.components.Screen
import com.lucianbc.receiptscan.v2.ui.viewModels.DraftViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*


@Composable
private fun ReceiptContainer(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    onBack: () -> Unit = {},
    children: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(top = 64.dp, bottom = 32.dp, start = 32.dp, end = 32.dp)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            children()
        }

        BackButton(standalone = true, onClick = onBack::invoke)
    }
}


@Composable
fun ReceiptScreen() {
    val receipt = receiptValue
    Screen {
        ReceiptContainer {
            CategoryAvatar(category = receipt.category)
            HorizontalSpace()
            Text(text = receipt.category.name)
            HorizontalSpace()
            Text(text = receipt.merchant)
            HorizontalSpace()
            Text(text = receipt.date.show)
            HorizontalSpace()
            Row {
                Text(text = receipt.total.show)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = receipt.currency.show)
            }
            HorizontalSpace()
            SpacedRow {
                Text(text = "Item")
                Text(text = "Price")
            }

            HorizontalSpace()
            Line()
            HorizontalSpace()

            receipt.items.forEach {
                SpacedRow {
                    Text(text = it.name)
                    Text(text = it.price.show)
                }
                HorizontalSpace()
            }

            Line()
            HorizontalSpace()

            SpacedRow {
                Text(text = "Sum")
                Text(text = receipt.total.show)
            }
        }
    }
}

private val iconSize = 16.dp
private val iconSpacing = 8.dp

@Composable
private fun EditableRow(label: String, value: String, onClick: OnClick) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick?.invoke() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, modifier = Modifier.weight(0.5f))
        Text(text = value, modifier = Modifier.weight(0.5f), textAlign = TextAlign.Right)
        Icon(Icons.Default.Edit, "Edit $label", modifier = Modifier
            .padding(start = 8.dp)
            .size(iconSize)
        )
    }
}


private sealed class DialogProps(open val title: String) {
    data class SingleField(
        override val title: String,
        val label: String,
        val defaultValue: String?,
        val onSave: (String) -> Unit,
    ) : DialogProps(title)

    data class TwoFields(
        override val title: String,
        val label1: String,
        val label2: String,
        val defaultValue1: String?,
        val defaultValue2: String?,
        val onSave: (String, String) -> Unit,
    ) : DialogProps(title)
}

private val merchantDialog: DialogProps = DialogProps.SingleField(
    "Edit merchant",
    "Merchant",
    "",
) {}

private val totalDialog: DialogProps = DialogProps.SingleField(
    "Edit total",
    "Total",
    "",
) {}


@Composable
private fun DialogLayout(props: MutableState<DialogProps?>) {
    val propsValue = props.value
    val dismiss = {
        props.value = null
    }
    if (propsValue != null) {
        AlertDialog(
            onDismissRequest = dismiss,
            title = { Text(text = propsValue.title) },
            text = {
                when (propsValue) {
                    is DialogProps.SingleField -> {
                        var currentValue by mutableStateOf(propsValue.defaultValue ?: "")
                        Column {
                            OutlinedTextField(value = currentValue,
                                label = { Text(propsValue.label) },
                                onValueChange = {
                                    currentValue = it
                                })
                        }
                    }
                    is DialogProps.TwoFields -> {
                        var value1 by mutableStateOf(propsValue.defaultValue1 ?: "")
                        var value2 by mutableStateOf(propsValue.defaultValue2 ?: "")

                        Column {
                            OutlinedTextField(value = value1,
                                onValueChange = { value1 = it },
                                label = { Text(propsValue.label1) }
                            )

                            OutlinedTextField(
                                value = value2,
                                onValueChange = { value2 = it },
                                label = { Text(propsValue.label2) },
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {}, enabled = false) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = dismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

interface EditableReceiptParams : NavigationBarParams {
    fun goToCategories()
    fun goToCurrencies()

    object Empty : EditableReceiptParams {
        override fun goToCategories() {}
        override fun goToCurrencies() {}
        override fun goBack() {}
    }
}

@Composable
fun EditableReceiptScreen(params: EditableReceiptParams, viewModel: DraftViewModel) {
    val scrollState = rememberScrollState()
    val dialog = remember { mutableStateOf<DialogProps?>(null) }
    val receipt = viewModel.receipt.value
    Screen {
        DialogLayout(props = dialog)

        ReceiptContainer(modifier = Modifier.weight(1f),
            scrollState = scrollState,
            onBack = params::goBack) {
            CategoryAvatar(category = receipt.category)
            HorizontalSpace()
            EditableRow(label = "Category",
                value = receipt.category.name,
                onClick = params::goToCategories)
            HorizontalSpace()
            EditableRow(label = "Merchant", value = receipt.merchant) {
                dialog.value = merchantDialog
            }
            HorizontalSpace()
            EditableRow(label = "Date", value = receipt.date.show) {}
            HorizontalSpace()
            EditableRow(label = "Total", value = receipt.total.show) {
                dialog.value = totalDialog
            }
            HorizontalSpace()
            EditableRow(label = "Currency",
                value = receipt.currency.show,
                onClick = params::goToCurrencies)
            HorizontalSpace()

            HorizontalSpace()
            SpacedRow {
                Text(text = "Item")
                Text(text = "Price", modifier = Modifier.padding(end = iconSize + iconSpacing))
            }

            HorizontalSpace()
            Line()
            HorizontalSpace()

            receipt.items.forEach {
                EditableRow(label = it.name, value = it.price.show) {
                    dialog.value =
                        DialogProps.TwoFields(
                            "Edit item",
                            "Name",
                            "Price",
                            it.name,
                            it.price.show,
                        ) { _, _ -> }
                }
                HorizontalSpace()
            }

            TextButton(onClick = { /*TODO*/ }) {
                Text("Add new item")
            }

            HorizontalSpace()

            Line()
            HorizontalSpace()

            SpacedRow {
                Text(text = "Sum")
                Text(text = receipt.total.show,
                    modifier = Modifier.padding(end = iconSize + iconSpacing))
            }
        }

        Surface(modifier = Modifier
            .fillMaxWidth(),
            elevation = 8.dp
        ) {
            Row(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
            ) {
                OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                    Text(text = "Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Composable
private fun HorizontalSpace(height: Int = 8) = Spacer(modifier = Modifier.height(height.dp))

@Composable
private fun Line() = Divider(color = Color.Gray, thickness = 1.dp)

@Composable
private fun SpacedRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = content
    )
}

private val receiptValue = Receipt(
    category = Category.Grocery,
    merchant = "S.C. PROFI ROM FOOD S.R.L",
    date = Date(),
    total = 11.9,
    currency = Currency.getInstance("RON"),
    items = listOf(
        ReceiptItem("item", 4.2),
        ReceiptItem("item", 7.7),
    ).repeat(10),
)

private fun List<ReceiptItem>.repeat(times: Int) = (1..times)
    .flatMap { this }
    .mapIndexed { index, x -> x.copy(name = "${x.name} - $index") }

data class Receipt(
    val category: Category,
    val merchant: String,
    val date: Date,
    val total: Double,
    val currency: Currency,
    val items: List<ReceiptItem>,
)

data class ReceiptItem(
    val name: String,
    val price: Double,
)

private val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)

val Date.show: String
    get() = format.format(this)

val Double.show: String
    get() = this.toString()

val Currency.show: String
    get() = this.currencyCode

@Preview
@Composable
fun ReceiptScreenPreview() {
    ReceiptScreen()
}

@Preview
@Composable
fun EditableReceiptScreenPreview() {
    EditableReceiptScreen(EditableReceiptParams.Empty, object : DraftViewModel {
        override val receipt: StateFlow<Receipt> = MutableStateFlow(receiptValue)

        override fun setCategory(category: Category) {}

        override fun setMerchant(merchant: String) {}

        override fun setDate(date: Date) {}

        override fun setTotal(price: Double) {}

        override fun setCurrency(currency: Currency) {}

        override fun updateItem(item: ReceiptItem) {}

        override fun deleteItem(item: ReceiptItem) {}

        override fun addItem(item: ReceiptItem) {}
    })
}