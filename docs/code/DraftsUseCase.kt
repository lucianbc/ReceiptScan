interface DraftsUseCase {
    fun list(): Flowable<List<DraftListItem>>
    fun fetch(draftId: DraftId): Manage

    interface Manage {
        val value: Flowable<Draft>
        val image: Flowable<Bitmap>
        fun <T> update(newVal: T, mapper: (T, Draft) -> Draft): Completable
        fun delete(): Completable
        fun moveToValid(): Completable
        fun createProduct(): Single<Product>
        fun updateProduct(product: Product): Completable
        fun deleteProduct(product: Product): Completable
    }
}