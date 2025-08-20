<template>
	<view class="main-container">
		<image src="@/static/logo.jpg" class="logo"></image>
		<view class="title">{{loginType == 0 ? '账号密码登录' : '手机号登录'}}</view>
		<view class="former">
			<uni-forms ref="former" v-if="loginType == 0" :model="loginReq" :rules="rules">
				<uni-forms-item name="account" label="账号">
					<uni-easyinput v-model="loginReq.account" placeholder="请输入账号"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item name="password" label="密码">
					<uni-easyinput v-model="loginReq.password" type="password" placeholder="请输入密码"></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
			<ls-mobile-code v-else class="former" ref="mobileCodeRef" business="loginCode"></ls-mobile-code>
		</view>
		<view class="btn login-btn" @click="login">登录</view>
		<view class="btn mobile-login-btn" v-if="settingsStore.mobileSupport" @click="changeLoginType">{{loginType == 0 ? '手机号登录' : '账号密码登录'}}</view>
		<view class="forget-password" v-if="settingsStore.mobileSupport" @click="forgetPassword">忘记密码了？</view>
		<view class="register">没有账号？<view class="go-register" @click="goRegister">去注册</view></view>
	</view>
</template>

<script setup>
	import {
		post
	} from '@/utils/request';
	import {
		ref,
		onMounted,
		getCurrentInstance
	} from 'vue'
	import crypto from 'crypto-js'
	import lsMobileCode from '@/components/common/ls-mobile-code.vue';
	import { useSettingsStore } from '@/stores/settings-store';

	const settingsStore = useSettingsStore()
	const rules = ref({
		'mobilePhone': {
			'rules': [{
				required: true,
				errorMessage: '请输入用户名'
			}, {
				minLength: 1,
				errorMessage: '请输入用户名'
			}]
		},
		'password': {
			'rules': [{
				required: true,
				errorMessage: '请输入密码'
			}, {
				minLength: 1,
				errorMessage: '请输入密码'
			}]
		}
	})
	const loginType = ref(0)
	const former = ref(null)
	const loginReq = ref({})
	const login = () => {
		if (loginType.value == 0) {
			former.value.validate().then(res => {
				const request = {
					"account": res.account,
					"password": crypto.MD5(res.password).toString(),
					"uniPushCid": uni.getStorageSync('push-client-id')
				}
				post("/login/password", request, (data) => {
					loginSuccess(data)
				})
			}).catch(() => {})
		} else {
			mobileCodeRef.value.validate().then(res => {
				const request = {
					"mobilePhone": res.mobilePhone,
					"code": res.code,
					"uniPushCid": uni.getStorageSync('push-client-id')
				}
				post("/login/mobilePhone", request, (data) => {
					loginSuccess(data)
				})
			}).catch(() => {})
		}
	}
	const loginSuccess = (data) => {
		uni.showToast({
			title: "登录成功！",
			icon: 'none'
		})
		uni.setStorageSync('satoken', data.data.token)
		post('/userBase/detail', {}, (data) => {
			uni.setStorageSync('userInfo', data.data)
		})
		uni.switchTab({
			url: '/pages/workbench/workbench'
		});
	}
	const changeLoginType = () => {
		if (loginType.value == 0) {
			loginType.value = 1
		} else {
			loginType.value = 0
		}
	}
	const mobileCodeRef = ref(null)

	const goRegister = () => {
		uni.navigateTo({
			url: '/pages/register/register'
		})
	}
	const forgetPassword = () => {
		uni.navigateTo({
			url: '/pages/login/forget-password'
		})
	}
	onMounted(() => {
		const instance = getCurrentInstance().proxy
		const eventChannel = instance.getOpenerEventChannel()
		if(eventChannel.on) {
			eventChannel.on('registerToLogin', (data) => {
				loginReq.value.account = data.account
				loginReq.value.password = data.password
			})
		}
	})
</script>

<style lang="scss" scoped>
	.main-container {
		display: flex;
		flex-direction: column;
		align-items: center;

		.logo {
			width: 150rpx;
			height: 150rpx;

			margin: 20% 0 20% 0;
		}

		.title {
			font-size: var(--font-size-llg);
			text-align: center;
			margin: 30px 0 30px 0;
			font-weight: bold;
		}

		.former {
			width: 80%;
		}

		.login-btn {
			width: 70%;
			margin-top: 50rpx;
		}

		.mobile-login-btn {
			width: 70%;
			margin-top: 25rpx;
			background-color: var(--bg-color);
		}

		.forget-password {
			margin-top: 45rpx;

			font-size: var(--font-size-sm);
			color: skyblue;
			font-weight: bold;
		}

		.register {
			position: absolute;
			bottom: 40rpx;

			font-size: var(--font-size-sm);
			color: var(--light-text-color);

			.go-register {
				display: inline;
				font-weight: bold;
				color: skyblue;
			}
		}



	}
</style>