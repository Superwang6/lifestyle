import { toLoginPage } from "@/utils/check-login"

var host = 'http://192.168.74.58:18888'
// var host = 'http://www.fudges.cn:18888'
export function post(url, req, successCallback, failCallback, completeCallback) {
	uni.request({
		url: host + url,
		data: req,
		method: "POST",
		timeout: 10000,
		header: {
			'Content-Type': 'application/json',
			'satoken': uni.getStorageSync('satoken')
		},
		success: (res) => {
			if(res.statusCode == 200) {
				if(res.data.code == "00000") {
					successCallback(res.data)
				} else if (res.data.code == "1001") {
					uni.removeStorageSync('userInfo');
					toLoginPage()
				} else {
					if(failCallback) {
						failCallback(res.data)
					} else {
						console.log("failCallback:" + JSON.stringify(res.data));
						uni.showToast({
							icon: "none",
							title: res.data.message
						})
					}
				}
			} else {
				console.log("fail,statusCode:" + res.statusCode);
				uni.showToast({
					icon: "error",
					title: '服务繁忙！'
				})
			}
		},
		fail: (res) => {
			console.log(res)
		},
		complete: completeCallback
	})
}