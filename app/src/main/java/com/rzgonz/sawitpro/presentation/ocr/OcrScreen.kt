package com.rzgonz.sawitpro.presentation.ocr

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.maps.DistanceMatrixApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import com.rzgonz.sawitpro.R
import com.rzgonz.sawitpro.core.Caption1
import com.rzgonz.sawitpro.core.ComposeFileProvider
import com.rzgonz.sawitpro.core.H1
import com.rzgonz.sawitpro.core.LOCATION_PLAZA_INDONESIA
import com.rzgonz.sawitpro.core.Loading
import com.rzgonz.sawitpro.core.Success
import com.rzgonz.sawitpro.core.image64
import com.rzgonz.sawitpro.core.logD
import com.rzgonz.sawitpro.core.navigateToAppSettings
import com.rzgonz.sawitpro.core.showMessage
import com.rzgonz.sawitpro.presentation.SawitProScreenNav
import kotlinx.coroutines.launch
import okhttp3.internal.parseCookie
import okhttp3.internal.wait
import org.koin.androidx.compose.koinViewModel

/**
 * Created by rzgonz on 13/03/23.
 *
 */


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun OcrScreen(
    navHostController: NavHostController,
    ocrViewModel: OcrViewModel = koinViewModel()
) {
    val context = LocalContext.current
    var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? =
        LocationServices.getFusedLocationProviderClient(context)
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations) {
                // Update UI with location data
                // ...
                val origin = LatLng(location.latitude, location.longitude)
                ocrViewModel.originLatLong.value = origin
                logD<OcrViewModel>("GPS Lat ${origin.latitude} ,Long ${origin.longitude}")
                if (ocrViewModel.inputFoto.value != null
                ) {
                    val mapsContext = GeoApiContext.Builder().apply {
                        apiKey(context.getString(R.string.MAPS_API_KEY))
                    }.build()

                    val originAddress = arrayOf("${location.latitude},${location.longitude}")
                    val destination =
                        arrayOf("${LOCATION_PLAZA_INDONESIA.latitude},${LOCATION_PLAZA_INDONESIA.longitude}")
                    val request = DistanceMatrixApi.getDistanceMatrix(
                        mapsContext,
                        originAddress,
                        destination
                    )
                    request.mode(TravelMode.DRIVING);
                    val response = request.await()
                    response.rows.forEach { maps ->
                        maps.elements.forEach { matrix ->
                            ocrViewModel.inputDistance.value =
                                TextFieldValue(matrix.distance.humanReadable)
                            ocrViewModel.inputDuration.value =
                                TextFieldValue(matrix.duration.humanReadable)
                            logD<OcrViewModel>("Distance: ${matrix.distance} Duration :${matrix.duration.humanReadable} --> ${matrix.duration.inSeconds}")
                        }
                    }
                    if (ocrViewModel.state.value.ocrProgressAsync is Success) {
                        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
                        fusedLocationClient = null;
                        locationCallback = null;
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Input Ocr Data",
                        style = H1,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White
                    )
                }, navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                )
            )
        }, content = {
            if (ocrViewModel.permissionGranted.value) {
                if (ocrViewModel.isGpsOn.value) {
                    InputOcrContainer(
                        it,
                        fusedLocationProviderClient = fusedLocationClient,
                        locationCallback = locationCallback
                    )
                } else {
                    turnOnGPS(context, onSuccess = {
                        ocrViewModel.isGpsOn.value = true
                    })
                }
            } else {
                RequestPermission(permission = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA
                ), onGranted = {
                    ocrViewModel.permissionGranted.value = true
                }
                )
            }

        }
    )
    if (ocrViewModel.successInputData.value) {
        navHostController.navigateUp()
    }

}


