package edu.vt.mobiledev.workbuddy.ui.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.vt.mobiledev.workbuddy.R
import edu.vt.mobiledev.workbuddy.databinding.FragmentStatsBinding

/**
 * Fragment that displays user productivity statistics:
 * Pomodoros completed today
 * Pomodoros completed this week
 * A random motivational quote
 */
class StatsFragment : Fragment(R.layout.fragment_stats) {

    // ViewBinding instance for this fragment's layout.
    // Held in a nullable backing property and clear in onDestroyView
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    // Obtain the StatsViewModel via the factory, providing the application context
    private val statsVM: StatsViewModel by viewModels {
        StatsViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Bind the view for ViewBinding
        _binding = FragmentStatsBinding.bind(view)

        // Observe the LiveData for today's Pomodoro count and update the TextView
        statsVM.dailyPomodoros.observe(viewLifecycleOwner) { count ->
            binding.tvDailyStats.text =
                getString(R.string.daily_pomodoros, count)
        }

        // Observe the LiveData for this week's Pomodoro count and update the TextView
        statsVM.weeklyPomodoros.observe(viewLifecycleOwner) { count ->
            binding.tvWeeklyStats.text =
                getString(R.string.weekly_pomodoros, count)
        }

        // Load all motivational quotes from resources and pick one at random
        val quotes = resources.getStringArray(R.array.motivational_quote)
        val randomQuote = quotes.random()
        binding.tvQuote.text = randomQuote
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference to avoid memory leaks
        _binding = null
    }
}