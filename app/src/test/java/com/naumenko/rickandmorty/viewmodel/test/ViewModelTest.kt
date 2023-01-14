package com.naumenko.rickandmorty.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
open class ViewModelTest {

    @get:Rule
    val testViewModelScopeRule = TestViewModelScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

}