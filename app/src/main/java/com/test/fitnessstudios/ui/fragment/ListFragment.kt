package com.test.fitnessstudios.ui.fragment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.fitnessstudios.R
import com.test.fitnessstudios.constants.IntentKeys
import com.test.fitnessstudios.data.Business
import com.test.fitnessstudios.databinding.FragmentListBinding
import com.test.fitnessstudios.ui.activity.DetailActivity
import com.test.fitnessstudios.ui.adapter.BusinessListAdapter
import com.test.fitnessstudios.viewmodel.HomeViewModel

class ListFragment : Fragment(), BusinessListAdapter.OnBusinessSelectedListener {

    private lateinit var mBinding: FragmentListBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var location: Location? = null
    private var businessList: List<Business> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location = homeViewModel.locationFlow.value
        businessList = homeViewModel.businessListFlow.value
        mBinding.showEmptyView = businessList.isEmpty()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvAdapter = BusinessListAdapter(this)
        rvAdapter.differ.submitList(businessList)
        val linearlayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerView.apply {
            layoutManager = linearlayoutManager
            adapter = rvAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    linearlayoutManager.orientation
                )
            )
        }
    }

    override fun onBusinessSelected(business: Business) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(IntentKeys.business, business)
        intent.putExtra(IntentKeys.location, location as Location)
        requireContext().startActivity(intent)
    }
}