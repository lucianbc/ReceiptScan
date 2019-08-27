@Entity(tableName = "receipt")
data class ReceiptEntity(
    val imagePath: String,
    val merchantName: String?,
    val date: Date?,
    val total: Float?,
    val currency: Currency?,
    val category: Category,
    val creationTimestamp: Date,
    val isDraft: Boolean,
    @PrimaryKey
    val id: Long? = null
)

@Entity(
    tableName = "ocrElement",
    foreignKeys = [
        ForeignKey(
            entity = ReceiptEntity::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["receiptId"]
        )
    ],
    indices = [Index("receiptId")]
)
data class OcrElementEntity(
    val text: String,
    val top: Int,
    val left: Int,
    val bottom: Int,
    val right: Int,
    val receiptId: Long? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)

@Entity(
    tableName = "product",
    foreignKeys = [
        ForeignKey(
            entity = ReceiptEntity::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["receiptId"]
        )
    ],
    indices = [Index("receiptId")]
)
data class ProductEntity(
    var name: String,
    var price: Float,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var receiptId: Long? = null
)