package com.openclassrooms.arista.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.openclassrooms.arista.databinding.FragmentUserDataBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.arista.R
import kotlinx.coroutines.launch

/**
 * Fragment that displays user data (name and email) and observes updates from the [UserDataViewModel].
 *
 * This fragment uses View Binding to interact with UI components and Kotlin Coroutines to collect user data asynchronously.
 */
@AndroidEntryPoint
class UserDataFragment : Fragment() {

    /** View binding instance for this fragment's layout. */
    private lateinit var binding: FragmentUserDataBinding

    /** ViewModel scoped to this fragment, providing user data. */
    private val viewModel: UserDataViewModel by viewModels()

    /**
     * Inflates the layout for this fragment and initializes view binding.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The root view of the inflated layout
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView().
     * Starts collecting user data from the ViewModel and updates UI fields accordingly.
     *
     * @param view The View returned by onCreateView()
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUser()
    }

    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.user?.let { user ->
                    binding.etName.setText(user.name)
                    binding.etEmail.setText(user.email)
                }
                if (uiState.isUserReady == false)
                    Toast.makeText(context, R.string.user_not_ready, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

