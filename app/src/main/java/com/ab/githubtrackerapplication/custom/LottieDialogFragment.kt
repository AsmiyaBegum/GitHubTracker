package com.ab.githubtrackerapplication.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ab.githubtrackerapplication.R
import com.ab.githubtrackerapplication.databinding.FetchDataDialogLayouBinding
import com.airbnb.lottie.LottieDrawable


class LottieDialogFragment : DialogFragment() {

    private var _binding: FetchDataDialogLayouBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var lottieJsonName : Int = R.raw.search  // should include the default json if any
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FetchDataDialogLayouBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.animationView.setAnimation(lottieJsonName)
        binding.animationView.playAnimation()
        binding.animationView.repeatCount = LottieDrawable.INFINITE
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object{
        fun newInstance(jsonFileName : Int) : LottieDialogFragment{
            val dialog = LottieDialogFragment()
            dialog.lottieJsonName = jsonFileName
            return dialog
        }
    }
}
