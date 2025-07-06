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

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeVM: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)

        // Observe timer text
        homeVM.timerText.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        // Observe current task name
        homeVM.currentTaskName.observe(viewLifecycleOwner) {
            binding.tvCurrentTask.text =
                getString(R.string.current_task_label, it)
        }

        // Observe next task name
        homeVM.nextTaskName.observe(viewLifecycleOwner) { name ->
            binding.tvCurrentTask.text =
                getString(R.string.current_task_label, name)
        }

        // Prepare the dropdown data + adapter once
        val items = resources.getStringArray(R.array.pomodoro_lengths)
        val adapter = NoFilterAdapter(requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items)

        // Wire up the AutoCompleteTextView
        binding.actvSessionLength.apply {
            threshold = 0
            setAdapter(adapter)
            setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    (v as AutoCompleteTextView).showDropDown()
                }
                false
            }

            // When the user picks a time, update the VM
            setOnItemClickListener { parent, _, pos, _ ->
                val mins = parent.getItemAtPosition(pos).toString().toLongOrNull() ?: return@setOnItemClickListener
                homeVM.setPomodoroLength(mins)
            }
        }

        // Button wiring
        binding.btnStart.setOnClickListener {
            homeVM.nextTask.value?.let { homeVM.startTimerForTask(it) }
        }
        binding.btnPause.setOnClickListener { homeVM.pauseTimer() }
        binding.btnReset.setOnClickListener { homeVM.resetTimer() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
