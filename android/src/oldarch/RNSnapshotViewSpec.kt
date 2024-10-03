package com.azzapp.rnsnapshotview

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.Promise

abstract class RNSnapshotViewSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  abstract fun captureSnapshot(viewTag: Double, promise: Promise)
  abstract fun releaseSnapshot(snapshotID: String, promise: Promise)
}
