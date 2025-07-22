<template>
	<view class="content">
		正在加载。。。
	</view>
</template>

<script setup>
	import {
		onShow
	} from '@dcloudio/uni-app'
	import {
		checkLogin
	} from '@/utils/check-login';
	
	onShow(() => {
		checkLogin()
	})
</script>

<style lang="scss">
.content {
	background-color: lightsteelblue;
}
</style>
