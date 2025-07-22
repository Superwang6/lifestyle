<script>
	import {
		post
	} from '@/utils/request.js'
	import permision from '@/utils/permission.js'

	export default {
		onLaunch: function() {
			console.log('App Launch')
			// #ifdef APP-PLUS
			const isNotify = permision.checkSystemEnableNotification()
			if(isNotify) {
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
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	@import "@/styles/common.scss";
	/*每个页面公共css */
	#app {
		box-sizing: border-box;
		padding-top: var(--status-bar-height);

		position: fixed;
		top: 0;
		right: 0;
		left: 0;
		bottom: 0;

	}
</style>