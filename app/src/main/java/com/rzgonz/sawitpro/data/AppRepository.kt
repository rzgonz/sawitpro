package com.rzgonz.sawitpro.data

import com.google.android.gms.maps.model.LatLng
import com.rzgonz.sawitpro.data.dto.SawitProOcrDto
import com.rzgonz.sawitpro.data.local.AppLocalDataSource
import com.rzgonz.sawitpro.data.remote.MapRemoteDataSource
import com.rzgonz.sawitpro.presistance.SawitPresistace

/**
 * Created by rzgonz on 12/03/23.
 *
 */
class AppRepository(
    private val appLocalDataSource: AppLocalDataSource,
    private val mapsRemoteDataSource: MapRemoteDataSource,
    private val presistace: SawitPresistace = SawitPresistace()
) {

    var isFirstShowPermission: Boolean
        set(value) {
            appLocalDataSource.isFirstShowPermission = value
        }
        get() = appLocalDataSource.isFirstShowPermission

//    suspend fun getDistaceLocation(fromLocation:LatLng,toLocation:LatLng){
//        val request = Map<String,String>
//        //request[origin]
//        return mapsRemoteDataSource
//    }

    fun saveOcrData(sawitProOcrDto: SawitProOcrDto) {
        presistace.saveData(sawitProOcrDto)
    }

    fun getListOcrData() = presistace.getListOcrRealtime()
}