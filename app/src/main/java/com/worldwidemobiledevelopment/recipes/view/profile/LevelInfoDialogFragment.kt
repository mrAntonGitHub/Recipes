package com.worldwidemobiledevelopment.recipes.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.dialog_user_level_info.*
import kotlinx.android.synthetic.main.dialog_user_level_info.view.*

class LevelInfoDialogFragment(private val currentLevel: String, private val nextLevel: String, private val progress: Int, private val maxProgress: Int) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_round_dialog)
        return inflater.inflate(R.layout.dialog_user_level_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.currentLevel.text = currentLevel
        view.nextLevel.text = nextLevel
        view.userLevel.text = currentLevel

        view.userLevel.text = String.format(resources.getString(R.string.current_level), currentLevel)
        view.progressValue.text = String.format(resources.getString(R.string.progress_value), progress, maxProgress)
        view.userLevelProgress.max = maxProgress
        view.userLevelProgress.progress = progress

        isCancelable = true
        close.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}