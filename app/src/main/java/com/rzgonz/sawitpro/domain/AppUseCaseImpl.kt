package com.rzgonz.sawitpro.domain

import com.rzgonz.sawitpro.core.Resource
import com.rzgonz.sawitpro.data.AppRepository
import com.rzgonz.sawitpro.data.dao.SawitProOcrDao
import com.rzgonz.sawitpro.data.dto.SawitProOcrDto
import kotlinx.coroutines.flow.Flow

/**
 * Created by rzgonz on 12/03/23.
 *
 */
class AppUseCaseImpl(
    private val appRepository: AppRepository
) : AppUseCase {
    override fun isFirstShowPermission(): Boolean {
        return appRepository.isFirstShowPermission
    }

    override fun disableFirstShowPermission() {
        appRepository.isFirstShowPermission = false
    }

    override fun saveOcrData(sawitProOcrDto: SawitProOcrDto) {
        appRepository.saveOcrData(sawitProOcrDto)
    }

    override fun getListOcrData(): Flow<Resource<List<SawitProOcrDao>>> {
        return appRepository.getListOcrData()
    }

}