package com.azzapp.reactnativesnapshotview

import android.view.View

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.ReactNativeSnapshotViewViewManagerDelegate
import com.facebook.react.viewmanagers.ReactNativeSnapshotViewViewManagerInterface

abstract class ReactNativeSnapshotViewViewManagerSpec<T : View> : SimpleViewManager<T>(), ReactNativeSnapshotViewViewManagerInterface<T> {
  private val mDelegate: ViewManagerDelegate<T>

  init {
    mDelegate = ReactNativeSnapshotViewViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<T>? {
    return mDelegate
  }
}
