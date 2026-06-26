package com.example.signtranslate.avatar

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.signtranslate.ui.service.AvatarService
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class SceneManager(
    context: Context,
    private val sceneView: SceneView
) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var avatarNode: ModelNode? = null
    
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var rotationY = 0f
    private var scaleFactor = 1.0f
    private val scaleMin = 0.5f
    private val scaleMax = 3.0f

    private val scaleDetector = ScaleGestureDetector(
        context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = max(scaleMin, min(scaleMax, scaleFactor))
                avatarNode?.scale = io.github.sceneview.math.Scale(scaleFactor)
                return true
            }
        }
    )

    fun setup() {
        AvatarService.markLoading()
        scope.launch {
            try {
                loadAvatar()
                setupGestures()
                observeAnimations()
                AvatarService.markReady()
            } catch (e: Exception) {
                e.printStackTrace()
                AvatarService.markError()
            }
        }
    }

    private suspend fun loadAvatar() {
        val modelNode = ModelNode(
            modelInstance = sceneView.modelLoader.loadModelInstance(
                fileLocation = AvatarService.AVATAR_ASSET
            )!!,
            scaleToUnits = 1.8f,
            centerOrigin = Position(y = -1.0f)
        ).apply {
            position = Position(x = 0f, y = -0.5f, z = -2.5f)
            rotation = Rotation(x = 0f, y = 0f, z = 0f)
        }
        sceneView.addChildNode(modelNode)
        avatarNode = modelNode
        playAnimation("Idle")
    }

    fun playAnimation(animationName: String) {
        val node = avatarNode ?: return
        val instance = node.modelInstance
        val index = (0 until instance.animator.animationCount).firstOrNull { i ->
            instance.animator.getAnimationName(i) == animationName
        } ?: 0
        node.playAnimation(animationIndex = index, loop = (animationName == "Idle"))
    }

    private fun observeAnimations() {
        scope.launch {
            AvatarService.currentAnimation.collect { animName ->
                if (animName != null) playAnimation(animName)
            }
        }
    }

    private fun setupGestures() {
        sceneView.setOnTouchListener { view, event ->
            scaleDetector.onTouchEvent(event)
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.getX()
                    lastTouchY = event.getY()
                    view.performClick()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (event.pointerCount == 1) {
                        val dx = event.getX() - lastTouchX
                        rotationY += dx * 0.5f
                        avatarNode?.rotation = Rotation(x = 0f, y = rotationY, z = 0f)
                        lastTouchX = event.getX()
                        lastTouchY = event.getY()
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun destroy() {
        avatarNode?.destroy()
        avatarNode = null
    }
}
