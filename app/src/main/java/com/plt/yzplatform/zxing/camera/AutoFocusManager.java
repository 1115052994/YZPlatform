/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.plt.yzplatform.zxing.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;


import com.plt.yzplatform.zxing.android.PreferencesActivity;

import java.util.ArrayList;
import java.util.Collection;



/**
 * 自动聚焦
 * @author qichunjie
 *
 */
final class AutoFocusManager implements Camera.AutoFocusCallback {

  private static final String TAG = AutoFocusManager.class.getSimpleName();
  //自动聚焦时所间隔的时间
  private static final long AUTO_FOCUS_INTERVAL_MS = 500L;
  private static final Collection<String> FOCUS_MODES_CALLING_AF;
  static {
    FOCUS_MODES_CALLING_AF = new ArrayList<String>(2);
    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO);
    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO);
  }

  private boolean stopped;
  private boolean focusing;
  private final boolean useAutoFocus;
  private final Camera camera;
  private Context context;

  AutoFocusManager(Context context, Camera camera) {
    this.camera = camera;
    this.context = context;
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//    String currentFocusMode = camera.getParameters().getFocusMode();
    Camera.Parameters parameters = camera.getParameters();
    if (parameters.isZoomSupported()) {
      // 设置成最大倍数的1/10，基本符合远近需求
      parameters.setZoom(parameters.getMaxZoom() / 10);
    }
    this.camera.setParameters(parameters);

    String currentFocusMode = parameters.getFocusMode();
    useAutoFocus =
        sharedPrefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true) &&
        FOCUS_MODES_CALLING_AF.contains(currentFocusMode);
    Log.i(TAG, "Current focus mode '" + currentFocusMode + "'; use auto focus? " + useAutoFocus);
    start();
  }

  @Override
  public synchronized void onAutoFocus(boolean success, Camera theCamera) {
    focusing = false;
    autoFocusAgainLater();
  }

  @SuppressLint("NewApi")
  private synchronized void autoFocusAgainLater() {
    if (!stopped ) {
      new Thread(sleepThread).start();
    }
  }

  Runnable sleepThread = new Runnable() {
    @Override
    public void run() {
      try {
        Thread.sleep(AUTO_FOCUS_INTERVAL_MS);
      } catch (InterruptedException e) {
        // continue
      }
      autoFocusHandler.sendMessage(autoFocusHandler.obtainMessage());
    }
  };

  Handler autoFocusHandler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      start();
    }
  };

  synchronized void start() {
    if (useAutoFocus) {
      if (!stopped) {
        try {
          camera.autoFocus(this);
          //focusing = true;
        } catch (RuntimeException re) {
          // Have heard RuntimeException reported in Android 4.0.x+; continue?
          Log.w(TAG, "Unexpected exception while focusing", re);
          // Try again later to keep cycle going
          autoFocusAgainLater();
        }
      }
    }
  }

  synchronized void stop() {
    stopped = true;
    if (useAutoFocus) {
      // Doesn't hurt to call this even if not focusing
      try {
        camera.cancelAutoFocus();
      } catch (RuntimeException re) {
        // Have heard RuntimeException reported in Android 4.0.x+; continue?
        Log.w(TAG, "Unexpected exception while cancelling focusing", re);
      }
    }
  }
}
