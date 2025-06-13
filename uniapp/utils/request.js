var host = 'http://localhost:8080'
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
				} else {
					if(failCallback) {
						failCallback(res.data)
					} else {
						console.log("failCallback:" + JSON.stringify(res.data));
						uni.showToast({
							icon: "none",
							title: '请求失败！'
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