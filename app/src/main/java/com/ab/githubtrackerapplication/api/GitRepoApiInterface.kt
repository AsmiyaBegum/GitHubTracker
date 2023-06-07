package com.ab.githubtrackerapplication.api

import com.ab.githubtrackerapplication.model.GitRepositoryDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface GitRepoApiInterface {

    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryDetail(@Path("owner") owner: String,@Path("repo") repo : String): GitRepositoryDetail

}