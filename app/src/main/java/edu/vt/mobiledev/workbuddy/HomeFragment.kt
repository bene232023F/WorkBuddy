package edu.vt.mobiledev.workbuddy.ui.home

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.vt.mobiledev.workbuddy.NoFilterAdapter
import edu.vt.mobiledev.workbuddy.R
import edu.vt.mobiledev.workbuddy.databinding.FragmentHomeBinding

/**
 * Fragment displaying the Pomodoro timer UI.
 *
 * Shows a dropdown to pick interval length.
 * Displays the countdown timer.
 * Shows the next task title.
 * Provides Start / Pause / Reset controls.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    // Backing property for view binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Obtain HomeViewModel via factory, scoped to this Fragment
    private val homeVM: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize view binding
        _binding = FragmentHomeBinding.bind(view)

        // Update timer display whenever HomeViewModel emits new formatted text
        homeVM.timerText.observe(viewLifecycleOwner) { formatted ->
            binding.tvTimer.text = formatted
        }

        // Update current-task label when HomeViewModel updates the task name
        homeVM.currentTaskName.observe(viewLifecycleOwner) { name ->
            binding.tvCurrentTask.text =
                getString(R.string.current_task_label, name)
        }

        // Also update to nextTaskName if underlying nextTask changes
        homeVM.nextTaskName.observe(viewLifecycleOwner) { nextName ->
            binding.tvCurrentTask.text =
                getString(R.string.current_task_label, nextName)
        }

        // Prepare the dropdown menu data and adapter (no filtering)
        val items = resources.getStringArray(R.array.pomodoro_lengths)
        val adapter = NoFilterAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )

        // Wire up the AutoCompleteTextView for session length selection
        binding.actvSessionLength.apply {
            threshold = 0                        // show dropdown immediately on touch
            setAdapter(adapter)                 // supply no-filter adapter
            setOnTouchListener { view, event ->
                // When user lifts finger, show full dropdown
                if (event.action == MotionEvent.ACTION_UP) {
                    (view as AutoCompleteTextView).showDropDown()
                }
                false
            }
            setOnItemClickListener { parent, _, position, _ ->
                // Parse selected minutes and inform the ViewModel
                val mins = parent
                    .getItemAtPosition(position)
                    .toString()
                    .toLongOrNull() ?: return@setOnItemClickListener
                homeVM.setPomodoroLength(mins)
            }
        }

        // Hook up control buttons to ViewModel actions
        binding.btnStart.setOnClickListener {
            // Only start if there’s a next unfinished task
            homeVM.nextTask.value?.let { task ->
                homeVM.startTimerForTask(task)
            }
        }
        binding.btnPause.setOnClickListener {
            homeVM.pauseTimer()
        }
        binding.btnReset.setOnClickListener {
            homeVM.resetTimer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear binding reference to avoid memory leaks
        _binding = null
    }
}