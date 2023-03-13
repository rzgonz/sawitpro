package com.rzgonz.sawitpro.presentation.ocr

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rzgonz.sawitpro.R
import com.rzgonz.sawitpro.core.Async
import com.rzgonz.sawitpro.core.Fail
import com.rzgonz.sawitpro.core.Loading
import com.rzgonz.sawitpro.core.Success
import com.rzgonz.sawitpro.core.Uninitialized
import com.rzgonz.sawitpro.core.logD
import com.rzgonz.sawitpro.core.orFalse
import com.rzgonz.sawitpro.core.safeLaunch
import com.rzgonz.sawitpro.data.dto.SawitProOcrDto
import com.rzgonz.sawitpro.domain.AppUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Created by rzgonz on 11/03/23.
 *
 */
data class OcrViewState(
    val ocrProgressAsync: Async<String> = Uninitialized,
)

class OcrViewModel(
    private val appUseCase: AppUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(OcrViewState())
    val state = _state.asStateFlow()

    val successInputData = mutableStateOf(false)
    val inputFoto = mutableStateOf<Uri?>(null)
    val originLatLong = mutableStateOf(LatLng(0.0, 0.0))

    val permissionGranted = mutableStateOf(false)
    val isGpsOn = mutableStateOf(false)
    val isFirstShowPermission = mutableStateOf(appUseCase.isFirstShowPermission())


    val inputText = mutableStateOf(TextFieldValue(""))
    val inputDistance = mutableStateOf(TextFieldValue(""))
    val inputDuration = mutableStateOf(TextFieldValue(""))

    fun hiddenFirstShowPermission() {
        appUseCase.disableFirstShowPermission()
        isFirstShowPermission.value.orFalse()
    }

    fun saveOcrData() {
        viewModelScope.launch {
            val sawitProOcrDto = SawitProOcrDto(
                text = inputText.value.text,
                distance = inputDistance.value.text,
                duration = inputDuration.value.text
            )
            appUseCase.saveOcrData(sawitProOcrDto)
            successInputData.value = true
        }

    }

    fun proccessImageToText(
        context: Context, imageUri: Uri, ocrText: MutableState<TextFieldValue>,
    ) {
        viewModelScope.launch {
            _state.update { it.copy(ocrProgressAsync = Loading()) }
            ocrText.value = TextFieldValue("")
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            safeLaunch {
                val image: InputImage
                try {
                    image = InputImage.fromFilePath(context, imageUri)
                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            // Task completed successfully
                            // ...
                            inputDistance.value =
                                TextFieldValue(context.getString(R.string.common_text_calculating_process))
                            inputDuration.value =
                                TextFieldValue(context.getString(R.string.common_text_calculating_process))
                            logD<OcrViewModel>("OCR value ${visionText.text}")
                            _state.update { it.copy(ocrProgressAsync = Success(visionText.text)) }
                            ocrText.value = TextFieldValue(visionText.text)

                        }
                        .addOnFailureListener { e ->
                            // Task failed with an exception
                            // ...
                            logD<OcrViewModel>("OCR ERROR ${e.message.orEmpty()}")
                            _state.update { it.copy(ocrProgressAsync = Fail(e)) }
                            ocrText.value = TextFieldValue(e.message.orEmpty())
                        }

                } catch (e: IOException) {
                    e.printStackTrace()
                    _state.update { it.copy(ocrProgressAsync = Fail(e)) }
                }
            }
        }
    }
}