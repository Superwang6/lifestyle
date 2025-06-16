import { defineStore } from 'pinia'
import {
	ref
} from 'vue'
import { post } from '@/utils/request'

export const useJotStore = defineStore('jot-info', () => {
	const jotRequest = ref({
		'status': 0
	})
	
	function initJotRequest() {
		jotRequest.value = {
			'status': 0
		}
		const req = uni.getStorageSync('jot_more_filter')
		if (req) {
			jotRequest.value = req
		} else {
			const bookReq = {
				"pageNum": 1,
				"pageSize": 1
			}
			post('/jotBook/page', bookReq, (data) => {
				if(data.data[0]) {
					jotRequest.value.bookId = data.data[0].id
				}
			})
		}
		return jotRequest.value
	}
	return {jotRequest, initJotRequest}
})