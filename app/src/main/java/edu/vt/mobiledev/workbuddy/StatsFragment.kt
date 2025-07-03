package edu.vt.mobiledev.workbuddy.ui.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.vt.mobiledev.workbuddy.R

class StatsFragment : Fragment(R.layout.fragment_stats) {
    private val statsVM: StatsViewModel by viewModels {
        StatsViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Load and display daily/weekly stats, motivational quotes
    }
}