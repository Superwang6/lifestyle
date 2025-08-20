<template>
	<view class="ls-container" :style="{paddingTop: statusBarHeight + 'px',paddingBottom: windowButtom + 'px', height: pageHeight + 'px'}">
		<slot></slot>
	</view>
</template>

<script setup lang="ts">
	import { onMounted, ref } from "vue";

	const statusBarHeight = ref(0)
	const windowButtom = ref(0)
	const pageHeight = ref(0)
	
	onMounted(() => {
		const systemInfo = uni.getSystemInfoSync()
		statusBarHeight.value = systemInfo.statusBarHeight
		// #ifndef H5
		windowButtom.value = systemInfo.windowBottom
		// #endif
		pageHeight.value = systemInfo.windowHeight
	})
</script>

<style lang="scss" scoped>
	.ls-container {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
	}
</style>