package com.openclassrooms.arista.domain.usecase

import android.util.Log
import com.openclassrooms.arista.data.repository.UserRepository
import com.openclassrooms.arista.domain.model.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun execute(): List<User> {
        val test = userRepository.getAllUsers()
        Log.d("MARC", "execute: $test")
        return userRepository.getAllUsers()
    }
}