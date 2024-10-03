package com.azzapp.rnsnapshotview


import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

@ReactModule(name = RNSnapshotRendererManager.NAME)
class RNSnapshotRendererManager :
  RNSnapshotRendererManagerSpec<RNSnapshotRenderer>() {
  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): RNSnapshotRenderer {
    return RNSnapshotRenderer(context)
  }

  @ReactProp(name = "snapshotID")
  override fun setSnapshotID(view: RNSnapshotRenderer?, snapshotID: String?) {
    view?.setSnapshotID(snapshotID)
  }

  companion object {
    const val NAME = "RNSnapshotRenderer"
  }
}
