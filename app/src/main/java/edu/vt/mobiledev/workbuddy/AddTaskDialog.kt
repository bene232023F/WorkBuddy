package edu.vt.mobiledev.workbuddy.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.vt.mobiledev.workbuddy.R
import edu.vt.mobiledev.workbuddy.data.Task
import edu.vt.mobiledev.workbuddy.data.TaskRepository
import edu.vt.mobiledev.workbuddy.databinding.DialogAddTaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Bottom sheet dialog for adding a new Task.
 */
class AddTaskDialog : BottomSheetDialogFragment() {

    // View binding for this dialog’s layout
    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!

    // Lazy initialization of the TaskRepository
    private val repo by lazy { TaskRepository.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the dialog’s view hierarchy via view binding
        _binding = DialogAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Save button click: validate inputs and persist the new task
        binding.btnSave.setOnClickListener {
            val titleText = binding.etTaskTitle.text.toString().trim()
            val dueInput   = binding.etDueTime.text.toString().trim()
            val dueHours   = dueInput.toLongOrNull()

            when {
                // Title must not be empty
                titleText.isEmpty() -> binding.tilTaskTitle.error =
                    getString(R.string.error_empty_title)

                // Due time must parse to a number
                dueHours == null   -> binding.tilDueTime.error =
                    getString(R.string.error_invalid_due)

                else -> {
                    // Compute due timestamp: now + (dueHours × 1 hour)
                    val dueTimestamp = System.currentTimeMillis() + dueHours * 3_600_000L

                    // Insert the new Task on a background thread
                    lifecycleScope.launch(Dispatchers.IO) {
                        repo.insertTask(
                            Task(
                                title   = titleText,
                                dueAt   = dueTimestamp
                            )
                        )
                    }
                    // Close the dialog once insertion is requested
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear binding to avoid memory leaks
        _binding = null
    }
}