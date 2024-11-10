package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.UserLocalDatasource
import com.dongsu.timely.data.datasource.UserRemoteDatasource
import com.dongsu.timely.domain.model.User
import com.dongsu.timely.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDatasource: UserLocalDatasource,
    private val userRemoteDatasource: UserRemoteDatasource
): UserRepository {
    override suspend fun sendToken(user: User)
    = userRemoteDatasource.sendToken(user)

    override suspend fun saveStatus(accessToken: String, refreshToken: String)
    = userLocalDatasource.saveLoginStatus(accessToken,refreshToken)

    override suspend fun isLoggedIn():TimelyResult<Boolean>
    = userLocalDatasource.isLoggedIn()

    override suspend fun getUserProfile() {
    }

    override suspend fun updateUserProfile() {
    }

    override suspend fun kakaoLogout() {
    }

    override suspend fun deleteAccount() {
    }
}