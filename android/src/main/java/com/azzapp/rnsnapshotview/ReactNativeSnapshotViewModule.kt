package com.azzapp.rnsnapshotview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.common.annotations.UnstableReactNativeAPI
import com.facebook.react.fabric.FabricUIManager
import com.facebook.react.uimanager.NativeViewHierarchyManager
import com.facebook.react.uimanager.UIBlock
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.common.UIManagerType
import java.util.Collections
import java.util.LinkedList
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class ReactNativeSnapshotViewModule internal constructor(context: ReactApplicationContext) :
  RNSnapshotViewSpec(context) {

  override fun getName(): String {
    return NAME
  }

  @OptIn(UnstableReactNativeAPI::class)
  @ReactMethod
  override fun captureSnapshot(viewTag: Double, promise: Promise) {

    val handleView = handleView@{ view: View? ->
      if (view != null) {
        val bitmap: Bitmap
        try {
          bitmap = getBitmapFromView(view)
        } catch (e: Throwable) {
          promise.reject("FAILED_TO_CREATE_BITMAP", e)
          return@handleView
        }
        val uuid = UUID.randomUUID().toString()
        snapshotMap[uuid] = bitmap;
        promise.resolve(uuid)
      } else {
        promise.reject("not_found", "View not found")
      }
    }

    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      val uiManager = UIManagerHelper.getUIManager(reactApplicationContext, UIManagerType.FABRIC)
      if (uiManager is FabricUIManager) {
        uiManager.addUIBlock { uiBlockViewResolver ->
          val view = uiBlockViewResolver.resolveView(viewTag.toInt())
          handleView(view);
        }
      } else {
        promise.reject("not_found", "Cannot obtain UIManager")
        return
      }
    } else {
      val uiManager = reactApplicationContext.getNativeModule(
        UIManagerModule::class.java
      )
      if (uiManager == null) {
        promise.reject("not_found", "Cannot obtain UIManager")
        return
      }
      uiManager.addUIBlock(UIBlock { nativeViewHierarchyManager: NativeViewHierarchyManager ->
        val view = nativeViewHierarchyManager.resolveView(viewTag.toInt())
        handleView(view)
      })
    }
  }

  @ReactMethod
  override fun releaseSnapshot(snapshotID: String, promise: Promise) {
    snapshotMap.remove(snapshotID)
    promise.resolve(null);
  }

  private fun getBitmapFromView(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)

    val childrenList = getAllChildren(view)
    val paint = Paint()
    paint.isAntiAlias = true
    paint.isFilterBitmap = true
    paint.isDither = true

    for (child in childrenList) {
      // Skip any child that we don't know how to process
      if (child is TextureView) {
        if (child.getVisibility() != VISIBLE) continue
        child.isOpaque = false
        val childBitmapBuffer = child.getBitmap(
          Bitmap.createBitmap(child.getWidth(), child.getHeight(), Bitmap.Config.ARGB_8888)
        )

        val countCanvasSave = canvas.save()
        applyTransformations(canvas, view, child)

        // Due to re-use of bitmaps for screenshot, we can get bitmap that is bigger in size than requested
        canvas.drawBitmap(childBitmapBuffer, 0f, 0f, paint)

        canvas.restoreToCount(countCanvasSave)
        childBitmapBuffer.recycle()
      } else if (child is SurfaceView) {
        val latch = CountDownLatch(1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          val childBitmapBuffer = Bitmap.createBitmap(child.getWidth(), child.getHeight(), Bitmap.Config.ARGB_8888)
          try {
            PixelCopy.request(child, childBitmapBuffer, {
              val countCanvasSave = canvas.save()
              applyTransformations(canvas, view, child)
              canvas.drawBitmap(childBitmapBuffer, 0f, 0f, paint)
              canvas.restoreToCount(countCanvasSave)
              latch.countDown()
            }, Handler(Looper.getMainLooper()))
            latch.await(SURFACE_VIEW_READ_PIXELS_TIMEOUT, TimeUnit.SECONDS)
          } catch (e: Exception) {
            Log.e(TAG, "Cannot PixelCopy for $child", e)
          }
        } else {
          val cache = child.drawingCache
          if (cache != null) {
            canvas.drawBitmap(child.drawingCache, 0f, 0f, paint)
          }
        }
      }
    }
    return bitmap
  }



  private fun getAllChildren(v: View): List<View> {
    if (v !is ViewGroup) {
      val viewArrayList = ArrayList<View>()
      viewArrayList.add(v)

      return viewArrayList
    }

    val result = ArrayList<View>()

    for (i in 0 until v.childCount) {
      val child: View = v.getChildAt(i)

      // Do not add any parents, just add child elements
      result.addAll(getAllChildren(child))
    }

    return result
  }

  /**
   * Concat all the transformation matrix's from parent to child.
   */
  private fun applyTransformations(c: Canvas, root: View, child: View): Matrix {
    val transform: Matrix = Matrix()
    val ms = LinkedList<View?>()

    // Find all parents of the child view
    var iterator = child
    do {
      ms.add(iterator)

      iterator = iterator.parent as View
    } while (iterator !== root)

    // Apply transformations from parent --> child order
    Collections.reverse(ms)

    for (v in ms) {
      if (v == null) {
        continue
      }
      // Apply each view transformations, so each child will be affected by them
      val dx = v.left + (if ((v !== child)) v.paddingLeft else 0) + v.translationX
      val dy = v.top + (if ((v !== child)) v.paddingTop else 0) + v.translationY
      c.translate(dx, dy)
      transform.postTranslate(dx, dy)

      c.rotate(v.rotation, v.pivotX, v.pivotY)
      transform.postRotate(v.rotation, v.pivotX, v.pivotY)

      c.scale(v.scaleX, v.scaleY, v.pivotX, v.pivotY)
      transform.postScale(v.scaleX, v.scaleY, v.pivotX, v.pivotY)
    }

    return transform
  }

  companion object {
    const val NAME = "RNSnapshotView"

    const val TAG = "ReactNativeSnapshotView"

    private val snapshotMap: MutableMap<String, Bitmap> = HashMap()

    fun getSnapShotMap(): Map<String, Bitmap> {
      return snapshotMap
    }

    private const val SURFACE_VIEW_READ_PIXELS_TIMEOUT: Long = 5
  }
}
