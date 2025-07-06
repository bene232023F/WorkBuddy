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

class AddTaskDialog : BottomSheetDialogFragment() {
    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!
    private val repo by lazy { TaskRepository.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val title = binding.etTaskTitle.text.toString().trim()
            val dueInput = binding.etDueTime.text.toString().trim()
            val dueHours = dueInput.toLongOrNull()

            when {
                title.isEmpty() -> binding.tilTaskTitle.error =
                    getString(R.string.error_empty_title)

                dueHours == null -> binding.tilDueTime.error =
                    getString(R.string.error_invalid_due)

                else -> {
                    // calculate due timestamp (now + minutes)
                    val dueTimestamp = System.currentTimeMillis() + dueHours * 3_600_000L
                    // insert on IO thread
                    lifecycleScope.launch(Dispatchers.IO) {
                        repo.insertTask(Task(title = title, dueAt = dueTimestamp))
                    }
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}