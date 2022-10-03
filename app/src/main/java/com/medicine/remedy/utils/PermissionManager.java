package com.medicine.remedy.utils;

import static com.medicine.remedy.utils.ExtraUtils.startActivityIn;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class PermissionManager
{
    /**
     * Request for phone call permission
     * -------- Phone call -----------
     */
    public static void requestForPhoneCallPermission(final Activity activity, PermissionListener permissionListener) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(permissionListener)
                .withErrorListener(error -> ViewUtils.showToast(activity, "Error occurred! " , AppConstants.WARNING_DIALOG))
                .check();
    }

    /**
     * Request for read and write external storage permission to pick images from gallery
     * -------- READ EXTERNAL STORAGE -----------
     */
    public static void requestForReadWriteExternalStoragePermission(final Activity activity, MultiplePermissionsListener permissionListener) {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(permissionListener)
                .withErrorListener(error -> ViewUtils.showToast(activity, "Error occurred! " , AppConstants.WARNING_DIALOG))
                .check();
    }

    public static void requestForGpsPermission(final Activity activity, MultiplePermissionsListener multiplePermissionsListener) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(multiplePermissionsListener)
                .withErrorListener(error ->ViewUtils.showToast(activity, "Error occurred! " , AppConstants.WARNING_DIALOG)).check();
    }

    // show dialog to warn user about permission if they cancel the permission
    public static void showSettingsDialog(final Activity activity,String settings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            if(settings.equals(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                openGpsSettings(activity,settings);
            else openSettings(activity,settings);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private static void openSettings(Activity activity,String settings) {
        Intent intent = new Intent(settings);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    public static void openAppDetailsSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    // navigating user to app settings
    private static void openGpsSettings(Activity activity,String settings) {
        Intent intent = new Intent(settings);
        activity.startActivity(intent);
    }

    public static boolean isRequiredVideoCallPermissionGranted(Context context) {
        return (
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED&&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED&&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED&&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isRequiredGpsPermissionGranted(Context context) {
        return (
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isCallPhonePermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isStorageReadWritePermissionGranted(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }


    public static boolean isPhoneCallPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCameraPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReadSMSPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReceiveSMSPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * ...enable gps location
     * ...request for permission if location permission is not granted
     * ...go to setting for enable gps location while location is disable
     */
    public static void enableGpsLocationPermission(Activity mActivity, Intent intent)
    {
        MultiplePermissionsListener permissionListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted()) {
                    startActivityIn(mActivity, intent);
                } else{
                    PermissionManager.openAppDetailsSettings(mActivity);
                    ViewUtils.showToast(mActivity, "You need to grant all permission" , AppConstants.WARNING_DIALOG);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };

        if (PermissionManager.isRequiredGpsPermissionGranted(mActivity))
            startActivityIn(mActivity, intent);
        else PermissionManager.requestForGpsPermission(mActivity, permissionListener);
    }
}
