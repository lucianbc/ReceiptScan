package com.lucianbc.receiptscan.domain.receipts

import com.lucianbc.receiptscan.domain.extract.rules.show
import com.lucianbc.receiptscan.domain.model.ImagePath
import com.lucianbc.receiptscan.util.show
import com.lucianbc.receiptscan.util.takeSingle
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Flowable
import io.reactivex.Single

class ManageReceiptUseCase @AssistedInject constructor(
    @Assisted source: Flowable<Receipt>
) : ReceiptsUseCase.Manage {
    override val receipt = source.replay(1).autoConnect()

    override fun exportReceipt(): Single<String> =
        receipt.map { it.exported() }.takeSingle()

    override fun exportPath(): Single<ImagePath> =
        receipt.map { ImagePath(it.imagePath) }.takeSingle()

    override fun exportBoth(): Single<Pair<String, ImagePath>> =
        receipt.map { it.exported() to ImagePath(it.imagePath) }.takeSingle()

    private fun Receipt.exported(): String {
        val lines = mutableListOf(
            "Merchant: ${this.merchantName}",
            "Date: ${this.date.show()}",
            "Total: ${this.total.show()} ${this.currency.currencyCode}"
        ) + this.productEntities.map { "${it.name}    ${it.price}" }
        return lines.joinToString("\n")
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(source: Flowable<Receipt>): ManageReceiptUseCase
    }
}
