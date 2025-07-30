<template>
	<uni-nav-bar left-icon="left" title="个人资料" right-text="保存" :border="false"
		@clickLeft="back" @clickRight="saveUserInfo"></uni-nav-bar>
	<view class="content">
		<view class="avatar" @click="openPhoto">
			<image class="img" :src="user.imgUrl ? settingsStore.filePrefix + user.imgUrl : '/static/logo.png'"></image>
			<uni-icons class="icon" type="camera-filled" size="23"></uni-icons>
		</view>
		<view class="base-info">
			<view class="line-item">
				<view class="line-left">手机号</view>
				<uni-easyinput class="line-right" :value="user.mobilePhone" :inputBorder="false" :clearable="false"
					disabled :styles="{disableColor: '#fff'}"></uni-easyinput>
			</view>
			<view class="line-item">
				<view class="line-left">用户名</view>
				<uni-easyinput class="line-right" v-model="user.name" :inputBorder="false"
					:clearable="false"></uni-easyinput>
			</view>
		</view>
		<view class="base-info">
			<view class="line-item" @click="chooseSex">
				<view class="line-left">性别</view>
				<view class="line-right text">
					{{sexFormat(user.sex)}}
					<uni-icons class="right-icon" type="right"></uni-icons>
				</view>
			</view>
			<view class="line-item" @click="chooseBirthday">
				<view class="line-left">生日</view>
				<view class="line-right text">
					{{birthdayFormat(user.birthday)}}
					<uni-icons class="right-icon" type="right"></uni-icons>
				</view>
			</view>
			<view class="line-item">
				<view class="line-left">签名</view>
				<uni-easyinput class="line-right" v-model="user.sign" :inputBorder="false"
					:clearable="false"></uni-easyinput>
			</view>
		</view>
	</view>
	<ls-choose-popup ref="sexPopup" title="选择性别"></ls-choose-popup>
	<ls-datetime-popup ref="birthdayPopup" :mode="1|2|4"></ls-datetime-popup>
</template>

<script setup>
	import {
		onBeforeMount,
		onMounted,
		ref
	} from 'vue';
	import {
		post
	} from '@/utils/request';
	import lsChoosePopup from '@/components/common/ls-choose-popup.vue';
	import lsDatetimePopup from '@/components/common/ls-datetime-popup.vue';
	import dayjs from 'dayjs'
	import { useSettingsStore } from '@/stores/settings-store';

	const settingsStore = useSettingsStore()
	const user = ref({})

	const back = () => {
		uni.navigateBack()
	}

	const sexPopup = ref(null)
	const sexFormat = (sex) => {
		return sex == 1 ? '男' : (sex == 2 ? '女' : '未知')
	}
	const chooseSex = () => {
		sexPopup.value.open(
			[{
					'value': 0,
					'name': '未知'
				},
				{
					'value': 1,
					'name': '男'
				},
				{
					'value': 2,
					'name': '女'
				}
			]).then((item) => {
				user.value.sex = item.value
			}).catch(() => {})
	}
	
	const birthdayPopup = ref(null)
	const birthdayFormat = (birthday) => {
		return dayjs(birthday).format('YYYY-MM-DD')
	}
	const chooseBirthday = () => {
		birthdayPopup.value.open().then((date) => {
			user.value.birthday = dayjs(date).format('YYYY-MM-DD 00:00:00')
		})
	}
	const saveUserInfo = () => {
		const request = {
			id: user.value.id,
			name: user.value.name,
			sex: user.value.sex,
			birthday: user.value.birthday,
			sign: user.value.sign,
			imgUrl: user.value.imgUrl
		}
		post('/userBase/modify', request, (res) => {
			uni.showToast({
				icon: 'none',
				title: '修改成功'
			})
			post('/userBase/detail/' + user.value.id, {} ,(data) => {
				uni.setStorageSync('userInfo', data.data)
				back()
			})
		})
	}
	
	const openPhoto = () => {
		uni.navigateTo({
			url: '/pages/image/image-copper',
			events: {
				returnImageUrl: (data) => {
					user.value.imgUrl = data.imgUrl
				}
			}
		})
	}

	onMounted(() => {
		const userInfo = uni.getStorageSync('userInfo')
		post('/userBase/detail/' + userInfo.id, {}, (data) => {
			user.value = data.data
		})
	})
</script>

<style lang="scss" scoped>
	.content {
		padding-top: 100rpx;

		display: flex;
		flex-direction: column;
		align-items: center;
		background-color: var(--bg-color);
		height: 100vh;

		.avatar {
			position: relative;
			
			.img {
				position: relative;
				width: 200rpx;
				height: 200rpx;
				border-radius: 100rpx;
				border: 3px white solid;
			}

			.icon {
				border: 5rpx white solid;
				position: absolute;
				padding: 5rpx;
				background-color: var(--primary-color);
				border-radius: 35rpx;
				left: calc(200rpx - 61rpx);
				top: calc(200rpx - 61rpx);
			}
		}

		.base-info {
			width: 75vw;
			margin-top: 30rpx;
			padding: 20rpx 30rpx 20rpx 30rpx;
			border-radius: var(--item-radius);
			background-color: var(--light-bg-color);

			.line-item {
				display: flex;
				flex-direction: row;
				align-items: center;
				font-size: var(--font-size);
				margin: 5rpx;
				height: 70rpx;

				.line-left {
					flex: 1;
					color: var(--light-text-color);
				}

				.line-right {
					flex: 3
				}

				.text {
					padding: 0 0 0 10px;

					display: flex;
					flex-direction: row;
					justify-content: space-between;

					.right-icon {
						color: var(--light-text-color) !important;
					}
				}
			}
		}

	}
</style>