package com.ab.githubtrackerapplication.util

import com.ab.githubtrackerapplication.R
import com.ab.githubtrackerapplication.databinding.RespositoryListRowBinding
import com.ab.githubtrackerapplication.model.GitRepositoryDetail
import com.jakewharton.rxbinding.view.clicks
import rx.android.schedulers.AndroidSchedulers

object AdapterUtils {

    fun setUpGitReposListAdapter(gitRepositoryList : List<GitRepositoryDetail>, delegate: GitRepoListDelegate) : GenericAdapter<GitRepositoryDetail, RespositoryListRowBinding,List<Unit>>{

        val adapter = GenericAdapter(R.layout.respository_list_row,object : GenericAdapterInteraction<GitRepositoryDetail, RespositoryListRowBinding,List<Unit>>(){

            override fun bindingViewHolder(
                binding: RespositoryListRowBinding,
                data: GitRepositoryDetail,
                holder: GenericAdapter.GenericViewHolder<GitRepositoryDetail, RespositoryListRowBinding, List<Unit>>,
                additionalData: List<Unit>?
            ) {
                binding.repoDetail = data

                binding.shareIcon.clicks()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        delegate.shareRepoDetail(data)
                    }
            }

            override fun onClicked(data: GitRepositoryDetail, binding: RespositoryListRowBinding) {
               delegate.onRepoClicked(data.htmlUrl)
            }
        })
        adapter.addItems(gitRepositoryList)

        return adapter
    }

    interface GitRepoListDelegate{
        fun onRepoClicked(repoUrl : String)
        fun shareRepoDetail(repoDetail : GitRepositoryDetail)
    }

}