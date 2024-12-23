package com.abdo.airplane;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebStorage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

public class airplaner extends CordovaPlugin {

    private static final String TAG = "airplaner";
    private CallbackContext callbackContext;
    private static ShizukuRemoteProcess mProcess = null;
    private static String mDir = "/";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("toggleAirplaneMode")) {
            toggleAirplaneMode();
            return true;
        }
        return false;
    }

    private void toggleAirplaneMode() {
        // Turn off Airplane Mode and clear cache before toggling
        clearAppCache();

        // Toggle Airplane Mode
        setAirplaneMode(true);

        // Wait for 3 seconds, then turn it back on
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAirplaneMode(false);
            }
        }, 700);
    }

    private void setAirplaneMode(boolean enable) {
        try {
            // Execute the command using Shizuku
            mProcess = Shizuku.newProcess(new String[]{"sh", "-c", "cmd connectivity airplane-mode " + (enable ? "enable" : "disable")}, null, mDir);

            BufferedReader mInput = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            BufferedReader mError = new BufferedReader(new InputStreamReader(mProcess.getErrorStream()));
            String line;

            sendSuccessResult("Airplane Mode " + (enable ? "enabled" : "disabled"));
            Log.i(TAG, "Airplane Mode " + (enable ? "enabled" : "disabled") + " via ADB shell");

        } catch (Exception e) {
            sendErrorResult("Error executing ADB shell command: " + e.getMessage());
            Log.e(TAG, "Error executing ADB shell command: ", e);
        }
    }

    private void clearAppCache() {
        try {
            // // Clear WebView cache
            // cordova.getActivity().runOnUiThread(new Runnable() {
            //     @Override
            //     public void run() {
            //         webView.clearCache(true);
            //     }
            // });

            // Clear cookies
            // CookieManager.getInstance().removeAllCookies(null);
            // CookieManager.getInstance().flush();

            // Clear WebStorage (localStorage, sessionStorage)
           // WebStorage.getInstance().deleteAllData();

            // Clear app cache
            // Context context = cordova.getActivity().getApplicationContext();
            // context.getCacheDir().delete();

            Log.i(TAG, "App cache cleared successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing cache: " + e.getMessage(), e);
        }
    }

    private void sendSuccessResult(String message) {
        PluginResult result = new PluginResult(PluginResult.Status.OK, message);
        this.callbackContext.sendPluginResult(result);
        Log.i(TAG, message);
    }

    private void sendErrorResult(String message) {
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, message);
        this.callbackContext.sendPluginResult(result);
        Log.e(TAG, message);
    }
}
