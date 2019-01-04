package com.johnnym.jackitemanimator.sample.taxilist.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.johnnym.jackitemanimator.sample.R

class TaxiListSortOptionsDialog : DialogFragment() {

    companion object {

        private const val SORT_OPTIONS = "sort_options"
        private const val INITIALLY_SELECTED_SORT_OPTION_POSITION = "initially_selected_sort_option_position"

        fun createInstance(
                sortOptionList: List<String>,
                initiallySelectedSortOptionPosition: Int
        ): TaxiListSortOptionsDialog {
            val sortOptions: Array<CharSequence> = Array(sortOptionList.size) {
                sortOptionList[it]
            }

            val args = Bundle()
            args.putCharSequenceArray(SORT_OPTIONS, sortOptions)
            args.putInt(INITIALLY_SELECTED_SORT_OPTION_POSITION, initiallySelectedSortOptionPosition)

            val savedAdsSortPickerDialog = TaxiListSortOptionsDialog()
            savedAdsSortPickerDialog.arguments = args
            return savedAdsSortPickerDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sortOptions = arguments!!.getCharSequenceArray(SORT_OPTIONS)
        val checkedItemPosition = arguments!!.getInt(INITIALLY_SELECTED_SORT_OPTION_POSITION)

        return AlertDialog.Builder(requireContext())
                .setTitle(R.string.taxi_list_sort_options_dialog_title)
                .setSingleChoiceItems(
                        sortOptions,
                        checkedItemPosition) { dialog, which ->
                    dialog.dismiss()
                    val parentActivity = activity
                    if (parentActivity is SortOptionSelectedListener) {
                        parentActivity.onSortOptionSelected(which)
                    }
                }
                .create()
    }

    interface SortOptionSelectedListener {

        fun onSortOptionSelected(selectedSortOptionPosition: Int)
    }
}
