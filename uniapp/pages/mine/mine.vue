<template>
	<ls-container>
		<view class="container">
			<view class="header">
				<view class="user-info" @click="openUserInfo">
					<image class="left" :src="imgUrl" @click.stop="openImg(imgUrl)"></image>
					<view class="mid">
						<view class="mid-top">{{userInfo.name}}</view>
						<view v-if="userInfo.mobilePhone && useSettingsStore.mobileSupport" class="mid-bottom">手机号：{{userInfo.mobilePhone}}</view>
						<view v-else class="mid-bottom">账号：{{userInfo.account}}</view>
						<view v-if="userInfo.sign" class="description text-hidden">{{userInfo.sign}}</view>
					</view>
					<view class="right">
						<uni-icons type="right"></uni-icons>
					</view>
				</view>
				
			</view>
			<view class="body">
				<view class="group">
					<view class="item" @click="settingClick">
						<uni-icons type="gear" class="item-left" size="27"></uni-icons>
						<view class="item-mid">设置</view>
						<uni-icons type="right" class="item-right"></uni-icons>
					</view>
				</view>
			</view>
			<ls-image-view ref="imgView"></ls-image-view>
		</view>
	</ls-container>
</template>

<script setup>
	import {
		computed,
		onMounted,
		ref
	} from 'vue';
	import {
		post
	} from '@/utils/request';
	import {
		onShow
	} from '@dcloudio/uni-app'
	import lsImageView from '@/components/common/ls-image-view.vue';
	import { useSettingsStore } from '@/stores/settings-store';

	const settingsStore = useSettingsStore()
	const userInfo = ref({})

	const settingClick = () => {
		uni.navigateTo({
			url: '/pages/settings/settings'
		})
	}
	
	const imgView = ref(null)
	const imgUrl = computed(() => {
		return userInfo.imgUrl ? settingsStore.filePrefix + userInfo.imgUrl : '/static/logo.jpg'
	})
	const openImg = (imgUrl) => {
		imgView.value.openImg(imgUrl)
	}
	const openUserInfo = () => {
		uni.navigateTo({
			url: '/pages/mine/user-info'
		})
	}

	onShow(() => {
		userInfo.value = uni.getStorageSync('userInfo')
	})
</script>

<style lang="scss" scoped>
	.container {
		.header {
			background-color: #FFFFFF;
			display: flex;
			flex-direction: column;
			padding: 20px;

			.user-info {
				display: flex;
				flex-direction: row;

				.left {
					width: 72px;
					height: 72px;
					margin-right: 10px;
				}

				.mid {
					flex: 1;
					width: 0vw;

					display: flex;
					flex-direction: column;
					justify-content: space-around;

					.mid-top {
						font-size: 20px;
						font-weight: bold;
					}

					.mid-bottom {
						font-size: 12px;
						color: #828282;
					}
					.description {
						margin-top: 5px;
						font-size: 12px;
					}
				}

				.right {
					display: flex;
					flex-direction: column;
					justify-content: center;
				}
			}
		}

		.body {
			.group {
				margin-top: 20rpx;
				background-color: var(--light-bg-color);
				padding: 10rpx;
				
				.item {
					display: flex;
					flex-direction: row;
					align-items: center;
					height: 70rpx;
				
					.item-mid {
						flex: 1;
						margin: 0 10px 0 10px;
					}
				}
			}

			
		}
	}
</style>