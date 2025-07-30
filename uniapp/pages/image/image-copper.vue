<template>
	<uni-icons type="left" class="back" size="30" @click="back"></uni-icons>
	<ls-image-cropper ref="cropper" :radius="500" @crop="getTempFileUrl"></ls-image-cropper>
</template>

<script setup>
	import {
		onMounted,
		ref,
		getCurrentInstance
	} from 'vue';
	import lsImageCropper from '@/components/ls-image-cropper/ls-image-cropper.vue'
	import {
		post,
		uploadFile,
		uploadFileBase64
	} from '@/utils/request';

	const {
		proxy
	} = getCurrentInstance()

	const cropper = ref(null)
	const getTempFileUrl = (data) => {
		// #ifdef H5
		uploadFileBase64('userAvatar', data.tempFilePath, (data) => {
			uni.showToast({
				icon: 'none',
				title: '上传成功'
			})
			back(data.data[0].filePath)
		})
		// #endif
		// #ifndef H5
		uploadFile('userAvatar', data.tempFilePath, (data) => {
			uni.showToast({
				icon: 'none',
				title: '上传成功'
			})
			back(data.data[0].filePath)
		})
		// #endif
	}
	const back = (imgUrl) => {
		const eventChannel = proxy.getOpenerEventChannel();
		if(imgUrl) {
			eventChannel.emit('returnImageUrl', {
				'imgUrl': imgUrl
			})
		}
		uni.navigateBack()
	}

	onMounted(() => {
		cropper.value.chooseImage({
			sourceType: ['album']
		})
	})
</script>

<style lang="scss">
	.back {
		position: relative;
		z-index: 100;
		color: white !important;
		top: 30rpx;
		left: 30rpx
	}
</style>