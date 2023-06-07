package com.ab.githubtrackerapplication.gitRepo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ab.githubtrackerapplication.R
import com.ab.githubtrackerapplication.custom.LottieDialogFragment
import com.ab.githubtrackerapplication.databinding.FragmentAddRepoBinding
import com.ab.githubtrackerapplication.model.GitRepositoryDetail
import com.ab.githubtrackerapplication.util.Constants
import com.ab.githubtrackerapplication.util.Utils
import com.ab.githubtrackerapplication.util.Utils.formatDate
import com.ab.githubtrackerapplication.util.Utils.showView
import com.ab.githubtrackerapplication.util.Utils.showVisibility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.textChanges
import rx.Observable
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
    private lateinit var repositoryDetail: GitRepositoryDetail

    private val snackBar : (Snackbar) -> Unit ={ snack ->
        snackBarOpened = snack
    }

    private fun dismissPreviousSnackBar(){
        snackBarOpened?.dismiss()
    }


    private fun bind(){

        Observable.merge(binding.repoOwnerName.textChanges(),binding.repositoryName.textChanges())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                dismissPreviousSnackBar()
                binding.addRepositoryButton.text = getString(R.string.fetch_repository) // reset tp fetch repo when input value changed
            }

        binding.addRepositoryButton.clicks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                validateAndAddRepo()
            }

        binding.backButton.clicks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                findNavController().navigate(R.id.action_AddRepoFragment_to_LandingFragment)
            }
    }

    private fun validateAndAddRepo() {
        when {
            binding.addRepositoryButton.text.toString() == getString(R.string.add_repository) -> addRepoAndMoveToHomeFragment()
            !Utils.checkInternetConnection() -> Utils.snackBarListener(binding.addRepositoryButton,getString(R.string.device_offline),snackBar)
            binding.repoOwnerName.text.toString().isBlank() -> Utils.snackBarListener(binding.addRepositoryButton,getString(R.string.enter_owner_name),snackBar)
            binding.repositoryName.text.toString().isBlank() -> Utils.snackBarListener(binding.addRepositoryButton,getString(R.string.enter_repo_name),snackBar)
            else -> startDialogAndFetchRepository()
        }
    }

    private fun addRepoAndMoveToHomeFragment(){
        viewModel.addRepoDetail(repositoryDetail) {
                findNavController().navigate(R.id.action_AddRepoFragment_to_LandingFragment)
        }
    }


    private fun startDialogAndFetchRepository(){
        lottieDialog = LottieDialogFragment.newInstance(R.raw.search)
        lottieDialog.isCancelable = false
        lottieDialog.show(requireFragmentManager(), "lottie_dialog")
        viewModel.fetchRepositoryDetail(binding.repoOwnerName.text.toString().trim(),binding.repositoryName.text.toString().trim())
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
            updateFooterButton()
        }

        viewModel.errorDetail.observe(viewLifecycleOwner) {
            lottieDialog.dismiss()
            showRepoCardDetail(false)
        }
    }

    private fun updateFooterButton() {
        binding.addRepositoryButton.text = getString(R.string.add_repository)
    }

    private fun showRepoCardDetail(show : Boolean){
        binding.repoInfoCard.showVisibility(show)
        binding.repoNotFoundLayout.showVisibility(!show)
    }

    private fun bindRepoCardDetail(repoDetail : GitRepositoryDetail){
        repositoryDetail = repoDetail
        binding.cardRepositoryName.text = repoDetail.name
        binding.cardRepoDescriptionText.text = repoDetail.description
        binding.cardRepositoryOwner.text = repoDetail.owner?.login
        binding.createdAt.formatDate(repoDetail.createdAt,Constants.DATE_FORMAT_DDMMYYY)
        Log.d("avatar",repoDetail.owner?.avatarUrl?:"")
        loadAvatarImg(repoDetail.owner?.avatarUrl?:"")

            //handle visibility

        listOf(binding.cardRepoDescriptionTextView,binding.cardRepoDescriptionText).showView(!repoDetail.description.isNullOrBlank())
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