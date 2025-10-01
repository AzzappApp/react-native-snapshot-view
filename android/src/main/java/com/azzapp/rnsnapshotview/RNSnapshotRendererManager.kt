package com.azzapp.rnsnapshotview

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.RNSnapshotRendererManagerInterface
import com.facebook.react.viewmanagers.RNSnapshotRendererManagerDelegate

@ReactModule(name = RNSnapshotRendererManager.NAME)
class RNSnapshotRendererManager : SimpleViewManager<RNSnapshotRenderer>(),
  RNSnapshotRendererManagerInterface<RNSnapshotRenderer> {
  private val mDelegate: ViewManagerDelegate<RNSnapshotRenderer>
  init {
    mDelegate = RNSnapshotRendererManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<RNSnapshotRenderer>? {
    return mDelegate
  }

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
