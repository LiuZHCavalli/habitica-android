package com.habitrpg.android.habitica.ui.fragments.preferences

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View
import android.widget.TimePicker
import com.habitrpg.android.habitica.prefs.TimePreference
import java.util.*

class TimePreferenceDialogFragment : PreferenceDialogFragmentCompat() {

    lateinit var picker: TimePicker

    private val timePreference: TimePreference
        get() = preference as TimePreference

    private val newTimeValue: String
        get() {
            val lastHour: Int
            val lastMinute: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lastHour = picker.hour
                lastMinute = picker.minute
            } else {
                lastHour = picker.currentHour
                lastMinute = picker.currentMinute
            }
            return lastHour.toString() + ":" + String.format(Locale.UK, "%02d", lastMinute)
        }

    override fun onCreateDialogView(context: Context?): View {
        picker = TimePicker(getContext())
        return picker
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val preference = timePreference
        val lastHour = preference.lastHour
        val lastMinute = preference.lastMinute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.hour = lastHour
            picker.minute = lastMinute
        } else {
            picker.currentHour = lastHour
            picker.currentMinute = lastMinute
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val preference = timePreference
            val time = newTimeValue

            preference.summary = time

            if (preference.callChangeListener(time)) {
                preference.text = time
            }
        }

    }

    companion object {

        val TAG = TimePreferenceDialogFragment::class.java.simpleName

        fun newInstance(
                preferenceFragment: PreferenceFragmentCompat, key: String): TimePreferenceDialogFragment {
            val fragment = TimePreferenceDialogFragment()
            val arguments = Bundle(1)
            arguments.putString(PreferenceDialogFragmentCompat.ARG_KEY, key)
            fragment.arguments = arguments
            fragment.setTargetFragment(preferenceFragment, 0)
            return fragment
        }
    }
}

