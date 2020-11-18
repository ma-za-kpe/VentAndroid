package com.makumatthew.vent.ui.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.makumatthew.vent.R

class HistoryFragment : Fragment() {

  private lateinit var dashboardViewModel: HistoryViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    dashboardViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_history, container, false)
    val textView: TextView = root.findViewById(R.id.text_history)
    dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })
    return root
  }
}