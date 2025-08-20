<template>
	<ls-container-nav title="忘记密码">
		<view class="container">
			<ls-mobile-code ref="mobileCodeRef" business="forgetPasswordCode" :mobile="mobile"></ls-mobile-code>
			<uni-forms ref="former" class="former" :model="info" :rules="rules">
				<uni-forms-item name="password" label="密码">
					<uni-easyinput v-model="info.password" type="password" placeholder="请输入密码"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item name="confirmPassword" label="确认密码">
					<uni-easyinput v-model="info.confirmPassword" type="password" placeholder="请再次输入密码"></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
			<view class="btn" @click="forgetPassword">确认</view>
		</view>
	</ls-container-nav>
</template>

<script setup>
	import {
		getCurrentInstance,
		onMounted,
		ref
	} from 'vue';
	import {onLoad} from '@dcloudio/uni-app'
	import {
		post
	} from '@/utils/request';
	import crypto from 'crypto-js'
	import lsMobileCode from '@/components/common/ls-mobile-code.vue';

	const back = () => {
		uni.navigateBack()
	}
	const eventChannel = getCurrentInstance().proxy.getOpenerEventChannel()
	const former = ref(null)
	const info = ref({})
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
	const mobileCodeRef = ref(null)
	const forgetPassword = () => {
		mobileCodeRef.value.validate().then(codeRes => {
			former.value.validate().then(res => {
				const request = {
					'mobilePhone': codeRes.mobilePhone,
					'code': codeRes.code,
					'password': crypto.MD5(res.password).toString(),
					'length': res.password.length
				}
				post('/userPassword/forgetPassword', request, (data) => {
					uni.showToast({
						icon: 'none',
						title: '改密成功'
					})
					back()
				})
			}).catch(e => {
				console.log(e);
			})
		}).catch(e => {
			console.log(e);
		})
	}
	const mobile = ref('')
	onMounted(() => {
		eventChannel.on('acceptFullMobilePhone', (data) => {
			mobile.value = data.mobilePhone
		})
	})
</script>

<style lang="scss" scoped>
	.container {
		padding: 20px;
		
		.mobile-code {
			display: flex;
			flex-direction: row;
			align-items: center;
			height: 100%;
			.right {
				margin-left: 10rpx;
				padding: 0 20rpx 0 20rpx;
				height: 100%;
				text-align: center;
				border-radius: var(--item-radius);
				display: flex;
				flex-direction: column;
				justify-content: center;
			}
			.get-code {
				background-color: var(--primary-color);
			}
			.left-second {
				background-color: var(--bg-color);
			}
		}
	}
</style>