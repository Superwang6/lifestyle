<template>
	<view class="content">
		<!-- <view class="child"> -->
			<view class="top">11</view>
			<view class="main">
				<view style="width:30px;height: 100px;background-color: brown;">111</view>
				<view style="width:30px;height: 100px;background-color: brown;">111</view>
				<view style="width:30px;height: 100px;background-color: brown;">111</view>
				
				<view style="width:30px;height: 100px;background-color: brown;">111</view>
				<view style="width:30px;height: 100px;background-color: brown;">111</view>
				<view style="width:30px;height: 100px;background-color: brown;">111</view>
			</view>
		<!-- </view> -->
	</view>
</template>

<script setup>
	import { ref } from 'vue';
	
</script>

<style lang="scss" scoped>
	.content {
		display: flex;
		flex-direction: column;
		height: 500px;
		background-color: lawngreen;
		
		.child {
			flex-grow: 1;
			display: flex;
			flex-direction: column;
			
			background-color: salmon;
		}
		
		.top {
			height: 44px;
			background-color: wheat;
		}
		
		.main {
			flex: 1;
			background-color: skyblue;
			width: 50px;
			overflow-y: auto;
		}
	}
</style>