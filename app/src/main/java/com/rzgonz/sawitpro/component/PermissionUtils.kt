//package com.rzgonz.sawitpro.component
//
//import android.content.Context
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.core.app.ActivityCompat
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.rememberPermissionState
//
///**
// * Created by rzgonz on 12/03/23.
// *
// */
///**
// * request permission or not
// */
//class RequestPermissionState(initRequest: Boolean, val permission: String) {
//    var requestPermission by mutableStateOf(initRequest)
//}
//
///**
// * Remember whether permission should be requested or not, true initially by default
// */
//@Composable
//fun rememberRequestPermissionsState(
//    initRequest: Boolean = true,
//    permissions: String
//): RequestPermissionState {
//    return remember {
//        RequestPermissionState(initRequest, permissions)
//    }
//}
//
///**
// * Permission requester
// */
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun RequestPermission(
//    context: Context,
//    requestState: RequestPermissionState,
//    granted: () -> Unit,
//    showRational: () -> Unit,
//    permanentlyDenied: () -> Unit
//) {
//    val permissionState =
//        rememberPermissionState(permission = requestState.permission) { isGranted ->
//            // This block will be triggered after the user chooses to grant or deny the permission
//            // and we can tell if the user permanently declines or if we need to show rational
//            val permissionPermanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(
//                context.findActivity(), requestState.permission
//            ) && !isGranted
//
//            if (permissionPermanentlyDenied) {
//                permanentlyDenied()
//            } else if (!isGranted) {
//                showRational()
//            }
//        }
//
//    // If requestPermission, then launchPermissionRequest and the user will be able to choose
//    // to grant or deny the permission.
//    // After that, the RequestPermission will recompose and permissionState above will be triggered
//    // and we can differentiate whether the permission is permanently declined or whether rational
//    // should be shown
//    if (requestState.requestPermission) {
//        requestState.requestPermission = false
//        if (permissionState.status.isGranted) {
//            granted()
//        } else {
//            LaunchedEffect(key1 = Unit) {
//                permissionState.launchPermissionRequest()
//            }
//        }
//    }
//}