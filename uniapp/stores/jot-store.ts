import { defineStore } from 'pinia'
import {
	ref
} from 'vue'
import { post } from '@/utils/request'
interface JotRequest {
	status : number,
	bookId ?: number,
	pageNum: number,
	pageSize: number,
	search?: string
}

export const useJotStore = defineStore('jot-info', () => {
	const jotRequest = ref<JotRequest>({
		status: 0,
		pageNum: 1,
		pageSize: 30
	})
	const bookList = ref([])

	function initJotRequest() {
		jotRequest.value.status = 0
		const req = uni.getStorageSync('jot_more_filter')
		if (req) {
			jotRequest.value = req
		} else {
			queryJotBookList(() => {
				if (bookList.value[0]) {
					jotRequest.value.bookId = bookList.value[0].id
				}
			})
		}
		return jotRequest.value
	}
	function queryJotBookList(successCallback:any, failCallback?:any) {
		console.log("query jot book");
		const request = {
			"pageNum": 1,
			"pageSize": 10000
		}
		post('/jotBook/page', request, (data : any) => {
			bookList.value = []
			bookList.value.push(...data.data)
			console.log(bookList.value);
			successCallback()
		}, () => {
			failCallback()
		})
	}
	return { jotRequest, initJotRequest, bookList, queryJotBookList }
})