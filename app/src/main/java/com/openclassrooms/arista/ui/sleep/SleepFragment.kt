package com.openclassrooms.arista.ui.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.arista.databinding.FragmentSleepBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment displaying the list of sleep sessions.
 * Uses ViewModel and ListAdapter for reactive data updates.
 */
@AndroidEntryPoint
class SleepFragment : Fragment() {

    /** View binding for the layout. */
    private var _binding: FragmentSleepBinding? = null
    private val binding get() = _binding!!

    /** ViewModel scoped to this Fragment. */
    private val viewModel: SleepViewModel by viewModels()

    /** Adapter for the RecyclerView. */
    private val sleepAdapter = SleepAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sleepRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sleepAdapter
        }

        observeSleepData()
    }

    /**
     * Collects sleep data from ViewModel and submits to adapter.
     */
    private fun observeSleepData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sleeps.collect { sleeps ->
                sleepAdapter.submitList(sleeps)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
