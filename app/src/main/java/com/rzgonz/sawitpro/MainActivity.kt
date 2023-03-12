package com.rzgonz.sawitpro

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rzgonz.sawitpro.component.CustomDialogLocation
import com.rzgonz.sawitpro.core.Caption1
import com.rzgonz.sawitpro.core.ComposeFileProvider
import com.rzgonz.sawitpro.core.image64
import com.rzgonz.sawitpro.core.logD
import com.rzgonz.sawitpro.core.navigateToAppSettings
import com.rzgonz.sawitpro.core.safeLaunch
import com.rzgonz.sawitpro.ui.theme.SawitProTheme
import org.koin.android.ext.android.inject
import java.io.IOException


class MainActivity : ComponentActivity() {
    val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val mainViewModel: MainViewModel by inject()
    val database = Firebase.database
    val myRef = database.getReference("message")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myRef.setValue("Hello, World!")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...
                    mainViewModel.lastLat.value = location.latitude
                    mainViewModel.lastLong.value = location.longitude
                }
            }
        }

        setContent {
            val context = LocalContext.current
            SawitProTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (mainViewModel.permissionGranted.value) {
                        if (mainViewModel.isGpsOn.value) {
                            MainContainer()
                        }else{
                            onResume()
                        }
                    } else {
                        RequestPermission(permission = listOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA
                        ), onGranted = {
                            mainViewModel.permissionGranted.value = true
                        }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        turnOnGPS(this, onSuccess = {
            mainViewModel.isGpsOn.value = true
        })
    }

    @ExperimentalPermissionsApi
    @Composable
    fun RequestPermission(
        permission: List<String>,
        onGranted: () -> Unit
    ) {

        val context = LocalContext.current as Activity
        val permissionState = rememberMultiplePermissionsState(permission)
        if (permissionState.allPermissionsGranted) {
            onGranted()
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
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
                    if (needOpenSetting && mainViewModel.isFirstShowPermission.value.not()) {
                        context.navigateToAppSettings()
                    } else {
                        permissionState.launchMultiplePermissionRequest()
                        mainViewModel.hiddenFirstShowPermission()
                    }
                }) {
                    Text(if (needOpenSetting && mainViewModel.isFirstShowPermission.value.not()) "Open Settings" else "Request permission")
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private fun proccessImageToText(context: Context, imageUri: Uri) {
        safeLaunch {
            val image: InputImage
            try {
                image = InputImage.fromFilePath(context, imageUri)
                val result = recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        // Task completed successfully
                        // ...
                        mainViewModel.ocrText.value = visionText.text
                        logD<MainActivity>("TExt ${visionText.text}")
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        logD<MainActivity>("TExt $e")
                    }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    @Composable
    fun MainContainer() {
        // Checking GPS is enabled
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gps) {
            startLocationUpdates()
        }

        Column() {
            FormImageView(title = "Ayam", imageUri = mainViewModel.inputFoto)
            mainViewModel.inputFoto.value?.run {
                proccessImageToText(context = baseContext, this)
            }
            val ocrText = mainViewModel.ocrText.value
            if (ocrText.isNotEmpty()) {
                Text(text = "OCR TEXT $ocrText")
            }

            Text(text = "Current  Location ${mainViewModel.lastLat.value} : ${mainViewModel.lastLong.value}")

            val singapore = LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(singapore, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = singapore),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPermissionsApi::class)
@Composable
fun FormImageView(
    title: String,
    imageUri: MutableState<Uri?>,
) {

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // 3
            imageUri.value = uri
        }
    )


    // 2
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->

        }
    )

    val permission = Manifest.permission.CAMERA
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            logD<MainActivity>("rememberLauncherForActivityResult")
            val uri = ComposeFileProvider.getImageUri(context)
            imageUri.value = uri
            cameraLauncher.launch(uri)
        } else {

        }
    }

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
                contentDescription = "image ktp",
                modifier = Modifier.image64(),
                contentScale = ContentScale.Fit
            )
            OutlinedButton(
                onClick = {
//                    checkAndRequestCameraPermission(context, permission, launcher, onGrandTed = {
//                        val uri = ComposeFileProvider.getImageUri(context)
//                        imageUri.value = uri
//                        cameraLauncher.launch(uri)
//                    }
//                    )
                    imagePicker.launch("image/*")
                }) {
                Text(text = "Open Camera")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

fun checkAndRequestCameraPermission(
    context: Context,
    permission: String,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    onGrandTed: () -> Unit
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        // Open camera because permission is already granted
        onGrandTed.invoke()
    } else {
        // Request a permission
        launcher.launch(permission)
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {

    if (shouldShowRationale) {

        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.body1.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give Permission")
                }
            }
        )

    } else {
        Content(onClick = onRequestPermission)
    }

}

private fun turnOnGPS(activity: Activity, onSuccess: () -> Unit) {
    val request = LocationRequest.create().apply {
        interval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
    val client: SettingsClient = LocationServices.getSettingsClient(activity)
    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
    task.addOnFailureListener {
        if (it is ResolvableApiException) {
            try {
                it.startResolutionForResult(activity, 12345)
            } catch (sendEx: IntentSender.SendIntentException) {
            }
        }
    }.addOnSuccessListener {
        onSuccess()
    }
}

@Composable
fun Content(showButton: Boolean = true, onClick: () -> Unit) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomDialogLocation(
                title = "Turn On Location Service",
                desc = "Explore the world without getting lost and keep the track of your location.\n\nGive this app a permission to proceed. If it doesn't work, then you'll have to do it manually from the settings.",
                enableLocation,
                onClick
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SawitProTheme {
        Greeting("Android")
    }
}