interface ReceiptsUseCase : ISourcesManager {

    fun fetch(receiptId: ReceiptId): Manage

    interface Manage {
        val receipt: Flowable<Receipt>
        fun exportReceipt(): Single<String>
        fun exportPath(): Single<ImagePath>
        fun exportBoth(): Single<Pair<String, ImagePath>>
    }
}

interface ISourcesManager {
    val availableCurrencies: Flowable<List<Currency>>
    val availableMonths: Flowable<List<Date>>
    val categories: Flowable<List<SpendingGroup>>
    val currentSpending: Flowable<SpendingGroup>
    val transactions: Flowable<List<ReceiptListItem>>

    fun fetchForCurrency(currency: Currency)
    fun fetchForMonth(month: Date)
    fun fetchForCategory(spendingGroup: SpendingGroup)
}

data class SpendingGroup (
    val group: Group,
    val total: Float,
    val currency: Currency
)

sealed class Group {
    data class Categorized(val value: Category): Group()
    object Total: Group()
}