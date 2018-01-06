package zkl.scienceFX.galaxy.logic

import zkl.tools.math.InstantPoint3D
import zkl.tools.math.MT


object GalaxyConstructor {
	fun constructGalaxy(galaxy: Galaxy) {
		for (i in 0..starsCount - 1) {
			val star = Star()
			star.w = setterW()
			star.distanceFromCore = setterR()
			val a = setterA(star.distanceFromCore)
			
			star.position = InstantPoint3D(star.distanceFromCore * Math.cos(a), star.distanceFromCore * Math.sin(a), setterH(star.distanceFromCore))
			
			star.r = starR
			galaxy.addStar(star)
		}
	}
	
	/** 星星总数 */
	private val starsCount:Long = 10000
	/** 星星半径 */
	private val starR:Double = 1.0
	
	
	/** 银河大半径 */
	private val galaxyR = 500.0
	/** 银河核心半径 */
	private val coreR = 10.0
	
	/** 银河最高处高度 */
	private val maxH = 50.0
	/** 旋臂数量 */
	private val arms = 4
	/** 旋臂旋转的角度 */
	private val maxDA = 2 * Math.PI
	/** 旋臂的最大宽度 */
	private val maxRanA = 2 * Math.PI / arms * 0.4
	
	
	/** 设置星星距离中心的距离 */
	private fun setterR(): Double {
		return coreR + (galaxyR - coreR) * Math.pow(Math.random(), 1.7)
	}
	/** 设置星星到星盘的高度距离 */
	private fun setterH(r: Double): Double {
		return MT.randomGaussianDistribution((maxH /3)*(1 - r / galaxyR))
	}
	/** 设置星星在星盘上的角度位置 */
	private fun setterA(r: Double): Double {
		val k = r / galaxyR
		/** 在某旋臂上，该旋臂的初始角度 */
		val armA = Math.floor(Math.random() * arms) * (Math.PI * 2 / arms)
		/** 旋臂随着半径增加而旋转产生的角度 */
		val dA = k * maxDA
		return armA - dA + MT.randomGaussianDistribution((maxRanA /3)*Math.pow(k,-0.4))
	}
	/** 设置星星转动的角速度 */
	private fun setterW(): Double {
		return Math.PI * 2 / 360 * 1
	}
	
	
}
