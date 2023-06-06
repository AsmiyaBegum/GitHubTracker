package com.ab.githubtrackerapplication.util

import com.ab.githubtrackerapplication.R
import com.ab.githubtrackerapplication.databinding.RespositoryListRowBinding
import com.ab.githubtrackerapplication.model.GitRespositoryDetail
import com.jakewharton.rxbinding.view.clicks
import rx.android.schedulers.AndroidSchedulers

object AdapterUtils {

    fun setUpGitReposListAdapter(gitRepositoryList : List<GitRespositoryDetail>,delegate: GitRepoListDelegate) : GenericAdapter<GitRespositoryDetail, RespositoryListRowBinding,List<Unit>>{

        val adapter = GenericAdapter(R.layout.respository_list_row,object : GenericAdapterInteraction<GitRespositoryDetail, RespositoryListRowBinding,List<Unit>>(){

            override fun bindingViewHolder(
                binding: RespositoryListRowBinding,
                data: GitRespositoryDetail,
                holder: GenericAdapter.GenericViewHolder<GitRespositoryDetail, RespositoryListRowBinding, List<Unit>>,
                additionalData: List<Unit>?
            ) {
                binding.repoDetail = data

                binding.shareIcon.clicks()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        delegate.shareRepoDetail(data)
                    }
            }

            override fun onClicked(data: GitRespositoryDetail, binding: RespositoryListRowBinding) {
               delegate.onRepoClicked(data.htmlUrl)
            }
        })
        adapter.addItems(gitRepositoryList)

        return adapter
    }

    interface GitRepoListDelegate{
        fun onRepoClicked(repoUrl : String)
        fun shareRepoDetail(repoDetail : GitRespositoryDetail)
    }

}