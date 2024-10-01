package com.azzapp.reactnativesnapshotview

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

@ReactModule(name = ReactNativeSnapshotViewViewManager.NAME)
class ReactNativeSnapshotViewViewManager :
  ReactNativeSnapshotViewViewManagerSpec<ReactNativeSnapshotViewView>() {
  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): ReactNativeSnapshotViewView {
    return ReactNativeSnapshotViewView(context)
  }

  @ReactProp(name = "color")
  override fun setColor(view: ReactNativeSnapshotViewView?, color: String?) {
    view?.setBackgroundColor(Color.parseColor(color))
  }

  companion object {
    const val NAME = "ReactNativeSnapshotViewView"
  }
}
