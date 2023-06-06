package com.ab.githubtrackerapplication.gitRepo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ab.githubtrackerapplication.R
import com.ab.githubtrackerapplication.custom.LottieDialogFragment
import com.ab.githubtrackerapplication.databinding.FragmentAddRepoBinding
import com.ab.githubtrackerapplication.model.GitRespositoryDetail
import com.ab.githubtrackerapplication.util.Constants
import com.ab.githubtrackerapplication.util.Utils
import com.ab.githubtrackerapplication.util.Utils.formatDate
import com.ab.githubtrackerapplication.util.Utils.showVisibility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.clicks
import rx.android.schedulers.AndroidSchedulers

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddRepoFragment : Fragment() {

    private var _binding: FragmentAddRepoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: GitRepoViewModel

    private lateinit var lottieDialog : LottieDialogFragment

    private var snackBarOpened : Snackbar? = null

    private val snackBar : (Snackbar) -> Unit ={ snack ->
        snackBarOpened = snack
    }

    private fun dismissPreviousSnackBar(){
        snackBarOpened?.dismiss()
    }


    private fun bind(){

        binding.addRepositoryButton.clicks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(Utils.checkInternetConnection()){
                    dismissPreviousSnackBar()
                    startDialogAndFetchRepository()
                }else{
                    Utils.snackBarListener(binding.addRepositoryButton,getString(R.string.device_offline),snackBar)
                }
            }
    }


    private fun startDialogAndFetchRepository(){
        lottieDialog = LottieDialogFragment.newInstance(R.raw.search)
        lottieDialog.isCancelable = false
        lottieDialog.show(requireFragmentManager(), "lottie_dialog")

        viewModel.fetchRepositoryDetail(binding.repoOwnerName.text.toString(),binding.cardRepositoryName.text.toString())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[GitRepoViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddRepoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        bindLayout()
    }

    private fun bindLayout(){
        viewModel.repositoryDetail.observe(viewLifecycleOwner) { repoDetail ->
            // Update UI with the data
            lottieDialog.dismiss()
            showRepoCardDetail(true)
            bindRepoCardDetail(repoDetail)
        }

        viewModel.errorDetail.observe(viewLifecycleOwner) {
            showRepoCardDetail(false)
        }
    }

    private fun showRepoCardDetail(show : Boolean){
        binding.repoInfoCard.showVisibility(show)
        binding.repoNotFoundLayout.showVisibility(!show)
    }

    private fun bindRepoCardDetail(repoDetail : GitRespositoryDetail){

        binding.cardRepositoryName.text = repoDetail.name
        binding.cardRepoDescriptionText.text = repoDetail.description
        binding.cardRepositoryOwner.text = repoDetail.owner?.login
        binding.createdAt.formatDate(repoDetail.createdAt,Constants.DATE_FORMAT_DDMMYYY)
        loadAvatarImg(repoDetail.htmlUrl)
    }

    private fun loadAvatarImg(avatarUrl : String){
        Glide.with(this)
            .load(avatarUrl)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.ic_account_circle) // Placeholder image while loading
                .error(R.drawable.ic_account_circle) // Error image if loading fails
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Caching strategy
            )
            .into(binding.repoAvatarIcon)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}