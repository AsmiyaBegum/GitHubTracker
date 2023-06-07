package com.ab.githubtrackerapplication.gitRepo

import android.content.Intent
import android.database.Observable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.githubtrackerapplication.MainActivity
import com.ab.githubtrackerapplication.R
import com.ab.githubtrackerapplication.databinding.FragmentGitRepoLandingBinding
import com.ab.githubtrackerapplication.model.GitRepositoryDetail
import com.ab.githubtrackerapplication.util.AdapterUtils
import com.ab.githubtrackerapplication.util.Utils.showVisibility
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.visibility
import rx.android.schedulers.AndroidSchedulers

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LandingFragment : Fragment(),AdapterUtils.GitRepoListDelegate {

    private var _binding: FragmentGitRepoLandingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: GitRepoViewModel


    private fun bind(){
        binding.addNowButton.clicks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                findNavController().navigate(R.id.action_LandingFragment_to_AddRepoFragment)
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGitRepoLandingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GitRepoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLayout()
        bind()
    }

    private fun bindLayout(){
        binding.repositoryList.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getRepositoryDetailFromDB()
        viewModel.repositoryDetailList.observe(viewLifecycleOwner) { repoDetail ->
            // Update UI with the currency data
        bindRepositoryDetail(repoDetail)
        }
    }

    private fun bindRepositoryDetail(repoDetail : List<GitRepositoryDetail>){
        binding.repositoryEmptyLayout.showVisibility(repoDetail.isEmpty())
        binding.repositoryLayout.showVisibility(repoDetail.isNotEmpty())
        binding.repositoryList.adapter = AdapterUtils.setUpGitReposListAdapter(repoDetail,this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRepoClicked(repoUrl: String) {
        // open webview
        binding.webview.showVisibility(true)
        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl(repoUrl)
    }

    override fun shareRepoDetail(repoDetail: GitRepositoryDetail) {
        // share repo

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.check_out_link))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_repo_detail,repoDetail.name,repoDetail.owner?.login,repoDetail.htmlUrl))
        startActivity(Intent.createChooser(intent, getString(R.string.share_link_via)))
    }
}