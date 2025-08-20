<template>
	<ls-container-nav title="用户名修改">
		<view class="container">
			<image src="/static/warning.png" mode="aspectFit"></image>
			<view v-if="mode == 0" class="username">用户名：{{account}}</view>
			<view v-else class="username modify">
				用户名：
				<uni-easyinput placeholder="请输入用户名" v-model="account" :clearable="false" :trim="true"
					:maxlength="15"></uni-easyinput>
			</view>
			<view class="tips">
				用户名是节奏生活的唯一凭证，请妥善保管
			</view>
			<view v-if="mode == 0" class="btn" @click="changeMode">修改用户名</view>
			<view v-else class="btn" @click="modifyAccount">保存用户名</view>
		</view>
	</ls-container-nav>
</template>

<script setup>
	import {
		getCurrentInstance,
		onMounted,
		ref
	} from 'vue';
	import {
		post
	} from '@/utils/request';
	const account = ref('')

	const mode = ref(0)
	const changeMode = () => {
		mode.value = 1
	}
	const modifyAccount = () => {
		const request = {
			'account': account.value
		}
		post('/userBase/modify/account', request, (data) => {
			uni.showToast({
				icon: 'none',
				title: '保存成功！'
			})
			queryUser()
			mode.value = 0
		})
	}

	const queryUser = () => {
		post('/userBase/detail', {}, (data) => {
			account.value = data.data.account
		})
	}
	
	const back = () => {
		uni.navigateBack()
	}

	onMounted(() => {
		queryUser()
	})
</script>

<style lang="scss" scoped>
	.container {
		align-items: center;
	
		.username {
			text-align: center;
			font-size: var(--font-size-llg);
			font-weight: bold;
			margin: 20rpx;
			width: 50%;
		}
	
		.modify {
			display: flex;
			flex-direction: row;
			align-items: center;
		}
	
		.tips {
			font-size: var(--font-size-sm);
			color: var(--light-text-color);
			margin: 20rpx;
		}
		.btn {
			width: 60%;
		}
	
	}
</style>