package com.lucianbc.receiptscan.presentation.home.exports.form


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.fragment_date_range.*
import java.text.SimpleDateFormat
import java.util.*

class DateRangeFragment
    : BaseFragment<FormViewModel>(FormViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return inflater.inflate(R.layout.fragment_date_range, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCalendar()
    }

    private fun initCalendar() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -1)
        val lastYear = calendar.time
        calendar.add(Calendar.YEAR, 2)
        val nextYear = calendar.time

        fun FormViewModel.range(): List<Date> {
            val first = this.firstDate.value ?: Date()
            val last = this.lastDate.value ?: Date()
            return listOf(first, last)
        }

        rangePicker.init(lastYear, nextYear, SimpleDateFormat("MMMM, YYYY", Locale.getDefault()))
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDates(viewModel.range())

        fun List<Date>.setRange() {
            val (first, last) = this.sorted().let { it.first() to it.last() }
            viewModel.firstDate.value = first
            viewModel.lastDate.value = last
        }

        rangePicker.setOnDateSelectedListener(object: CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date?) =
                rangePicker.selectedDates.setRange()

            override fun onDateUnselected(date: Date?) =
                rangePicker.selectedDates.setRange()
        })
    }
}
