package com.kasjan.feauters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class SharedViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> get() = _selectedDate

    fun setSelectedDate(date: Date) {
        _selectedDate.value = date
    }
}
