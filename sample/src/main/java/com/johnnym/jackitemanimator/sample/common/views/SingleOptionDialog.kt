package com.johnnym.jackitemanimator.sample.common.views

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog

class SingleOptionDialog : DialogFragment() {

    companion object {

        private const val TITLE = "title"
        private const val OPTIONS = "options"
        private const val INITIALLY_SELECTED_SORT_OPTION_POSITION = "initially_selected_sort_option_position"

        fun createInstance(
                title: String,
                sortOptionList: List<String>,
                initiallySelectedSortOptionPosition: Int
        ): SingleOptionDialog {
            val sortOptions: Array<CharSequence> = Array(sortOptionList.size) {
                sortOptionList[it]
            }

            val args = Bundle()
            args.putString(TITLE, title)
            args.putCharSequenceArray(OPTIONS, sortOptions)
            args.putInt(INITIALLY_SELECTED_SORT_OPTION_POSITION, initiallySelectedSortOptionPosition)

            val savedAdsSortPickerDialog = SingleOptionDialog()
            savedAdsSortPickerDialog.arguments = args
            return savedAdsSortPickerDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments!!.getString(TITLE)
        val sortOptions = arguments!!.getCharSequenceArray(OPTIONS)
        val checkedItemPosition = arguments!!.getInt(INITIALLY_SELECTED_SORT_OPTION_POSITION)

        return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setSingleChoiceItems(
                        sortOptions,
                        checkedItemPosition) { dialog, which ->
                    dialog.dismiss()
                    val parentActivity = activity
                    if (parentActivity is OptionSelectedListener) {
                        parentActivity.onSortOptionSelected(which)
                    }
                }
                .create()
    }

    interface OptionSelectedListener {

        fun onSortOptionSelected(selectedSortOptionPosition: Int)
    }
}
