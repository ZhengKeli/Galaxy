package zkl.scienceFX.galaxy.fx


import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.stage.Window
import zkl.scienceFX.galaxy.logic.Galaxy
import zkl.scienceFX.galaxy.logic.GalaxyConstructor
import zkl.tools.math.InstantMatrix
import zkl.tools.math.InstantPoint2D
import zkl.tools.math.InstantPoint3D


fun main(args: Array<String>) {
	Application.launch(GalaxyApplication::class.java, *args)
}
class GalaxyApplication:Application(){
	override fun start(stage: Stage) {
		val root: Pane = FXMLLoader.load(GalaxyController::class.java.getResource("galaxy.fxml"))
		stage.title = "galaxy"
		stage.scene = Scene(root, root.prefWidth, root.prefHeight)
		stage.show()
	}
}
class GalaxyController {
	private var stage: Window? = null
	@FXML private lateinit var canvas: Canvas
	private lateinit var galaxyPainterSource: GalaxyPainterSource
	fun onStartButtonClicked() {
		stage=canvas.scene.window
		canvas.requestFocus()
		
		val galaxy = Galaxy()
		GalaxyConstructor.constructGalaxy(galaxy)
		galaxyPainterSource = GalaxyPainterSource(galaxy, VisualState3D(InstantPoint3D(0.0, 0.0, 1000.0)))
		
		val period = 20
		object : Thread() {
			internal var time = 0
			internal var galaxyPainter = GalaxyPainterFX()
			override fun run() {
				galaxyPainter.initialize(canvas.graphicsContext2D, galaxyPainterSource)
				while (stage != null && stage!!.isShowing) {
					time++
					galaxy.process(period.toDouble() / 100)
					
					if(goingForward) {
						galaxyPainterSource.camera.relativeMove(InstantPoint3D(z = 1.0))
					}
					
					val graphicsContext = canvas.graphicsContext2D
					val width = canvas.width
					val height = canvas.height
					Platform.runLater {
						graphicsContext.fillRect(0.0, 0.0, width, height)
						graphicsContext.save()
						graphicsContext.translate(width / 2, height / 2)
						galaxyPainter.paint()
						graphicsContext.restore()
					}
					
					try {
						sleep(period.toLong())
					} catch (e: InterruptedException) {
						e.printStackTrace()
					}
					
				}
			}
		}.start()
	}
	
	
	private var rotateDragLastPoint: InstantPoint2D? = null
	private var moveDragLastPoint: InstantPoint2D? = null
	private var zoomDragLastPoint: InstantPoint2D? = null
	fun mouseDrag(event: MouseEvent) {
		when(event.button) {
			MouseButton.PRIMARY -> {
				if (rotateDragLastPoint == null) {
					rotateDragLastPoint = InstantPoint2D(event.x, event.y)
				} else {
					val dx = event.x - rotateDragLastPoint!!.x
					val dy = event.y - rotateDragLastPoint!!.y
					
					galaxyPainterSource.camera.relativeConverse(
						InstantMatrix.matrix3DRotateY(dx / 2 * Math.PI / 360) * InstantMatrix.matrix3DRotateX(dy / 2 * Math.PI / 360))
					
					rotateDragLastPoint!!.set(event.x, event.y)
				}
			}
			MouseButton.SECONDARY -> {
				if (moveDragLastPoint == null) {
					moveDragLastPoint = InstantPoint2D(event.x, event.y)
				} else {
					val dx = event.x - moveDragLastPoint!!.x
					val dy = event.y - moveDragLastPoint!!.y
					
					galaxyPainterSource.camera.relativeMove(InstantPoint3D(-dx, -dy, 0.0))
					
					moveDragLastPoint!!.set(event.x, event.y)
				}
			}
			MouseButton.MIDDLE -> {
				if (zoomDragLastPoint == null) {
					zoomDragLastPoint = InstantPoint2D(event.x, event.y)
				} else {
					//val dx = event.x - zoomDragLastPoint!!.x
					val dy = event.y - zoomDragLastPoint!!.y
					galaxyPainterSource.camera.relativeMove(InstantPoint3D(0.0, 0.0, dy))
					
					zoomDragLastPoint!!.set(event.x, event.y)
				}
			}
			else -> {}
		}
	}
	fun mouseRelease(event: MouseEvent) {
		if (event.button == MouseButton.PRIMARY) {
			rotateDragLastPoint = null
		} else if (event.button == MouseButton.SECONDARY) {
			moveDragLastPoint = null
		} else if (event.button == MouseButton.MIDDLE) {
			zoomDragLastPoint = null
		}
	}
	
	
	private var goingForward=false
	fun keyEvent(event: KeyEvent) {
		if(event.code== KeyCode.W){
			when(event.eventType){
				KeyEvent.KEY_PRESSED->{
					goingForward=true
				}
				KeyEvent.KEY_RELEASED->{
					goingForward=false
				}
			}
		}
	}
	
}
