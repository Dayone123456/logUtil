package com.hy.logutil

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.*

/**
 * java类作用描述
 * @className LogUtils
 * @author huye
 * @date 2021-10-22 14:45
 * @version V1.0.0  2021-10-22
 * @since [产品/模块版本]
 */
object Log {
    private val MIN_STACK_OFFSET = 3

    var defaultTag = "LogUtil"
    private val lineSeparator = System.getProperty("line.separator", "/n")

    val V = Log.VERBOSE
    val D = Log.DEBUG
    val I = Log.INFO
    val W = Log.WARN
    val E = Log.ERROR
    val A = Log.ASSERT

    private val TOP_BORDER =
        "╔═══════════════════════════════════════════════════════════════════════════════════════════════════"
    private val LEFT_BORDER = "║ "
    private val BOTTOM_BORDER =
        "╚═══════════════════════════════════════════════════════════════════════════════════════════════════"
    private val MAX_LEN = 1000
    var open = true

    /**
     * 显示的log等级
     */
    var levelLimit: Int = V

    //使用一个异常Handler来捕获异常信息CoroutineExceptionHandler协程的异常捕获Handler
    var handler = CoroutineExceptionHandler { _, exception ->
        //打印异常信息
        exception.printStackTrace()
        e("handler got $exception")
    }

    private fun processTagAndHead(): String {
        val elements = Thread.currentThread().stackTrace
        val offset = getStackOffset(elements)
        val targetElement = elements[offset]
        val head = Formatter()
            .format(
                "%s [%s(%s:%d)]",
                "In Thread: " + Thread.currentThread().name,
                targetElement.methodName,
                targetElement.fileName,
                targetElement.lineNumber
            )

        return head.toString()
    }

    private fun processMsgBody(msg: String, flag: Int, tag: String = defaultTag) {
        printTop(flag, tag)
        // 首先打印调用信息
        printLog(flag, tag)

        val lineCount = msg.length / MAX_LEN
        if (lineCount == 0) {
            printLog(flag, tag, msg)
        } else {
            var index = 0
            var i = 0
            while (true) {
                printLog(flag, tag, msg.substring(index, index + MAX_LEN))
                index += MAX_LEN
                if ((++i) >= lineCount)
                    break
            }
        }
        printBottom(flag, tag)
    }

    fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != Log::class.java.name) {
                return i
            }
            i++
        }
        return 2
    }

    /* 虽然 kotlin 有默认值这种操作，但是 Log.i(tag,msg) 这种比较符合平时的操作，所以还是提供类似的重载，
     * 而非 LogUtil.i(msg: String,tag: String = defaultTAG) 这种带默认值参数的方法 */

    fun v(msg: String) {
        v(defaultTag, msg)
    }

    fun i(msg: String) {
        i(defaultTag, msg)
    }

    fun d(msg: String) {
        d(defaultTag, msg)
    }

    fun w(msg: String) {
        w(defaultTag, msg)
    }

    fun e(msg: String) {
        e(defaultTag, msg)
    }

    fun v(tag: String, msg: String) {
        if (!isShow(V)) {
            return
        }
        processMsgBody(msg, V, tag)
    }

    fun i(tag: String, msg: String) {
        if (!isShow(I)) {
            return
        }
        processMsgBody(msg, I, tag)
    }

    fun d(tag: String, msg: String) {
        if (!isShow(D)) {
            return
        }
        processMsgBody(msg, D, tag)
    }

    fun w(tag: String, msg: String) {
        if (!isShow(W)) {
            return
        }
        processMsgBody(msg, W, tag)
    }

    fun e(tag: String, msg: String) {
        if (!isShow(E)) {
            return
        }
        processMsgBody(msg, E, tag)
    }

    fun printLog(flag: Int, tag: String, msg: String = processTagAndHead()) {
        Log.println(flag, tag, LEFT_BORDER + msg)
    }

    fun printBottom(flag: Int, tag: String) {
        Log.println(flag, tag, BOTTOM_BORDER)
    }

    fun printTop(flag: Int, tag: String) {
        Log.println(flag, tag, TOP_BORDER)
    }

    fun showLevel(level: Int) {
        levelLimit = level
    }

    private fun isShow(flag: Int): Boolean {
        return open && levelLimit <= flag
    }

    fun closeLog() {
        this.open = false
    }
}