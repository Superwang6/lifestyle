<template>
	<view>
		<button @click="toLoginPage()">登录</button>
	</view>
</template>

<script setup>
	const toLoginPage = () => {
		uni.navigateTo({
			url: "/pages/login/login"
		})
	}
</script>

<style>

</style>
