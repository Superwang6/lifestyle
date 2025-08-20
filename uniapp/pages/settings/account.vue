<template>
	<ls-container-nav title="账号管理">
		<view class="container">
			<view class="group">
				<view class="item" @click="openAccountChange">
					<view class="item-left">用户名</view>
					<view class="item-right">
						<view class="info">{{user.account}}</view>
						<uni-icons type="right" color="var(--light-text-color)"></uni-icons>
					</view>
				</view>
				<view v-if="settingsStore.mobileSupport" class="divide"></view>
				<view v-if="settingsStore.mobileSupport" class="item" @click="openMobileBind">
					<view class="item-left">绑定手机</view>
					<view class="item-right">
						<view class="info">{{user.mobilePhone}}</view>
						<uni-icons type="right" color="var(--light-text-color)"></uni-icons>
					</view>
				</view>
				<view class="divide"></view>
				<view class="item" @click="openPasswordSetting">
					<view class="item-left">登录密码</view>
					<view class="item-right">
						<view class="info">已设置</view>
						<uni-icons type="right" color="var(--light-text-color)"></uni-icons>
					</view>
				</view>
			</view>
		</view>
	</ls-container-nav>
</template>

<script setup>
	import {
		ref,
		onMounted
	} from 'vue'
	import {
		post
	} from '@/utils/request'
	import lsDialog from '@/components/common/ls-dialog.vue'
	import lsMobileCode from '@/components/common/ls-mobile-code.vue'
	import { useSettingsStore } from '@/stores/settings-store'

	const settingsStore = useSettingsStore()
	const user = ref({})

	const openAccountChange = () => {
		uni.navigateTo({
			url: '/pages/settings/account-change'
		})
	}
	const openMobileBind = () => {
		uni.navigateTo({
			url: '/pages/settings/mobile-bind'
		})
	}
	const openPasswordSetting = () => {
		uni.navigateTo({
			url: '/pages/settings/password-setting'
		})
	}
	const back = () => {
		uni.navigateBack()
	}

	const queryUser = () => {
		post('/userBase/detail', {}, (data) => {
			user.value = data.data
		})
	}
	onMounted(() => {
		queryUser()
	})
</script>

<style lang="scss" scoped>
	.container {
		padding: 20rpx;
		
		.group {
			padding: 20rpx;
			border-radius: var(--item-radius);
			background-color: var(--light-bg-color);

			display: flex;
			flex-direction: column;

			.divide {
				height: 5rpx;
				background-color: #fbfbfb;
			}

			.item {
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: center;
				margin: 15rpx 10rpx;

				.item-right {
					display: flex;
					flex-direction: row;
					align-items: center;
					color: var(--light-text-color) !important;

					.info {
						margin-right: 20rpx;
					}
				}
			}
		}
	}
</style>