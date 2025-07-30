import { toLoginPage } from "@/utils/check-login"

// var host = 'http://192.168.74.58:18888'
var host = 'http://www.fudges.cn:18888'
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
			response(res, successCallback, failCallback)
		},
		fail: (res) => {
			uni.showToast({
				icon: "error",
				title: '服务繁忙！'
			})
		},
		complete: completeCallback
	})
}

export function uploadFile(businessName, path, successCallback, failCallback) {
	uni.uploadFile({
		url: host + '/file/upload',
		filePath: path,
		name: 'files',
		timeout: 10000,
		formData: {
			'businessName': businessName
		}, 
		success(res) {
			res.data = JSON.parse(res.data)
			response(res, successCallback, failCallback)
		}, 
		fail() {
			failCallback()
		}
	})
}
export function uploadFileBase64(businessName, base64, successCallback, failCallback) {
	const request = {
		'businessName': businessName,
		'base64': base64
	}
	post('/file/upload/base64', request, successCallback, failCallback)
}

function response(res, successCallback, failCallback) {
	console.log(res);
	if(res.statusCode == 200) {
		if(res.data.code == "0000") {
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
		uni.showToast({
			icon: "error",
			title: '服务繁忙！'
		})
	}
}