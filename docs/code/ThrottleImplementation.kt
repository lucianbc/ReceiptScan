fun <T> throttled(
    disposable: CompositeDisposable,
    timeout: Long,
    unit: TimeUnit,
    func: ((T) -> Unit)
): ((T) -> Unit) {
    val subject = PublishSubject.create<T>()

    subject
        .throttleLast(timeout, unit)
        .subscribe(func)
        .addTo(disposable)

    return { t -> subject.onNext(t) }
}

val updateMerchant =
    throttled<String>(disposables, TIMEOUT, TIME_UNIT) {
        useCase.update(it) { v, dwp -> dwp.copy(merchantName = v) }
            .subscribe()
    }