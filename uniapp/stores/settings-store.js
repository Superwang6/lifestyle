import {
	defineStore
} from 'pinia'
import {
	post
} from '@/utils/request.js'
import {
	ref
} from 'vue'

export const useSettingsStore = defineStore('settingsStore', () => {
	const domain = ref('')
	const filePrefix = ref('')

	function initSettingsStore() {
		post('/configure/index', {}, (data) => {
			domain.value = data.data.domain
			filePrefix.value = data.data.domain + data.data.filePrefix
		})
	}

	return {
		domain,
		filePrefix,
		initSettingsStore
	}
})