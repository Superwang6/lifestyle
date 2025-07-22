export function checkLogin() {
	const userInfo = uni.getStorageSync('userInfo')
	if (!userInfo) {
		// 无登录缓存，跳登录页
		toLoginPage()
		return false
	} else {
		uni.reLaunch({
			url: '/pages/index/index'
		})
	}
	return true
}

let isRedirectLogin = false
export function toLoginPage(){
	if(isRedirectLogin) {
		return;
	}
	isRedirectLogin = true
	uni.reLaunch({
		url: '/pages/login/login',
		complete: () => {
			setTimeout(() => {
				isRedirectLogin = false
			}, 1000)
		}
	})
}