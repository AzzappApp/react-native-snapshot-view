package com.azzapp.rnsnapshotview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class RNSnapshotRenderer : AppCompatImageView {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )

  private var _snapshotID: String? = null
  fun setSnapshotID(snapshotID: String?) {
    if (snapshotID != this._snapshotID) {
      this._snapshotID = snapshotID
      if (snapshotID == null) {
        this.setImageBitmap(null)
        return
      }
      this.setImageBitmap(ReactNativeSnapshotViewModule.getSnapShotMap()[snapshotID])
    }
  }
}
