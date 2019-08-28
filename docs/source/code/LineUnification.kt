data class OcrElement (
    val text: String,
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    val mid: Float
        get() = (bottom + top).toFloat() / 2

    val height: Int
        get() = bottom - top + 1
}

typealias Line = List<OcrElement>

fun firebaseTextToLines(text: FirebaseVisionText) {
    val sorted = text
        .textBlocks
        .flatMap { it.lines }
        .map {
            OcrElement(
                it.text,
                it.boundingBox!!.left,
                it.boundingBox!!.top,
                it.boundingBox!!.right,
                it.boundingBox!!.bottom
            )
        }
        .sortedBy { it.mid }

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

    return unifiedLines
}