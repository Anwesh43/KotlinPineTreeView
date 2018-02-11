package ui.anwesome.com.pinetreeview

/**
 * Created by anweshmishra on 11/02/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class PineTreeView(ctx: Context,var n:Int = 5) : View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class PineTree(var i:Int, var x: Float, var y: Float, var size: Float) {
        val state = PineTreeState()
        fun draw(canvas: Canvas, paint: Paint) {
            canvas.save()
            canvas.translate(x, y)
            for (i in 0..1) {
                canvas.save()
                canvas.rotate(60f * (i * 2 - 1) * state.scale)
                canvas.drawLine(0f, 0f, 0f, size, paint)
                canvas.restore()
            }
            canvas.restore()
        }

        fun update(stopcb: (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb: () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class PineTreeState(var scale:Float = 0f,var dir:Float = 0f, var prevScale:Float = 0f) {
        fun update(stopcb: (Float) -> Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb: () -> Unit) {
            if(dir == 0f) {
                dir = 1 - 2*scale
                startcb()
            }
        }
    }
    data class PineTreeContainer(var n:Int, var w:Float, var h:Float,var gap:Float = 0f) {
        val state = PineTreeContainerState(n)
        var pineTrees:ConcurrentLinkedQueue<PineTree> = ConcurrentLinkedQueue()
        init {
            var w_size = 0f
            for(i in 1..n) {
                w_size += (1f/i)
            }
            gap = w/w_size
            for(i in 1..n) {
                pineTrees.add(PineTree(i,w/2,3*h/4-gap - gap/i,gap/i))
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            pineTrees.forEach {
                it.draw(canvas,paint)
            }
            var y_end = 3*h/4 - gap
            canvas.drawLine(w/2, 3*h/4, w/2, y_end , paint)
            for(i in 1..n) {
                canvas.drawLine(w / 2, y_end, w/2, y_end-gap/i, paint)
                y_end -= gap/i
            }
        }
        fun update(stopcb: (Float) -> Unit) {
            state.executeCB { j ->
                pineTrees.at(j)?.update {
                    stopcb(it)
                    state.incrementCounter()
                }
            }
        }
        fun startUpdating(startcb: () -> Unit) {
            state.executeCB { j ->
                pineTrees.at(j)?.startUpdating(startcb)
            }
        }
    }
    data class PineTreeContainerState(var n:Int,var j:Int = 0,var dir:Int = 0) {
        fun incrementCounter() {
            j += dir
            if(j == n || j == -1) {
                dir *= -1
                j += dir
            }
        }
        fun executeCB(cb:(Int)->Unit) {
            cb(j)
        }
    }
    data class Animator(var view:PineTreeView, var animated:Boolean = false) {
        fun animate(updatecb: () -> Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = true
            }
        }
    }
}
data class Renderer(var view:PineTreeView, var time:Int = 0) {
    var container:PineTreeView.PineTreeContainer?=null
    val animator = PineTreeView.Animator(view)
    fun render(canvas:Canvas,paint:Paint) {
        if(time == 0) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            container = PineTreeView.PineTreeContainer(view.n, w, h)
        }
        canvas.drawColor(Color.parseColor("#212121"))
        container?.draw(canvas,paint)
        animator.animate {
            container?.update {
                animator.stop()
            }
        }
        time++
    }
    fun handleTap() {
        container?.startUpdating {
            animator.start()
        }
    }
}
fun ConcurrentLinkedQueue<PineTreeView.PineTree>.at(i:Int):PineTreeView.PineTree? {
    var j = 0
    forEach {
        if (j == i) {
            return it
        }
        j++
    }
    return null
}