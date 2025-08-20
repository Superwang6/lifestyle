<template>
	<ls-container-nav title="设置">
		<view class="container">
			<view class="group">
				<view class="item" @click="goAccountSetting">
					<view class="item-left">账号管理</view>
					<uni-icons type="right" class="item-right"></uni-icons>
				</view>
			</view>
			<view class="group">
				<view class="item" @click="goModuleSetting">
					<view class="item-left">功能管理</view>
					<uni-icons type="right" class="item-right"></uni-icons>
				</view>
			</view>
			<view class="group">
				<view class="item logout" @click="logout">登出</view>
			</view>
		</view>
	</ls-container-nav>
</template>

<script setup>
	const back = () => {
		uni.navigateBack()
	}
	
	const logout = () => {
		uni.removeStorageSync('satoken')
		uni.removeStorageSync('userInfo')
		uni.removeStorageSync('jot_more_filter')
		
		uni.reLaunch({
			url: '/pages/login/login'
		})
	}
	const goAccountSetting = () => {
		uni.navigateTo({
			url: '/pages/settings/account'
		})
	}
	const goModuleSetting = () => {
		uni.navigateTo({
			url: '/pages/settings/module-setting'
		})
	}
</script>

<style lang="scss" scoped>
	.container {
		.group {
			background-color: var(--light-bg-color);
			margin-top: 20rpx;
			padding: 10rpx;
			
			.item {
				display: flex;
				flex-direction: row;
				align-items: center;
				height: 70rpx;
			
				.item-left {
					flex: 1;
					margin: 0 10px 0 10px;
				}
			}
			
			.logout {
				justify-content: center;
			}
		}
	}
</style>