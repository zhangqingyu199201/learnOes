package com.example.es

import android.content.Context
import android.opengl.GLES30
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Oes {
    companion object {
        const val TAG = "Oes"

        fun checkGlError() {
            while (true) {
                val glErr = GLES30.glGetError()
                if (glErr != GLES30.GL_NO_ERROR) {
                    Log.e(TAG, "checkGlError: ${glErr}", )
                } else {
                    break
                }
            }
        }
    }

    class ShaderProgram(vs: String, fs: String) {
        private var mVsSrc = ""
        private var mFsSrc = ""
        private var mProgramIdx = 0

        init {
            mVsSrc = vs
            mFsSrc = fs

            var vertexIdx =  GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER)
            if (vertexIdx == 0) {
                Log.e(ShowFrag.TAG, "create vs fail")
            }

            var fragIdx =  GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER)
            if (fragIdx == 0) {
                Log.e(ShowFrag.TAG, "create fs fail")
            }

            GLES30.glShaderSource(vertexIdx, mVsSrc)
            GLES30.glShaderSource(fragIdx, mFsSrc)

            GLES30.glCompileShader(vertexIdx)
            Log.e(ShowFrag.TAG, "vs compile log: ${GLES30.glGetShaderInfoLog(vertexIdx)}")

            GLES30.glCompileShader(fragIdx)
            Log.e(ShowFrag.TAG, "fs compile log: ${GLES30.glGetShaderInfoLog(fragIdx)}")

            var programIdx = GLES30.glCreateProgram()
            if (programIdx == 0) {
                Log.e(ShowFrag.TAG, "create program fail")
            }

            GLES30.glAttachShader(programIdx, vertexIdx)
            GLES30.glAttachShader(programIdx, fragIdx)
            GLES30.glLinkProgram(programIdx)
            Log.e(ShowFrag.TAG, "program compile log: ${GLES30.glGetProgramInfoLog(programIdx)}")

            mProgramIdx = programIdx

            GLES30.glDeleteShader(vertexIdx)
            GLES30.glDeleteShader(fragIdx)
        }

        fun setUniform4f(key: String, x: Float, y: Float, z: Float, w: Float) {
            var idx = GLES30.glGetUniformLocation(mProgramIdx, key)
            if (idx < 0) {
                Log.e(TAG, "setUniform4f: $key : $idx", )
            } else {
                GLES30.glUniform4f(idx, x, y, z, w)
            }
            
        }

        fun active() {
            GLES30.glUseProgram(mProgramIdx)
        }

        fun getProgramIdx() : Int {
            return mProgramIdx
        }
    }

    class FloatVao(data: FloatArray,
                   location : Int,
                   element: Int,
                   stride: Int,
                   offset: Int) {
        private var mDataBuffer : FloatBuffer ? = null
        private var mVao = 0
        private var mVbo = 0

        init {
            mDataBuffer = ByteBuffer.allocateDirect(data.size * Float.SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mDataBuffer!!.put(data)
            mDataBuffer!!.position(0)

            var vao = IntBuffer.allocate(1)
            GLES30.glGenVertexArrays(1, vao)
            var vbo = IntBuffer.allocate(1);
            GLES30.glGenBuffers(1, vbo)

            mVao = vao[0]
            mVbo = vbo[0]

            GLES30.glBindVertexArray(mVao)
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVbo)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, data.size * Float.SIZE_BYTES, mDataBuffer, GLES30.GL_STATIC_DRAW)

            GLES30.glVertexAttribPointer(location, element
                , GLES30.GL_FLOAT, false, stride, offset)

            GLES30.glEnableVertexAttribArray(mVao)
        }

        fun active() {
            GLES30.glBindVertexArray(mVao)
            // GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVbo)
        }
    }


    var mProgram : ShaderProgram? = null
    var mRect: FloatVao? = null

    fun initial(ctx : Context) {
        mProgram = ShaderProgram(SourceLoader.loadSource(ctx!!, R.raw.simple_vertext_shader ),
            SourceLoader.loadSource(ctx!!, R.raw.simple_fragment_shader ))

        var rectPoint : FloatArray = floatArrayOf(
            // first triangle
            -0.5f, -0.5f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.0f, 0.0f,

            // second triangle
            -110.5f, -110.5f, 0.0f, 0.0f,
            110.5f, -110.5f, 0.0f, 0.0f,
            110.5f, 110.5f, 0.0f, 0.0f)
        mRect = FloatVao(rectPoint, 0, 4,4 * Float.SIZE_BYTES, 0)

        mProgram!!.active()
        mProgram!!.setUniform4f("u_color", 1.0f, 1.0f, 1.0f, 0.5f)
    }

    fun drawRect() {
        var linePoint : FloatArray = floatArrayOf(
            0.0f, 7.0f,
            9.0f, 7.0f)

        var startPoint : FloatArray = floatArrayOf(
            4.5f, 2.0f,
            4.5f, 12.0f)

        mProgram!!.active()
        mRect!!.active()

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6)

        checkGlError()
    }
}