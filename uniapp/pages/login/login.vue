<template>
	<view class="main-container">
		<uni-easyinput v-model="mobilePhone" placeholder="请输入用户名"></uni-easyinput>
		<uni-easyinput v-model="password" placeholder="请输入密码"></uni-easyinput>
		<button @click="login()">登录</button>
	</view>
</template>

<script setup>
import { post } from '../../utils/request';
import {ref} from 'vue'
import crypto from 'crypto-js'

	const mobilePhone = ref("")
	const password = ref("")
	const login = () => {
		const request = {
			"mobilePhone": mobilePhone.value,
			"password": crypto.MD5(password.value).toString()
		}
		console.log(request);
		post("/login/password",request, (data) => {
			uni.navigateBack()
		})
	}
</script>

<style lang="scss">
	.main-container {
		margin: 10px;
	}
</style>
