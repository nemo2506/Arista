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
 * Interface used to allow deletion of an [Exercise] from the UI.
 */
interface DeleteExerciseInterface {
    /**
     * Callback to request deletion of an exercise.
     *
     * @param exercise The exercise to delete.
     */
    fun deleteExercise(exercise: Exercise?)
}

/**
 * Fragment responsible for displaying a list of exercises and allowing the user
 * to add or delete exercises through the UI.
 *
 * It observes the [ExerciseViewModel] for state changes and interacts with
 * the user through dialogs and buttons.
 */
@AndroidEntryPoint
class ExerciseFragment : Fragment(), DeleteExerciseInterface {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var exerciseAdapter: ExerciseAdapter

    /**
     * Inflates the fragment layout.
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
     * Called when the view has been created. Sets up UI components and observers.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        observeExercises()
    }

    /**
     * Initializes the RecyclerView and its adapter.
     */
    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(this)
        binding.exerciseRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.exerciseRecyclerview.adapter = exerciseAdapter
    }

    /**
     * Observes the exercise UI state from the [ExerciseViewModel]
     * and updates the list when changes occur.
     */
    private fun observeExercises() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { flowState ->
                exerciseAdapter.submitList(flowState.exercises)
                if (flowState.isExerciseReady == false)
                    Toast.makeText(requireContext(), R.string.exercice_not_ready, Toast.LENGTH_SHORT).show()
                if (flowState.isExerciseDeleted == false)
                    Toast.makeText(requireContext(), R.string.exercice_not_deleted, Toast.LENGTH_SHORT).show()
                if (flowState.isExerciseAdded == false)
                    Toast.makeText(requireContext(), R.string.exercice_not_added, Toast.LENGTH_SHORT).show()

            }
        }
    }

    /**
     * Sets up the Floating Action Button to show the "Add Exercise" dialog.
     */
    private fun setupFab() {
        binding.fab.setOnClickListener { showAddExerciseDialog() }
    }

    /**
     * Shows a dialog allowing the user to input exercise details and add a new exercise.
     */
    private fun showAddExerciseDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_exercise, null)
        setupDialogViews(dialogView).also {
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle(R.string.add_new_exercise)
                .setPositiveButton(R.string.add) { _, _ -> addExercise(it) }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    /**
     * Binds views from the add exercise dialog layout.
     *
     * @param dialogView The inflated dialog view.
     * @return Triple containing duration EditText, category Spinner, and intensity EditText.
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
     * Validates input and triggers the creation of a new exercise.
     *
     * @param views A triple of duration EditText, category Spinner, and intensity EditText.
     */
    private fun addExercise(views: Triple<EditText, Spinner, EditText>) {
        viewModel.reset()
        val (durationEditText, categorySpinner, intensityEditText) = views

        val durationStr = durationEditText.text.toString().trim()
        val intensityStr = intensityEditText.text.toString().trim()

        val isDurationValid = viewModel.validateDuration(durationStr)
        if (!isDurationValid)
            Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show()

        var isIntensityValid: Boolean = false
        when (viewModel.validateIntensity(intensityStr)) {
            is IntensityValidationResult.Valid -> {
                isIntensityValid = true
            }

            is IntensityValidationResult.Blank -> {
                Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            }

            is IntensityValidationResult.InvalidNumber -> {
                Toast.makeText(
                    context,
                    R.string.invalid_input_please_enter_valid_numbers,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is IntensityValidationResult.OutOfRange -> {
                Toast.makeText(
                    context,
                    R.string.intensity_should_be_between_1_and_10,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (!isDurationValid || !isIntensityValid) return

        viewModel.addExercise(
            startTime = LocalDateTime.now(),
            duration = durationStr.toInt(),
            category = categorySpinner.selectedItem as ExerciseCategory,
            intensity = intensityStr.toInt()
        )
    }

    /**
     * Cleans up the binding when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Called when an exercise deletion is requested from the adapter.
     *
     * @param exercise The exercise to be deleted.
     */
    override fun deleteExercise(exercise: Exercise?) {
        exercise?.let {
            viewModel.deleteExercise(it)
        }
    }
}
