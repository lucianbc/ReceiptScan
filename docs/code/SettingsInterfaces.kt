interface ReceiptDefaults {
    val currency: Currency
    val category: Category
}

interface CollectingOption {
    val enabled: Boolean
    val appId: String
}
