<template>
	<ls-container-nav title="设置密码">
		<view class="container">
			<uni-forms ref="former" :rules="rules" :model="password">
				<uni-forms-item label="用户名" name="account">
					<uni-easyinput class="input" value="123" disabled :styles="{disableColor: 'var(--bg-color)'}"
						:input-border="false"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="原密码" name="oldPassword">
					<uni-easyinput placeholder="请输入原密码，没有可不填" v-model="password.oldPassword"
						type="password"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="新密码" name="password">
					<uni-easyinput placeholder="请输入新密码" v-model="password.password" type="password"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="确认密码" name="confirmPassword">
					<uni-easyinput placeholder="请输入确认密码" v-model="password.confirmPassword"
						type="password"></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
			<view class="btn" @click="modifyPassword">确认</view>
			<view class="forget-password" v-if="settingsStore.mobileSupport" @click="forgetPassword">忘记密码了？</view>
		</view>
	</ls-container-nav>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import crypto from 'crypto-js';
	import {
		post
	} from '@/utils/request';
	import {
		useSettingsStore
	} from '@/stores/settings-store';
	const settingsStore = useSettingsStore()
	const former = ref(null)
	const rules = ref({
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
					validateFunction: (rule, value, data, callback) => {
						if (value == data.password) {
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
	const password = ref({})

	const modifyPassword = () => {
		former.value.validate().then(res => {
			const request = {
				'oldPassword': res.oldPassword,
				'password': crypto.MD5(res.password).toString(),
				'length': res.password.length
			}
			post('/userPassword/modifyPassword', request, () => {
				uni.showToast({
					icon: 'none',
					title: '修改成功'
				})
				uni.navigateBack()
			})
		}).catch(e => {
			console.log(e);
		})
	}
	const forgetPassword = () => {
		post('/userBase/mobilePhone', {}, (data) => {
			uni.navigateTo({
				url: '/pages/login/forget-password',
				success: (res) => {
					res.eventChannel.emit('acceptFullMobilePhone', {mobilePhone: data.data})
				}
			})
		})
	}
	
</script>

<style lang="scss" scoped>
	.container {
		padding: 40rpx;

		.btn {
			margin: 50rpx 0;
		}
		.forget-password {
			text-align: center;
			margin-top: 45rpx;
		
			font-size: var(--font-size-sm);
			color: skyblue;
			font-weight: bold;
		}
	}
</style>