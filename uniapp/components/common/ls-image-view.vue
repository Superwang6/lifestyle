<template>
	<uni-popup ref="popup" type="center" :is-mask-click="true">
		<view class="content">
			<uni-icons class="close" type="closeempty" size="30" @click="closeImg"></uni-icons>
			<image mode="aspectFit" :src="url"></image>
		</view>
	</uni-popup>
</template>

<script setup>
	import { ref } from 'vue';
	import { useSettingsStore } from '@/stores/settings-store';
	
	const settingsStore = useSettingsStore()
	
	const popup = ref(null)
	const url = ref(null)
	
	const openImg = (imgUrl) => {
		url.value = settingsStore.filePrefix + imgUrl
		popup.value.open()
	}
	const closeImg = () => {
		popup.value.close()
	}
	defineExpose({
		openImg
	})
</script>

<style lang="scss" scoped>
	.content {
		width: 100vw;
		height: 100vh;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		
		.close {
			position: absolute;
			right: 40rpx;
			top: 40rpx;
			background-color: var(--light-bg-color);
			border-radius: 40rpx;
			padding: 4rpx;
		}
		
	}
	
</style>