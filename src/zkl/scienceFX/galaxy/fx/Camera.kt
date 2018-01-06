package zkl.scienceFX.galaxy.fx

import zkl.tools.math.Matrix
import zkl.tools.math.Point3D


class Camera3D(var visualState: VisualState3D = VisualState3D(), var cameraH:Double=100.0){
	/**
	 * 以摄像头的视野为准进行变换（而不是以标准坐标轴为基准）
	 */fun relativeConverse(conversion: Matrix) {
		visualState.conversion=conversion*visualState.conversion
	}
	/**
	 * 以摄像头的视野为准进行移动（而不是以标准坐标轴为基准），z轴朝里
	 */fun relativeMove(point: Point3D){
		visualState.position.offset(visualState.conversion.transfer(point))
	}
	
	fun getRelativePosition(point: Point3D): Point3D {
		val dPosition =point-visualState.position
		return visualState.inverse.transfer(dPosition)
	}
	fun getScaledProjection(point: Point3D): ScalePoint2D {
		return Visual3D.getScaleProjection(getRelativePosition(point), cameraH)
	}
}