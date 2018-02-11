package ui.anwesome.com.pinetreeview

/**
 * Created by anweshmishra on 11/02/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class PineTreeView(ctx: Context) : View(ctx) {
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

    data class PineTree(var x: Float, var y: Float, var size: Float) {
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
    data class PineTreeContainer(var n:Int, var w:Float, var h:Float) {
        var pineTrees:ConcurrentLinkedQueue<PineTree> = ConcurrentLinkedQueue()
        fun draw(canvas:Canvas,paint:Paint) {
            pineTrees.forEach {
                it.draw(canvas,paint)
            }
        }
        fun update(stopcb: (Float) -> Unit) {

        }
        fun startUpdating(startcb: () -> Unit) {

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