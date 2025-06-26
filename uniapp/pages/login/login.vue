<template>
	<view class="main-container">
		<image src="@/static/logo.jpg" class="logo"></image>
		<uni-forms ref="former" class="former" :model="loginReq" :rules="rules" >
			<uni-forms-item name="mobilePhone" label="用户名">
				<uni-easyinput v-model="loginReq.mobilePhone" placeholder="请输入用户名"></uni-easyinput>
			</uni-forms-item>
			<uni-forms-item name="password" label="密码">
				<uni-easyinput v-model="loginReq.password" type="password" placeholder="请输入密码"></uni-easyinput>
			</uni-forms-item>
		</uni-forms>
		<button class="login-btn" @click="login()">登录</button>
	</view> 
</template>

<script setup>
	import {
		post
	} from '@/utils/request';
	import {
		ref
	} from 'vue'
	import crypto from 'crypto-js'

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
	const former = ref(null)
	const loginReq = ref({
		'mobilePhone': '',
		'password': ''
	})
	const login = () => {
		former.value.validate().then(res => {
			const request = {
				"mobilePhone": res.mobilePhone,
				"password": crypto.MD5(res.password).toString(),
				"uniPushCid": uni.getStorageSync('push-client-id')
			}
			post("/login/password", request, (data) => {
				uni.showToast({
					title: "登录成功！",
					icon: 'none'
				})
				uni.setStorageSync('satoken', data.data.token)
				uni.setStorageSync('userInfo', data.data)
				console.log(data.data);
				uni.switchTab({
					url: '/pages/index/index'
				});
				
			})
		}).catch(() => {
		})
	}
</script>

<style lang="scss">
	.main-container {
		margin: 10px;
		display: flex;
		flex-direction: column;
		align-items: center;

		.logo {
			width: 72px;
			height: 72px;

			margin: 33% 0 33% 0;
		}
		
		.former {
			width: 80%;
		}

		.login-btn {
			width: 70%;
			margin-top: 40px;
			background-color: lightblue;
			border: 0;
			border-radius: 20px;
		}
	}
</style>