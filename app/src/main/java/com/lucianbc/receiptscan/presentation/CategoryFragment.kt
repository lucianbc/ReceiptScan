package com.lucianbc.receiptscan.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Category
import kotlinx.android.synthetic.main.category_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment(
    private val callback: (Category) -> Unit
) : Fragment() {

    private lateinit var _adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _adapter = CategoryAdapter {
            callback.invoke(it)
            fragmentManager?.popBackStack()
        }
        categoriesList.apply {
            adapter = _adapter
            layoutManager = GridLayoutManager(activity, 2)
        }
        (categoryToolbar.menu.getItem(0).actionView as SearchView)
            .apply(::initSearch)
        categoryToolbar.apply {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun initSearch(view: SearchView) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                _adapter.filter.filter(newText)
                 return false
            }
        })
    }

    companion object {
        const val TAG = "CATEGORIES"
    }
}

private class CategoryAdapter(
    private val callback: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.Holder>(Diff()), Filterable {

    private var allItems = emptyList<Category>()

    init {
        submitList(Category.values().asList())
    }

    override fun submitList(list: List<Category>?) {
        list?.let { allItems = list }
        super.submitList(list)
    }

    private fun submitWithoutSave(list: List<Category>?) = super.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.category_item_layout, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val element = getItem(position)
        holder.item = element
    }

    override fun getFilter() = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply { values =
                filterCategories(constraint, allItems)
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitWithoutSave(results?.values as List<Category>?)
        }
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item : Category? = null
            set(value) {
                field = value
                value?.let {
                    view.setOnClickListener {
                        value.let(callback)
                    }
                    view.categoryName.text = value.displayName
                    view.categoryIc.setImageResource(value.icon)
                }
            }
    }

    class Diff : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }
    }
}

private fun filterCategories(constraint: CharSequence?, items: List<Category>): List<Category> {
    return if (constraint == null || constraint.isEmpty())
            items
    else {
        val const = constraint.toString().toLowerCase().trim()
        items.filter { category -> category.name.toLowerCase().contains(const) }
    }
}