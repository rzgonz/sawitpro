package com.rzgonz.sawitpro

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rzgonz.sawitpro.core.Caption1
import com.rzgonz.sawitpro.core.ComposeFileProvider
import com.rzgonz.sawitpro.core.image64
import com.rzgonz.sawitpro.core.logD
import com.rzgonz.sawitpro.core.safeLaunch
import com.rzgonz.sawitpro.ui.theme.MainViewModel
import com.rzgonz.sawitpro.ui.theme.SawitProTheme
import java.io.IOException

class MainActivity : ComponentActivity() {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val mainViewModel: MainViewModel = MainViewModel();
    val database = Firebase.database
    val myRef = database.getReference("message")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myRef.setValue("Hello, World!")
        setContent {
            SawitProTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FormImageView(title = "Ayam", imageUri = mainViewModel.inputFoto)
                    mainViewModel.inputFoto.value?.run {
                        proccessImageToText(context = baseContext, this)
                    }
                }
            }
        }
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