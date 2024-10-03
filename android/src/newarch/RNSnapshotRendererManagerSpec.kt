package com.azzapp.rnsnapshotview

import android.view.View

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.RNSnapshotRendererManagerDelegate
import com.facebook.react.viewmanagers.RNSnapshotRendererManagerInterface

abstract class RNSnapshotRendererManagerSpec<T : View> : SimpleViewManager<T>(), RNSnapshotRendererManagerInterface<T> {
  private val mDelegate: ViewManagerDelegate<T>

  init {
    mDelegate = RNSnapshotRendererManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<T>? {
    return mDelegate
  }
}
