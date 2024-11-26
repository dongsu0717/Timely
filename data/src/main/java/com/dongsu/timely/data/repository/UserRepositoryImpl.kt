package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.local.UserLocalDatasource
import com.dongsu.timely.data.datasource.remote.UserRemoteDatasource
import com.dongsu.timely.domain.model.Token
import com.dongsu.timely.domain.model.map.User
import com.dongsu.timely.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDatasource: UserLocalDatasource,
    private val userRemoteDatasource: UserRemoteDatasource,
) : UserRepository {
    override suspend fun sendKaKaoTokenAndGetToken(token: String): TimelyResult<Token> =
        try {
            val response = userRemoteDatasource.sendKaKaoTokenAndGetToken(token)
            TimelyResult.Success(response)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun saveTokenLocal(accessToken: String, refreshToken: String) =
        userLocalDatasource.saveLoginStatus(accessToken, refreshToken)

    override suspend fun sendFCMToken(token: String) = userRemoteDatasource.sendFCMToken(token)

    override suspend fun saveLoginStatus(accessToken: String, refreshToken: String) =
        userLocalDatasource.saveLoginStatus(accessToken, refreshToken)

    override suspend fun isLoggedIn(): TimelyResult<Boolean> = userLocalDatasource.isLoggedIn()

    override suspend fun fetchMyInfo(): TimelyResult<User> =
        try {
            when (val myInfo = userRemoteDatasource.fetchMyInfo()) {
                User.EMPTY -> TimelyResult.Empty
                else -> TimelyResult.Success(myInfo)
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun countLateness() = userRemoteDatasource.countLateness()


    override suspend fun getUserProfile() {
    }

    override suspend fun updateUserProfile() {
    }

    override suspend fun kakaoLogout() {
    }

    override suspend fun deleteAccount() {
    }
}