package com.rzgonz.sawitpro.domain

import com.rzgonz.sawitpro.core.Resource
import com.rzgonz.sawitpro.data.dao.SawitProOcrDao
import com.rzgonz.sawitpro.data.dto.SawitProOcrDto
import kotlinx.coroutines.flow.Flow

/**
 * Created by rzgonz on 12/03/23.
 *
 */
interface AppUseCase {
    fun isFirstShowPermission(): Boolean
    fun disableFirstShowPermission()

    fun saveOcrData(sawitProOcrDto: SawitProOcrDto)

    fun getListOcrData(): Flow<Resource<List<SawitProOcrDao>>>
}