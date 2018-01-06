package zkl.scienceFX.galaxy.fx


import zkl.tools.math.*


/**
 * 表示3D物体的位置和旋转（缩放、拉伸）状态
 */
class VisualState3D
@JvmOverloads constructor(
	var position: Point3D = InstantPoint3D(),
	conversion: Matrix = InstantMatrix.matrix3DUnit()) {
	
	var conversion: Matrix = conversion
		@Synchronized set(value) {
			mInverse = null
			field = value
		}
	
	private var mInverse: Matrix? = null
	val inverse: Matrix
		@Synchronized get() {
			if (mInverse == null) {
				mInverse = conversion.getInverseMatrix()
			}
			return mInverse!!
		}
	
	init {
		this.conversion = conversion
	}
	
	/**
	 * 计算相对坐标在世嘉变换后的绝对坐标
	 */
	fun getConvertedPosition3D(relativePosition: Point3D)
		= position.getOffset(conversion.transfer(relativePosition))
}

class ScalePoint2D(override var x: Double = 0.0, override var y: Double = 0.0, var scale: Double = 1.0) : InstantPoint2D(x, y)

object Visual3D {
	
	/**
	 * 摄像头距离 cameraH 的透视投影
	 */
	fun getProjection2D(position: Point3D, cameraH: Double = 1.0): InstantPoint2D {
		val scale = cameraH / position.z
		return InstantPoint2D(position.x * scale, position.y * scale)
	}
	
	/**
	 * cameraH为无穷远的投影
	 */
	fun getPlatProjection2D(position: Point3D): Point2D {
		return position
	}
	
	/**
	 * camera 视为在原点处，z轴正方向向前（z越大就越远）
	 * @param cameraH 摄像机位置到前方的底片的距离，用于计算不同远近物体投影后的大小
	 */
	fun getScaleProjection(position: Point3D, cameraH: Double): ScalePoint2D {
		val scale = cameraH / position.z
		return ScalePoint2D(position.x * scale, position.y * scale, scale)
	}
	
}