@Composable
private fun InputOcrContainer(
    paddingValues: PaddingValues,
    ocrViewModel: OcrViewModel = koinViewModel(),
    fusedLocationProviderClient: FusedLocationProviderClient?,
    locationCallback: LocationCallback?
) {
    val context = LocalContext.current
    // Checking GPS is enabled
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    if (gps && ocrViewModel.inputDistance.value.text.isEmpty()) {
        startLocationUpdates(context, fusedLocationProviderClient, locationCallback)
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "Current Location ${ocrViewModel.originLatLong.value}")

        FormImageView(title = "Input Image", imageUri = ocrViewModel.inputFoto)
        if (ocrViewModel.inputText.value.text.isEmpty() && ocrViewModel.inputFoto.value != null) {
            ocrViewModel.inputFoto.value?.run {
                ocrViewModel.proccessImageToText(context = context, this, ocrViewModel.inputText)
            }
        }
        if (ocrViewModel.state.collectAsState().value.ocrProgressAsync is Success
            && ocrViewModel.inputText.value.text.isEmpty()
        ) {
            Text(text = "No Text Detection at Image", color = Color.Red)
        }

        if (ocrViewModel.state.collectAsState().value.ocrProgressAsync is Loading) {
            Text(text = "Processing Image to Text")
        }

        if (ocrViewModel.inputText.value.text.isNotEmpty()) {
            Text(text = "OCR TEXT RESULT")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(100.dp),
                value = ocrViewModel.inputText.value,
                onValueChange = {
                    ocrViewModel.inputText.value = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
            )

            Text(text = "Distance")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(100.dp),
                value = ocrViewModel.inputDistance.value,
                onValueChange = {
                    ocrViewModel.inputDistance.value = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
            )

            Text(text = "Duration")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(100.dp),
                value = ocrViewModel.inputDuration.value,
                onValueChange = {
                    ocrViewModel.inputDuration.value = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
            )
            Button(
                enabled = ocrViewModel.inputFoto.value != null && ocrViewModel.inputDistance.value.text != stringResource(
                    id = R.string.common_text_calculating_process
                ),
                onClick = {
                    ocrViewModel.saveOcrData()
                    context.showMessage("Success Saving Data")
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save Data")
            }
        }

    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPermissionsApi::class)
@Composable
private fun FormImageView(
    title: String,
    imageUri: MutableState<Uri?>,
) {


    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->

        }
    )

    Column(
        horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            8.dp
        )
    ) {
        Text(text = title, style = Caption1)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlideImage(
                model = if (imageUri.value != null) imageUri.value else R.drawable.common_place_image,
                contentDescription = "image",
                modifier = Modifier.image64(),
                contentScale = ContentScale.Fit
            )
            OutlinedButton(
                onClick = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri.value = uri
                    cameraLauncher.launch(uri)
                }) {
                Text(text = "Open Camera")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


private fun startLocationUpdates(
    context: Context,
    fusedLocationProviderClient: FusedLocationProviderClient?,
    locationCallback: LocationCallback?
) {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    locationCallback?.let {
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}


private fun turnOnGPS(context: Context, onSuccess: () -> Unit) {
    val request = LocationRequest.create().apply {
        interval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
    val client: SettingsClient = LocationServices.getSettingsClient((context as Activity))
    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
    task.addOnFailureListener {
        if (it is ResolvableApiException) {
            try {
                it.startResolutionForResult((context as Activity), 12345)
            } catch (sendEx: IntentSender.SendIntentException) {
            }
        }
    }.addOnSuccessListener {
        onSuccess()
    }
}


@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    permission: List<String>,
    onGranted: () -> Unit,
    ocrViewModel: OcrViewModel = koinViewModel()
) {

    val context = LocalContext.current as Activity
    val permissionState = rememberMultiplePermissionsState(permission)
    if (permissionState.allPermissionsGranted) {
        onGranted()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            val textToShow = if (permissionState.shouldShowRationale) {
                "The camera and Location is important for this app. Please grant the permission."
            } else {
                "The camera and Location is important for this app. Please grant the permission"
            }
            Text(textToShow, textAlign = TextAlign.Center)
            var needOpenSetting = false
            permission.forEach {
                val permissionStateItem = rememberPermissionState(it)
                if (permissionStateItem.status.shouldShowRationale.not()) {
                    needOpenSetting = true
                }
            }
            Button(onClick = {
                if (needOpenSetting && ocrViewModel.isFirstShowPermission.value.not()) {
                    context.navigateToAppSettings()
                } else {
                    permissionState.launchMultiplePermissionRequest()
                    ocrViewModel.hiddenFirstShowPermission()
                }
            }) {
                Text(if (needOpenSetting && ocrViewModel.isFirstShowPermission.value.not()) "Open Settings" else "Request permission")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    OcrScreen(navHostController = rememberNavController())
}