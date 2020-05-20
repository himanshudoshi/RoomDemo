package com.techm.roomdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.techm.roomdemo.db.UserRepository

/*@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val repository: UserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
     if(modelClass.isAssignableFrom(UserViewModel::class.java)){
         return UserViewModel(repository) as T
     }
        throw IllegalArgumentException("Unknown View Model class")
    }

}*/


@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        UserViewModel(userRepository) as T
}