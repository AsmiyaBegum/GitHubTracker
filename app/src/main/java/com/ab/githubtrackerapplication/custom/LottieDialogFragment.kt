package com.ab.githubtrackerapplication.custom

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ab.githubtrackerapplication.R
import com.ab.githubtrackerapplication.databinding.FetchDataDialogLayouBinding
import com.airbnb.lottie.LottieAnimationView

class LottieDialogFragment : DialogFragment() {

    var lottieJsonName : Int = R.raw.search  // should include the default json if any

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        val binding = FetchDataDialogLayouBinding.inflate(requireActivity().layoutInflater)
        binding.animationView.setAnimation(R.raw.search)

        binding.animationView.playAnimation()
        return builder.create()
    }


    companion object{
        fun newInstance(jsonFileName : Int) : LottieDialogFragment{
            val dialog = LottieDialogFragment()
            dialog.lottieJsonName = jsonFileName
            return dialog
        }
    }
}
