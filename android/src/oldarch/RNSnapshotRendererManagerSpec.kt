package com.azzapp.rnsnapshotview

import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager

abstract class RNSnapshotRendererManagerSpec<T : View> : SimpleViewManager<T>() {
  abstract fun setSnapshotID(view: T?, snapshodID: String?)
}
