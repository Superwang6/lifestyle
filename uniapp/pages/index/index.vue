<template>
	<view class="test">1111</view>
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

<style>
	page {
		overflow: hidden;
	}

	.test {
		background-color: aqua;
	}
</style>