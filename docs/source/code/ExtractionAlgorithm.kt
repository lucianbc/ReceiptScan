typealias OcrElements = Sequence<OcrElement>

class Extractor @Inject constructor(
    private val defaults: ReceiptDefaults
) {
    operator fun invoke(elements: OcrElements): Draft {
        val receipt = RawReceipt.create(elements)
        val text = receipt.text
        val merchant = extractMerchant(receipt)
        val date = extractDate(text)
        val currency = defaults.currency
        val category = defaults.category
        val (total, products) = ProductsAndTotalStrategy(
            receipt
        ).execute()
        return Draft (
            merchant,
            date,
            total,
            currency,
            category,
            products.map { Product(it.name, it.price) },
            elements.map { OcrElement(it.text, it.top, it.bottom, it.left, it.right) }.toList()
        )
    }
}

class RawReceipt(private val lines: List<Line>) : Iterable<RawReceipt.Line> {
    override fun iterator(): Iterator<Line> = lines.iterator()

    class Line(private val elements: List<OcrElement>) : Iterable<OcrElement> {
        override fun iterator() = elements.iterator()
        val text by lazy { elements.joinToString(" ") { it.text } }
        val height by lazy { elements.map { it.height }.average() }
        val top by lazy { elements.map { it.top }.min()!! }
        val bottom by lazy { elements.map { it.bottom }.max()!! }
    }

    val averageLineHeight by lazy { this.lines.map { it.height }.average() }

    val text by lazy { lines.joinToString("\n") { it.joinToString("\t") { t -> t.text } } }

    companion object {
        private const val THRESHOLD = 0.5F
        fun create(elements: OcrElements): RawReceipt {
            val sorted = elements.sortedBy { t -> t.mid }
            val unifiedLines = LinkedList<Line>()

            var currentLine = ArrayList<OcrElement>()
            val boxesIterator = sorted.iterator()

            if (boxesIterator.hasNext()) {
                var lastBox = boxesIterator.next()
                currentLine.add(lastBox)

                while (boxesIterator.hasNext()) {
                    val crtBox = boxesIterator.next()
                    val threshVal = THRESHOLD * (crtBox.height + lastBox.height).toFloat() / 2
                    if (crtBox.mid - lastBox.mid < threshVal) {
                        currentLine.add(crtBox)
                    } else {
                        val sortedLine = currentLine.sortedBy { it.left }
                        unifiedLines.add(
                            Line(
                                sortedLine
                            )
                        )
                        currentLine = ArrayList()
                        currentLine.add(crtBox)
                    }
                    lastBox = crtBox
                }
            }

            if (currentLine.isNotEmpty()) {
                unifiedLines.add(
                    Line(
                        currentLine
                    )
                )
            }

            return RawReceipt(unifiedLines)
        }
    }
}

private const val MERCHANT_MIN_LENGTH = 2

fun extractMerchant(rawReceipt: RawReceipt): String? {
    val linesIterator = rawReceipt.iterator()
    while (linesIterator.hasNext()) {
        val line = linesIterator.next()
        if (line.text.length < MERCHANT_MIN_LENGTH) continue

        val nextLine = if (linesIterator.hasNext()) linesIterator.next() else null

        val heightThreshold = 1.2 * rawReceipt.averageLineHeight

        return if (
            line.height > heightThreshold &&
            nextLine != null &&
            nextLine.text.split(" ").size < 2 &&
            nextLine.height > heightThreshold &&
            nextLine.top - line.bottom < rawReceipt.averageLineHeight
        )
            line.text + " " + nextLine.text
        else
            line.text
    }
    return null
}

fun extractDate(receiptText: String): Date =
    findDatesWithPatterns(receiptText).firstOrNull() ?: Date()

fun parseNumber(string: String): Float? =
    Regex("[+-]?([0-9]*[.,])[0-9]+")
        .findAll(string.removeSpaceInFloat())
        .map { it.value.replace(',', '.') }
        .mapNotNull { it.toFloatOrNull() }
        .sortedDescending()
        .firstOrNull()

private val spaceBefore = "(\\d)\\s([.,])".toRegex()
private val spaceAfter = "([.,])\\s(\\d)".toRegex()

private fun String.removeSpaceInFloat(): String = this
    .replace(spaceBefore, "$1$2")
    .replace(spaceAfter, "$1$2")


class ProductsAndTotalStrategy(private val receipt: RawReceipt) {
    private val horizontalBorders: HorizontalBorders

    private var lastKey: Optional<OcrElement> = None

    private val totalMarkRegex = "total|ammount|summe".toRegex()

    private val keyPriceResults = mutableListOf<ResultObj>()

    init {
        horizontalBorders = boundaries(receipt)
    }

    fun execute(): Pair<Float?, List<Product>> {
        walkAndProcess()
        return makeResult()
    }

