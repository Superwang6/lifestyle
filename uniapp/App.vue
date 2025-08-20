<script setup>
	import {
		post
	} from '@/utils/request.js'
	import {
		onLaunch,
		onShow,
		onHide
	} from '@dcloudio/uni-app'
	import permision from '@/utils/permission.js'
	import {
		useSettingsStore
	} from '@/stores/settings-store'

	const settingsStore = useSettingsStore()
	onLaunch(() => {
		console.log('App Launch')
		// #ifdef APP-PLUS
		const isNotify = permision.checkSystemEnableNotification()
		if (isNotify) {
			uni.onPushMessage((res) => {
				console.log(res)
				uni.createPushMessage({
					content: res.data.content,
					success: () => {
						console.log('success');
					},
					fail: () => {
						console.log('fa');
					}
				})
			})
			uni.getPushClientId({
				success: (res) => {
					console.log(res.cid);
					uni.setStorageSync('push-client-id', res.cid)
				},
				fail(err) {
					console.log(err)
				}
			})
		}
		// #endif

		// 初始化配置
		settingsStore.initSettingsStore()
	})

	onShow(() => {
		console.log('App Show')
	})
	onHide(() => {
		console.log('App Hide')
	})
</script>

<style lang="scss">
	@import "@/styles/common.scss";

	/*每个页面公共css */
	// #app {
	// 	height: 100%;
	// }
	// page {
	// 	// padding-top: var(--status-bar-height);
	// 	height: 100vh;
	// }
</style>