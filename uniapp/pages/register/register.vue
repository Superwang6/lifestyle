<template>
	<view class="content">
		<view class="title">账号注册</view>
		<uni-forms ref="former" class="former" :model="registerInfo" :rules="rules">
			<uni-forms-item name="mobilePhone" label="手机号">
				<uni-easyinput v-model="registerInfo.mobilePhone" placeholder="请输入手机号"></uni-easyinput>
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
		<view class="login">已经有账号了？<span @click="toLogin">去登录</span></view>
	</view>
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
						console.log(value);
						console.log(data);
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
				"mobilePhone": res.mobilePhone,
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
							mobilePhone: res.mobilePhone,
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
	.content {
		margin: 20px;

		.title {
			font-size: var(--font-size-llg);
			text-align: center;
			margin: 30px 0 30px 0;
			font-weight: bold;
		}

		.login {
			font-size: var(--font-size);
			color: var(--light-text-color);
			text-align: center;

			margin: 20px;

			span {
				font-weight: bold;
				color: skyblue;
			}
		}

	}
</style>