package zkl.scienceFX.galaxy.fx


import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import zkl.scienceFX.galaxy.logic.Galaxy
import zkl.scienceFX.galaxy.logic.Star
import zkl.tools.math.Point2D
import zkl.tools.math.Point3D

abstract class GalaxyPainter<Canvas:Any> : Painter<Canvas, GalaxyPainterSource>() {
	override fun onInitialized() { }
	override fun paint() {
		paintBackground()
		
		paintCore()
		val iterator = source.galaxy.starsIterator
		while (iterator.hasNext()) {
			val star = iterator.next()
			paintStar(star)
		}
		
		paintForeground()
	}
	override fun onRelease() { }
	
	private fun paintCore() {
		drawBall3D(source.galaxyState3D.position, 10.0)
	}
	private fun paintStar(star: Star) {
		drawBall3D(source.galaxyState3D.getConvertedPosition3D(star.position), star.r)
	}
	private fun drawBall3D(absolutePosition: Point3D, r: Double) {
		val relative = source.camera.getRelativePosition(absolutePosition)
		if (relative.z > 0) {
			val pointWithScale = Visual3D.getScaleProjection(relative,source.camera.cameraH)
			drawCircle(pointWithScale, r * pointWithScale.scale)
		}
	}
	
	
	abstract fun paintBackground()
	abstract fun drawCircle(position: Point2D, r: Double)
	abstract fun paintForeground()
	
}
class GalaxyPainterSource(var galaxy: Galaxy, var galaxyState3D: VisualState3D){
	var camera: Camera3D = Camera3D(cameraH = 1200.0)
}


class GalaxyPainterFX : GalaxyPainter<GraphicsContext>() {
	override fun paintBackground() { }
	
	override fun drawCircle(position: Point2D, r: Double) {
		canvas.fill = Color.WHITE
		canvas.fillOval(position.x - r, position.y - r, 2*r, 2*r)
	}
	override fun paintForeground() { }
}