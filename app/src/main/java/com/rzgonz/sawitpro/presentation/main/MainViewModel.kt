package com.rzgonz.sawitpro.presentation.main

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.rzgonz.sawitpro.core.Resource
import com.rzgonz.sawitpro.core.logD
import com.rzgonz.sawitpro.core.orFalse
import com.rzgonz.sawitpro.data.dao.SawitProOcrDao
import com.rzgonz.sawitpro.data.dto.SawitProOcrDto
import com.rzgonz.sawitpro.domain.AppUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by rzgonz on 11/03/23.
 *
 */
class MainViewModel(
    private val appUseCase: AppUseCase
) : ViewModel() {
    private var _listOcrData = mutableStateOf<List<SawitProOcrDao>>(emptyList())
    val listOcdData: State<List<SawitProOcrDao>> = _listOcrData

    init {
        appUseCase.getListOcrData()
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _listOcrData.value = resource.data!!
                    }
                    is Resource.Error -> {
                        logD<MainViewModel>(resource.error!!)
                    }
                }

            }
            .launchIn(viewModelScope)
    }
}