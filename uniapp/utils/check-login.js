export function checkLogin() {
	const userInfo = uni.getStorageSync('userInfo')
	if (!userInfo) {
		// 无登录缓存，跳登录页
		uni.reLaunch({
			url: '/pages/login/login'
		})
		return false
	} else {
		uni.reLaunch({
			url: '/pages/index/index'
		})
	}
	return true
}