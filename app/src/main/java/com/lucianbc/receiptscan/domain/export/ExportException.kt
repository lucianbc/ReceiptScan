package com.lucianbc.receiptscan.domain.export

class ExportException(val error: Cause) : IllegalStateException(error.name) {
    enum class Cause {
        BAD_RANGE,
        BAD_FORMAT,
        BAD_CONTENT;

        operator fun invoke() = ExportException(this)
    }
}