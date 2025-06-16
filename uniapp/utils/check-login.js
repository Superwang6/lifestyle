export function checkLogin() {
	const userInfo = uni.getStorageSync('userInfo')
	if (!userInfo) {
		// 无登录缓存，跳登录页
		uni.navigateTo({
			url: '/pages/login/login'
		})
		return false
	}
	return true
}