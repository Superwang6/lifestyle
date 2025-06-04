var host = 'http://localhost:8080'
export function post(url, req, successCallback, failCallback, completeCallback) {
	uni.request({
		url: host + url,
		data: req,
		method: "POST",
		timeout: 10000,
		header: {
			'Content-Type': 'application/json'
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
					}
				}
			} else {
				console.log("fail,statusCode:" + res.statusCode);
			}
		},
		fail: (res) => {
			console.log(res)
		},
		complete: completeCallback
	})
}