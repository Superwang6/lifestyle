<template>
	<ls-container-nav title="账号注册">
		<view class="container">
			<uni-forms ref="former" class="former" :model="registerInfo" :rules="rules">
				<uni-forms-item name="account" label="账号">
					<uni-easyinput v-model="registerInfo.account" placeholder="请输入账号"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item name="password" label="密码">
					<uni-easyinput v-model="registerInfo.password" type="password" placeholder="请输入密码"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item name="confirmPassword" label="确认密码">
					<uni-easyinput v-model="registerInfo.confirmPassword" type="password"
						placeholder="请再次输入密码"></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
			<view class="btn" @click="registerAccount">注册</view>
			<view class="login">已经有账号了？<view class="go-login" @click="toLogin">去登录</view></view>
		</view>
	</ls-container-nav>
</template>

<script setup>
	import {
		post
	} from '@/utils/request';
	import {
		ref
	} from 'vue';
	import crypto from 'crypto-js'

	const registerInfo = ref({})
	const rules = ref({
		'account': {
			'rules': [{
				required: true,
				errorMessage: '请输入账号'
			}, {
				minLength: 1,
				errorMessage: '请输入账号'
			}]
		},
		'password': {
			'rules': [{
					required: true,
					errorMessage: '请输入密码'
				},
				{
					minLength: 1,
					errorMessage: '请输入密码'
				},
			]
		},
		'confirmPassword': {
			'rules': [{
					required: true,
					errorMessage: '请再次输入密码'
				},
				{
					minLength: 1,
					errorMessage: '请再次输入密码'
				},
				{
					validateFunction: (rule,value,data,callback) => {
						if(value == data.password) {
							return true
						} else {
							return false
						}
					},
					errorMessage: '两次密码输入不一致'
				}
			]
		}
	})
	const former = ref(null)
	const registerAccount = () => {
		former.value.validate().then(res => {
			const request = {
				"account": res.account,
				"password": crypto.MD5(res.password).toString(),
				"length": res.password.length
			}
			post("/register/userPassword", request, (data) => {
				uni.showToast({
					title: "注册成功！",
					icon: 'none'
				})
				uni.navigateTo({
					url: '/pages/login/login',
					success: (loginRes) => {
						loginRes.eventChannel.emit('registerToLogin', {
							account: res.account,
							password: res.password
						})
					}
				});
			})
		}).catch((e) => {
			console.log(e);
		})
	}
	const toLogin = () => {
		uni.navigateTo({
			url: '/pages/login/login'
		})
	}
</script>

<style lang="scss" scoped>
	.container {
		padding: 40rpx;
		
		.login {
			font-size: var(--font-size);
			color: var(--light-text-color);
			text-align: center;
		
			margin: 40rpx;
		
			.go-login {
				display: inline;
				font-weight: bold;
				color: skyblue;
			}
		}
	}
	
</style>