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
 * Fragment gérant l'affichage de la liste des sessions de sommeil.
 *
 * This fragment uses View Binding to access the layout views,
 * and observes the [SleepViewModel] to get the latest sleep data.
 * It updates a RecyclerView using [SleepAdapter] to show the list of sleeps.
 */
@AndroidEntryPoint
class SleepFragment : Fragment() {

    /** Binding object instance for this fragment's layout. */
    private var _binding: FragmentSleepBinding? = null

    /** Non-nullable binding getter for use after view creation. */
    private val binding get() = _binding!!

    /** ViewModel scoped to this fragment, responsible for sleep data. */
    private val viewModel: SleepViewModel by viewModels()

    /** Adapter managing the sleep list in the RecyclerView. */
    private val sleepAdapter = SleepAdapter(emptyList())

    /**
     * Inflates the layout and initializes view binding.
     *
     * @param inflater The LayoutInflater to inflate the fragment's UI
     * @param container The parent view the fragment UI should be attached to
     * @param savedInstanceState Bundle with saved state if any
     * @return Root view of the inflated layout
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after onCreateView. Sets up observers and configures RecyclerView.
     * Initiates the fetching of sleep data.
     *
     * @param view The created view
     * @param savedInstanceState Bundle with saved state if any
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.sleepRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.sleepRecyclerview.adapter = sleepAdapter

        lifecycleScope.launch {
            viewModel.fetchSleeps()
        }
    }

    /**
     * Starts collecting the sleep data flow from the ViewModel
     * and updates the adapter with the latest data.
     */
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sleeps.collect { sleeps ->
                sleepAdapter.updateData(sleeps)
            }
        }
    }

    /**
     * Cleans up the binding object when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
