package com.example.nexus.viewmodels

import androidx.lifecycle.ViewModel
import com.example.nexus.data.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NexusLoginViewModel  @Inject constructor(private val repo: LoginRepository) : ViewModel(){
}