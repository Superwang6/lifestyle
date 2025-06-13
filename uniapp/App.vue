<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
			const userInfo = uni.getStorageSync('userInfo')
			if(!userInfo) {
				// 无登录缓存，跳登录页
				uni.navigateTo({
					url: '/pages/login/login'
				})
			}
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
</style>