    private fun makeResult(): Pair<Float?, List<Product>> {
        val price = keyPriceResults
            .mapNotNull {
                when (it) {
                    is ResultObj.Total -> it
                    else -> null
                }
            }
            .sortedWith(compareBy({ -it.price }, { it.top }))
            .firstOrNull()

        val products = keyPriceResults
            .mapNotNull {
                when (it) {
                    is ResultObj.Product -> it
                    else -> null
                }
            }
            .filter { if (price != null) it.top < price.top else true }
            .map { Product(it.name, it.price) }
        return price?.price to products
    }

    private fun walkAndProcess() {
        for (line in receipt) {
            for (element in line) {
                val left = isAlignedToLeft(element)
                val right = isAlignedToRight(element)
                if (left && right) {
                    processKeyValue(element)
                } else if (left) {
                    processKey(element)
                } else if (right) {
                    processPrice(element)
                }
            }
        }
    }

    private fun processPrice(priceElement: OcrElement) {
        val price = parseNumber(priceElement.text)
        price?.let {
            val mLastKey = lastKey
            if (mLastKey is Just) {
                val keyElement = mLastKey.value
                if (priceElement.top - keyElement.bottom < 0.5 * priceElement.height) {
                    makeResult(keyElement, it)
                } else {
                    lastKey = None
                }
            }
        }
    }

    private fun processKey(element: OcrElement) {
        val digitCount = element.text.count { it.isDigit() }
        if (digitCount < 0.3 * element.text.length) {
            lastKey = Just(element)
        }
    }

    private fun processKeyValue(element: OcrElement) {
        val price = parseNumber(element.text)
        price?.let {
            val name = element.text.split(" ").take(3).joinToString(" ")
            if (name.length > 1) {
                makeResult(name, price, element)
            }
        }
    }

    private fun makeResult(key: String, price: Float, element: OcrElement) {
        val keyLowercase = element.text.toLowerCase()
        if (keyLowercase.contains(totalMarkRegex)) {
            keyPriceResults.add(
                ResultObj.Total(
                    price,
                    element.top
                )
            )
        } else {
            keyPriceResults.add(
                ResultObj.Product(
                    price,
                    key,
                    element.top
                )
            )
        }
    }

    private fun makeResult(keyElement: OcrElement, price: Float) {
        val keyLowercase = keyElement.text.toLowerCase()
        if (keyLowercase.contains(totalMarkRegex)) {
            keyPriceResults.add(
                ResultObj.Total(
                    price,
                    keyElement.top
                )
            )
        } else {
            keyPriceResults.add(
                ResultObj.Product(
                    price,
                    keyElement.text,
                    keyElement.top
                )
            )
        }
    }

    private fun isAlignedToLeft(element: OcrElement): Boolean =
        (element.left - horizontalBorders.left).toFloat() / horizontalBorders.width < ALIGN_THRESH

    private fun isAlignedToRight(element: OcrElement): Boolean =
        (horizontalBorders.right - element.right).toFloat() / horizontalBorders.width < ALIGN_THRESH

    private fun boundaries(receipt: RawReceipt): HorizontalBorders {
        val elements = receipt.flatten()
        var top = Int.MAX_VALUE
        var bottom = -1
        var left = Int.MAX_VALUE
        var right = -1
        for (a in elements) {
            if (a.top < top) top = a.top
            if (a.left < left) left = a.left
            if (a.bottom > bottom) bottom = a.bottom
            if (a.right > right) right = a.right
        }
        return HorizontalBorders(
            left,
            right
        )
    }

    private class HorizontalBorders(val left: Int, val right: Int)
    private val HorizontalBorders.width
        get() = this.right - this.left + 1

    private sealed class ResultObj {
        class Total(val price: Float, val top: Int) : ResultObj()
        class Product(val price: Float, name: String, val top: Int) : ResultObj() { val name = name.toUpperCase() }
    }

    companion object {
        private const val ALIGN_THRESH = 0.1
    }
}

private const val DIGIT_MISTAKES = "[\\doO]"

val formats = mapOf(
    "$DIGIT_MISTAKES{8}" to "yyyyMMdd",
    "$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{4}" to "dd/MM/yyyy",
    "$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{4}" to "dd-MM-yyyy",
    "$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{4}" to "dd.MM.yyyy",
    "$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}(?!\\d)" to "dd/MM/yy",
    "$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}(?!\\d)" to "dd-MM-yy",
    "$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}(?!\\d)" to "dd.MM.yy",
    "$DIGIT_MISTAKES{4}/$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}" to "yyyy/MM/dd",
    "$DIGIT_MISTAKES{4}-$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}" to "yyyy-MM-dd",
    "$DIGIT_MISTAKES{4}\\.$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}" to "yyyy.MM.dd"
)

fun findDatesWithPatterns(searchedString: String): Sequence<Date> {
    var results = sequenceOf<Pair<String, String>>()
    val fakeZeros = "Oo".toRegex()
    for ((regex, format) in formats) {
        val result = regex.toRegex().findAll(searchedString)
        val seq = result.map { it.value.replace(fakeZeros, "0") to format }
        results += seq
    }

    val now = Date()

    return results
        .mapNotNull {
            try {
                SimpleDateFormat(it.second, Locale.US).parse(it.first)
            } catch (e: ParseException) {
                null
            }
        }
        .sortedBy { abs(it.time - now.time) }
}