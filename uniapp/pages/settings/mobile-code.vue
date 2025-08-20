<template>
	<ls-container-nav title="换绑手机">
		<view class="content">
			<view class="title">请输入新手机号码</view>
			<ls-mobile-code class="mobile-code" ref="mobileCodeRef" business="modifyMobilePhoneCode"></ls-mobile-code>
			<view class="btn" @click="modifyMobile">确认绑定</view>
		</view>
	</ls-container-nav>
</template>

<script setup>
	import {
		ref,
		onMounted,
		getCurrentInstance
	} from 'vue';
	import lsMobileCode from '@/components/common/ls-mobile-code.vue';
	import {
		post
	} from '@/utils/request';

	const eventChannel = getCurrentInstance().proxy.getOpenerEventChannel()
	const mobileCodeRef = ref(null)
	const modifyMobile = () => {
		mobileCodeRef.value.validate().then(res => {
			const request = {
				mobilePhone: res.mobilePhone,
				code: res.code
			}
			post('/userBase/modify/mobilePhone', request, () => {
				uni.showToast({
					icon: 'none',
					title: '保存成功'
				})
				uni.navigateBack()
				eventChannel.emit('on-refresh-index', {})
			})
		}).catch(e => {
			console.log(e);
		})
	}
</script>

<style lang="scss" scoped>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.title {
			font-size: var(--font-size-llg);
			font-weight: bold;
			margin: 100rpx;
		}
		.mobile-code {
			width: 80%;
		}
		
		.btn {
			margin: 40rpx;
			width: 80%;
		}
	}
	
</style>