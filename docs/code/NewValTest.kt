@Test
    fun `when fetching new currency, values are emmited`() {
        val repo: ReceiptsRepository = mock {
            on { getAvailableCurrencies() } doReturn Flowable.just(currencies).subscribeOn(Schedulers.io())
        }
        val subject = SourcesManager(repo)
        subject.availableCurrencies.test()
        val currencyObs = subject.currentCurrency.test()

        subject.fetchForCurrency(otherCurrency)

        currencyObs.awaitCount(2).assertValues(currencies[0], otherCurrency)

        subject.fetchForCurrency(otherCurrency)

        currencyObs.awaitCount(1).assertValues(currencies[0], otherCurrency, otherCurrency)
    }