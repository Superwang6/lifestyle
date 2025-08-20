<template>
	<uni-forms ref="mobileFormer" :rules="rules" :model="mobileInfo">
		<uni-forms-item label="手机号" name="mobilePhone">
			<uni-easyinput placeholder="请输入手机号" v-model="mobileInfo.mobilePhone"></uni-easyinput>
		</uni-forms-item>
		<uni-forms-item label="验证码" name="code">
			<view class="mobile-code">
				<uni-easyinput v-model="mobileInfo.code" placeholder="请输入验证码"></uni-easyinput>
				<view v-if="leftSecond > 0" class="right left-second">{{leftSecond}}秒</view>
				<view v-else class="right get-code" @click="getCode">{{getCodeText}}</view>
			</view>
		</uni-forms-item>
	</uni-forms>
</template>

<script setup lang="ts">
	import {
		computed,
		onMounted,
		reactive,
		ref
	} from 'vue'
	import {
		post
	} from '@/utils/request'

	const props = defineProps<{
		mobile?:string,
		business: string
	}>()
	const mobileInfo = reactive({
		mobilePhone: computed(() => props.mobile),
		code: ''
	})
	const mobileFormer = ref(null)
	const rules = ref({
		'mobilePhone': {
			'rules': [{
				required: true,
				errorMessage: '请输入手机号'
			}, {
				minLength: 11,
				maxLength: 11,
				errorMessage: '请输入正确的手机号'
			}]
		},
		'code': {
			'rules': [{
				required: true,
				errorMessage: '请输入验证码'
			}, {
				minLength: 6,
				maxLength: 6,
				errorMessage: '请输入正确的验证码'
			}]
		}
	})
	const getCodeText = ref('获取验证码')
	const leftSecond = ref(null)
	let timer = null
	const getCode = () => {
		mobileFormer.value.validateField(['mobilePhone']).then((res:any) => {
			const request = {
				'mobilePhone': res.mobilePhone,
				'business': props.business
			}
			post('/sms/send/code', request, () => {
				uni.showToast({
					icon: 'none',
					title: '验证码发送成功！'
				})
				leftSecond.value = 60
				timer = setInterval(() => {
					leftSecond.value--;

					if (leftSecond.value <= 0) {
						clearInterval(timer);
						getCodeText.value = '获取验证码'
						leftSecond.value = null
					}
				}, 1000)
			})
		}).catch((e:any) => {
			console.log(e);
		})
	}
	
	const validate = () => {
		return mobileFormer.value.validate()
	}
	defineExpose({
		validate
	})
</script>

<style lang="scss" scoped>
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
</style>