package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.FakeApiService
import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.domain.model.User

class UserRepository(private val apiService: UserDtoDao = FakeApiService()) {

    // Get the current user
    var user: User
        get() = apiService.user
        // Set or update the user
        set(user) {
            apiService.user = user
        }
}