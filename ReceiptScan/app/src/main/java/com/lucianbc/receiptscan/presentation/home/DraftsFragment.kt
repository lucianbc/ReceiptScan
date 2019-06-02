package com.lucianbc.receiptscan.presentation.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.presentation.DraftItem
import com.lucianbc.receiptscan.presentation.DraftItemClick
import com.lucianbc.receiptscan.presentation.DraftReviewActivity
import com.lucianbc.receiptscan.presentation.DraftsAdapter
import kotlinx.android.synthetic.main.fragment_drafts.*
import java.text.SimpleDateFormat


class DraftsFragment : Fragment() {

    private lateinit var draftsAdapter: DraftsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drafts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        draftsAdapter = DraftsAdapter(goToDraft)
        draftsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = draftsAdapter
        }
        draftsAdapter.submitList(hugeList)
    }

    private val goToDraft: DraftItemClick = {
        val intent = DraftReviewActivity.navIntent(activity!!, it.id)
        startActivity(intent)
    }

    private val smallList = listOf(
        DraftItem(
            1,
            SimpleDateFormat("dd-MM-yy hh:mm").parse("23-05-2019 14:23")
        ),
        DraftItem(2, SimpleDateFormat("dd-MM-yy hh:mm").parse("24-05-2019 17:32"))
    )

    private val hugeList = generateSequence { smallList }
        .flatten()
        .take(55)
        .toList()
}
