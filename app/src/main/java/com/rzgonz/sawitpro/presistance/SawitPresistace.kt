package com.rzgonz.sawitpro.presistance

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rzgonz.sawitpro.core.Resource
import com.rzgonz.sawitpro.core.logD
import com.rzgonz.sawitpro.core.orZero
import com.rzgonz.sawitpro.core.safeLaunchWithResult
import com.rzgonz.sawitpro.data.dao.SawitProOcrDao
import com.rzgonz.sawitpro.data.dto.SawitProOcrDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by rzgonz on 13/03/23.
 *
 */
class SawitPresistace() {

    val database = Firebase.database
    val myRef = database.getReference("listOcrText")

    fun saveData(sawitProOcrDto: SawitProOcrDto) {
        myRef.push().setValue(sawitProOcrDto)
    }


    fun getListOnce(): Flow<Resource<List<SawitProOcrDao>>> = callbackFlow {
        myRef.get()
            .addOnCompleteListener { task ->
                val response = if (task.isSuccessful) {
                    val listOcr = arrayListOf<SawitProOcrDao>()
                    safeLaunchWithResult(emptyList<SawitProOcrDao>()) {
                        for (itemSnapShot in task.result.children) {
                            val ocrItem = itemSnapShot.getValue<SawitProOcrDto>()
                            val item = ocrItem?.let {
                                SawitProOcrDao(
                                    id = itemSnapShot.key.orEmpty(),
                                    ocrData = ocrItem
                                )
                            }
                            if (item != null) {
                                listOcr.add(item)
                            }
                        }
                    }
                    Resource.Success(listOcr.toList())
                } else {
                    Resource.Error(task.exception?.localizedMessage.toString())
                }
                trySend(response).isSuccess
            }

        awaitClose {
            close()
        }
    }

    fun getListOcrRealtime(): Flow<Resource<List<SawitProOcrDao>>> = callbackFlow {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listOcr = arrayListOf<SawitProOcrDao>()
                safeLaunchWithResult(emptyList<SawitProOcrDao>()) {
                    for (itemSnapShot in dataSnapshot.children) {
                        val ocrItem = itemSnapShot.getValue<SawitProOcrDto>()
                        val item = ocrItem?.let {
                            SawitProOcrDao(
                                id = itemSnapShot.key.orEmpty(),
                                ocrData = ocrItem
                            )
                        }
                        if (item != null) {
                            listOcr.add(item)
                        }
                    }

                    trySend(Resource.Success(listOcr.reversed())).isSuccess
                }

            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error = error.message)).isFailure
            }
        })

        awaitClose {
            close()
        }
    }

}