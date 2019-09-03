class ReceiptsUseCaseImpl @Inject constructor(
    private val repository: ReceiptsRepository,
    private val manageFactory: ManageReceiptUseCase.Factory
) : ReceiptsUseCase {
    override fun list(): Flowable<List<ReceiptListItem>> =
        repository.listReceipts()

    override fun fetch(receiptId: ReceiptId): ReceiptsUseCase.Manage {
        return repository
            .getReceipt(receiptId)
            .subscribeOn(Schedulers.io())
            .let { manageFactory.create(it) }
    }
}

interface ReceiptsRepository {
    fun listReceipts(): Flowable<List<ReceiptListItem>>
    fun getReceipt(receiptId: ReceiptId): Flowable<Receipt>
}