<template>
	<ls-container-nav title="手机号绑定">
		<view class="container">
			<view class="info">已绑定手机号</view>
			<view class="mobile">{{mobile}}</view>
			<view class="tips">绑定手机号将作为您身份验证的重要方式，请谨慎操作！</view>
			<view class="btn modify-mobile" @click="openMobileChange">更换手机号</view>
		</view>
	</ls-container-nav>
</template>

<script setup>
import { ref ,onMounted} from 'vue'
import { post } from '@/utils/request'
	const mobile = ref('')
	
	const openMobileChange = () => {
		uni.navigateTo({
			url: '/pages/settings/mobile-code',
			events: {
				'on-refresh-index': (data) => {
					queryUser()
				}
			}
		})
	}
	const queryUser = () => {
		post('/userBase/detail', {}, (data) => {
			mobile.value = data.data.mobilePhone
		})
	}
	onMounted(() => {
		queryUser()
	})
</script>

<style lang="scss" scoped>
	.container {
		align-items: center;
		
		.info {
			font-size: var(--font-size-sm);
			color: var(--light-text-color);
			margin: 20rpx 0 10rpx 0;
		}
		.mobile {
			font-size: var(--font-size-llg);
			font-weight: bold;
			margin: 10rpx 0 30rpx 0;
		}
		.tips {
			font-size: var(--font-size-ssm);
			color: var(--light-text-color);
		}
		.modify-mobile {
			margin: 100rpx;
			width: 70%;
		}
	}
</style>