package com.vladislavmyasnikov.courseproject.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vladislavmyasnikov.courseproject.domain.entities.Lecture
import com.vladislavmyasnikov.courseproject.domain.models.Outcome
import com.vladislavmyasnikov.courseproject.domain.repositories.ILectureRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LectureListViewModel(private val lectureRepository: ILectureRepository) : ViewModel() {

    val lecturesFetchOutcome: Observable<Outcome<List<Lecture>>> = lectureRepository.lecturesFetchOutcome
    var lectures: List<Lecture> = emptyList()
    var isLoading: Boolean = false
    private val disposables = CompositeDisposable()

    init {
        disposables.add(lecturesFetchOutcome.subscribe {
            when (it) {
                is Outcome.Success -> {
                    lectures = it.data
                    Log.d("LECTURE_LIST_VM", "Lectures are fetched (size: ${lectures.size})")
                }
                is Outcome.Progress -> isLoading = it.loading
            }
        })
    }

    fun fetchLectures() {
        lectureRepository.fetchLectures()
    }

    fun refreshLectures() {
        lectureRepository.refreshLectures()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}



class LectureListViewModelFactory @Inject constructor(private val lectureRepository: ILectureRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LectureListViewModel::class.java)) {
            LectureListViewModel(lectureRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}
