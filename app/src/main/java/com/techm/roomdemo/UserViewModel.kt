package com.techm.roomdemo

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techm.roomdemo.db.User
import com.techm.roomdemo.db.UserRepository
import kotlinx.coroutines.launch

/**
 * Created Viewmodel class and give reference of repository class added instance as constructor parameter and extended with viewmodel class
 */
class UserViewModel(private val repository: UserRepository) : ViewModel(), Observable {

    val users = repository.users
    private var isUpdate = false
    private lateinit var userToUpdate: User

    // for views
    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    // For the text displaying buttons
    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    // initial displaying names for buttons
    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {

        if (inputName.value == null) {
            statusMessage.value = Event("Please enter user's name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter user's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a correct email address")
        } else {
            if (isUpdate) {
                userToUpdate.name = inputName.value!!
                userToUpdate.email = inputEmail.value!!
                update(userToUpdate)
            } else {
                // value should be not null
                val name = inputName.value!!
                val email = inputEmail.value!!
                // as id is autoincremental we can set id = 0
                insert(User(0, name, email))
                // clear input field after saving data
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    // To insert User in Database
    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
      /* val newRowId = repository.insert(user)
        if (newRowId > -1) {
            statusMessage.value = Event("User Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }*/
    }


    fun clearAllOrDelete() {
        if (isUpdate) {
            // delete(userToUpdateOrDelete)
        } else {
             clearAll()
        }
    }

    fun update(user: User) = viewModelScope.launch {

        val noOfRows = repository.update(user)
        if (noOfRows > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdate = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }

    }

     fun delete(user: User) = viewModelScope.launch {
         val noOfRowsDeleted = repository.delete(user)

         if (noOfRowsDeleted > 0) {
             inputName.value = null
             inputEmail.value = null
           //  isUpdateOrDelete = false
             saveOrUpdateButtonText.value = "Save"
             clearAllOrDeleteButtonText.value = "Clear All"
             statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
         } else {
             statusMessage.value = Event("Error Occurred")
         }

     }

    fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Users Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun initUpdateAndDelete(user: User) {
        inputName.value = user.name
        inputEmail.value = user.email
        isUpdate = true
        userToUpdate = user
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"

    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}