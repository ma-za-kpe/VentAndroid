package com.makumatthew.vent.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makumatthew.vent.R
import com.makumatthew.vent.ui.fragment.add.AddFragment
import kotlinx.android.synthetic.main.activity_delete.*

class DeleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        ui()

    }

    private fun ui() {
        add.setOnClickListener {
            openBottomSheetDialog()
        }

        bottomAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }
    }

    private fun openBottomSheetDialog() {
        val addBottomDialogFragment: AddFragment = AddFragment.newInstance()
        addBottomDialogFragment.show(
            supportFragmentManager, "bottom sheet"
        )
    }
}