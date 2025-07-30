<template>
	<view class="main-contenter">
		<view class="header">
			<view class="user-info" @click="openUserInfo">
				<image class="left" :src="settingsStore.filePrefix + userInfo.imgUrl" @click.stop="openImg(userInfo.imgUrl)"></image>
				<view class="mid">
					<view class="mid-top">{{userInfo.name}}</view>
					<view class="mid-bottom">手机号：{{userInfo.mobilePhone}}</view>
					<view v-if="userInfo.sign" class="description text-hidden">{{userInfo.sign}}</view>
				</view>
				<view class="right">
					<uni-icons type="right"></uni-icons>
				</view>
			</view>
			
		</view>
		<view class="body">
			<!-- <uni-group margin-top="20">
				<view> 分组内容 </view>
			</uni-group>
			<uni-group margin-top="20">
				<view> 分组内容 </view>
				<view> 分组内容 </view>
				<view> 分组内容 </view>
			</uni-group> -->
			<uni-group margin-top="20">
				<view class="setting" @click="settingClick">
					<uni-icons type="gear" class="setting-left" size="27"></uni-icons>
					<view class="setting-mid">设置</view>
					<uni-icons type="right" class="setting-right"></uni-icons>
				</view>
			</uni-group>
		</view>
		<ls-image-view ref="imgView"></ls-image-view>
	</view>
</template>

<script setup>
	import {
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

<style lang="scss">
	page {
		height: 100%;
	}

	.main-contenter {
		height: 100%;
		background-color: #EDEDED;
		display: flex;
		flex-direction: column;

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

			.setting {
				display: flex;
				flex-direction: row;
				align-items: center;

				.setting-left {}

				.setting-mid {
					flex: 1;
					margin: 0 10px 0 10px;
				}
			}
		}
	}
</style>