package com.openclassrooms.arista.ui.exercise

import android.os.Bundle
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
            viewModel.exercisesFlow.collect { exercises ->
                exerciseAdapter.submitList(exercises)
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
                        addExercise(it)
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
     * Collects user input from dialog views, validates, and adds the new exercise via the ViewModel.
     * @param views The Triple of input views: duration EditText, category Spinner, intensity EditText.
     */
    private suspend fun addExercise(views: Triple<EditText, Spinner, EditText>) {
        val (durationEditText, categorySpinner, intensityEditText) = views

        val durationStr = durationEditText.text.toString().trim()
        val intensityStr = intensityEditText.text.toString().trim()

        val isDurationValid = validateDuration(durationStr)
        val isIntensityValid = validateIntensity(intensityStr)

        if (!isDurationValid || !isIntensityValid) return

        val duration = durationStr.toInt()
        val intensity = intensityStr.toInt()
        val category = categorySpinner.selectedItem as ExerciseCategory

        viewModel.add(listOf(LocalDateTime.now(), duration, category, intensity))
    }

    /**
     * Validates that the duration input is not blank.
     * Shows a Toast message if invalid.
     * @param duration The duration input as String.
     * @return true if valid, false otherwise.
     */
    private fun validateDuration(duration: String): Boolean {
        if (duration.isBlank()) {
            Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /**
     * Validates the intensity input is a number between 1 and 10.
     * Shows appropriate Toast messages if invalid.
     * @param intensity The intensity input as String.
     * @return true if valid, false otherwise.
     */
    private fun validateIntensity(intensity: String): Boolean {
        if (intensity.isBlank()) {
            Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return false
        }

        return try {
            val intensityValue = intensity.toInt()
            if (intensityValue !in 1..10) {
                Toast.makeText(
                    requireContext(),
                    R.string.intensity_should_be_between_1_and_10,
                    Toast.LENGTH_SHORT
                ).show()
                false
            } else {
                true
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(
                requireContext(),
                R.string.invalid_input_please_enter_valid_numbers,
                Toast.LENGTH_SHORT
            ).show()
            false
        }
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
