package edu.vt.mobiledev.workbuddy.ui.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.vt.mobiledev.workbuddy.R
import edu.vt.mobiledev.workbuddy.databinding.FragmentStatsBinding

class StatsFragment : Fragment(R.layout.fragment_stats) {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private val statsVM: StatsViewModel by viewModels {
        StatsViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentStatsBinding.bind(view)

        // Observe “today” count
        statsVM.dailyPomodoros.observe(viewLifecycleOwner) { count ->
            binding.tvDailyStats.text =
                getString(R.string.daily_pomodoros, count)
        }

        // Observe “week” count
        statsVM.weeklyPomodoros.observe(viewLifecycleOwner) { count ->
            binding.tvWeeklyStats.text =
                getString(R.string.weekly_pomodoros, count)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}