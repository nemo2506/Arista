package com.openclassrooms.arista.ui.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.arista.R
import com.openclassrooms.arista.databinding.FragmentExerciseBinding
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * Interface for deleting an Exercise item.
 */
interface DeleteExerciseInterface {
    /**
     * Called to delete the specified exercise.
     * @param exercise The exercise to be deleted, or null.
     */
    fun deleteExercise(exercise: Exercise?)
}

/**
 * Fragment responsible for displaying and managing exercises.
 * Allows viewing a list of exercises, adding new ones, and deleting existing ones.
 */
@AndroidEntryPoint
class ExerciseFragment : Fragment(), DeleteExerciseInterface {

    /** View binding for the fragment layout. */
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    /** ViewModel instance scoped to this fragment. */
    private val viewModel: ExerciseViewModel by viewModels()

    /** Adapter managing the exercise RecyclerView list items. */
    private lateinit var exerciseAdapter: ExerciseAdapter

    /**
     * Inflates the fragment view and initializes view binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the fragment's view is created.
     * Sets up RecyclerView, FloatingActionButton, and LiveData observation.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        observeExercises()
    }

    /**
     * Initializes the RecyclerView with a LinearLayoutManager and adapter.
     */
    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(this)
        binding.exerciseRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.exerciseRecyclerview.adapter = exerciseAdapter
    }

    /**
     * Observes exercises from the ViewModel and submits the list to the adapter.
     */
    private fun observeExercises() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                exerciseAdapter.submitList(it.exercises)
            }
        }
    }

    /**
     * Sets up the FloatingActionButton to open the dialog for adding a new exercise.
     */
    private fun setupFab() {
        binding.fab.setOnClickListener { showAddExerciseDialog() }
    }

    /**
     * Shows a dialog to add a new exercise with inputs for duration, category, and intensity.
     * Validates inputs and invokes ViewModel to add the exercise.
     */
    private fun showAddExerciseDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_exercise, null)
        setupDialogViews(dialogView).also {
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle(R.string.add_new_exercise)
                .setPositiveButton(R.string.add) { _, _ ->
                    lifecycleScope.launch {
                        Log.d("MARC", "showAddExerciseDialog: $it")
                        val (durationEditText, categorySpinner, intensityEditText) = it
                        val durationStr = durationEditText.text.toString().trim()
                        val intensityStr = intensityEditText.text.toString().trim()
                        val category = categorySpinner.selectedItem as ExerciseCategory

                        viewModel.validateDuration(durationStr)
                        viewModel.validateIntensityNotBlank(intensityStr)
                        viewModel.validateIntensityInterval(intensityStr)
                        viewModel.uiState.collectLatest {
                            Log.d("MARC2", "showAddExerciseDialog: $it")
                        }
                        viewModel.add(
                            startTime = LocalDateTime.now(),
                            duration = durationStr.toInt(),
                            category = category,
                            intensity = intensityStr.toInt()
                        )
                    }
                }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    /**
     * Finds and initializes the dialog views for user input.
     * @param dialogView The inflated dialog view.
     * @return A Triple containing references to duration EditText, category Spinner, and intensity EditText.
     */
    private fun setupDialogViews(dialogView: View): Triple<EditText, Spinner, EditText> {
        val durationEditText = dialogView.findViewById<EditText>(R.id.durationEditText)
        val categorySpinner = dialogView.findViewById<Spinner>(R.id.categorySpinner).apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                ExerciseCategory.entries.toTypedArray()
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
        val intensityEditText = dialogView.findViewById<EditText>(R.id.intensityEditText)
        return Triple(durationEditText, categorySpinner, intensityEditText)
    }

    /**
     * Cleans up the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Implements [DeleteExerciseInterface]. Deletes the specified exercise by invoking ViewModel.
     * @param exercise The exercise to delete, if not null.
     */
    override fun deleteExercise(exercise: Exercise?) {
        exercise?.let {
            lifecycleScope.launch {
                viewModel.deleteExercise(it)
            }
        }
    }
}
