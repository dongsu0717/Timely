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

    override suspend fun saveTokenLocal(accessToken: String, refreshToken: String): TimelyResult<Unit> =
        try {
            userLocalDatasource.saveTokenLocal(accessToken, refreshToken)
            TimelyResult.Success(Unit)
        } catch (e: Exception) {
            TimelyResult.LocalError(e)
        }

    override suspend fun sendFCMToken(token: String): TimelyResult<Unit> =
        try {
            userRemoteDatasource.sendFCMToken(token)
            TimelyResult.Success(Unit)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun isLoggedIn(): TimelyResult<Boolean> =
        try {
            val isLoggedIn = userLocalDatasource.isLoggedIn()
            TimelyResult.Success(isLoggedIn)
        } catch (e: Exception) {
            TimelyResult.LocalError(e)
        }

    override suspend fun fetchMyInfo(): TimelyResult<User> =
        try {
            when (val myInfo = userRemoteDatasource.fetchMyInfo()) {
                User.EMPTY -> TimelyResult.Empty
                else -> TimelyResult.Success(myInfo)
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun countLateness(isLate: Boolean): TimelyResult<Unit> =
        try{
            userRemoteDatasource.countLateness(isLate)
            TimelyResult.Success(Unit)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }


    override suspend fun getUserProfile() {
    }

    override suspend fun updateUserProfile() {
    }

    override suspend fun kakaoLogout() {
    }

    override suspend fun deleteAccount() {
    }
}