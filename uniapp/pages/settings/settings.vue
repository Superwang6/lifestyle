<template>
	<view class="main-container">
		<uni-nav-bar title="设置" left-icon="left" :shadow="false" :border="false" @clickLeft="back()"
			background-color="#EDEDED"></uni-nav-bar>
		<uni-group >
			<view class="group">
				<view class="group-left">个人资料</view>
				<uni-icons type="right" class="group-right"></uni-icons>
			</view>
		</uni-group>
		<uni-group>
			<view class="logout" @click="logout">登出</view>
		</uni-group>
	</view>
</template>

<script setup>
	const back = () => {
		uni.navigateBack()
	}
	
	const logout = () => {
		uni.removeStorageSync('satoken')
		uni.removeStorageSync('userInfo')
		
		uni.navigateTo({
			url: '/pages/login/login'
		})
	}
</script>

<style lang="scss" scoped>
	.main-container {
		display: flex;
		flex-direction: column;

		background-color: #EDEDED;
		height: 100vh;

		.group {
			display: flex;
			flex-direction: row;
			align-items: center;

			.group-left {
				flex: 1;
				margin: 0 10px 0 10px;
			}
		}
		
		.logout {
			text-align: center;
		}
	}
</style>