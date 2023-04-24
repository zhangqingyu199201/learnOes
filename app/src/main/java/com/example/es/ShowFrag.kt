package com.example.es

import android.app.ActivityManager
import android.content.Context
import android.opengl.EGL14.eglGetCurrentContext
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [show.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowFrag : Fragment() {
    companion object {
        const val TAG = "Oes"
    }

    var mGlSurfaceView : GLSurfaceView? = null
    var mContext : Context? = null
    var mEglCtx : android.opengl.EGLContext ?= null
    var mOes = Oes()

    private fun glVersion() {
        if (mContext != null) {
            val info = (mContext!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo

            Log.e(TAG, "printVersion: ${info.glEsVersion} ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = inflater.context
        mGlSurfaceView = GLSurfaceView(mContext)
        glVersion()
        mGlSurfaceView!!.setEGLContextClientVersion(3)

        mGlSurfaceView!!.setRenderer(object : GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                GLES30.glClearColor(0.5f, 0.3f, 0.3f, 0.3f)
                mOes.initial(mContext!!)
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                GLES30.glViewport(0, 0, width, height)
            }

            override fun onDrawFrame(gl: GL10?) {
                GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
                mOes.drawRect()
            }
        })

        mEglCtx = eglGetCurrentContext()
        return mGlSurfaceView
    }

}