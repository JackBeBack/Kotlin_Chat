package Viewmodel

import kotlinx.coroutines.flow.MutableStateFlow

class WindowViewModel() {
    val windowTitle = MutableStateFlow("New Chat")

    companion object{
        val instance = WindowViewModel()
    }
}