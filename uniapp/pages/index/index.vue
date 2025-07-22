<template>
	<button @click="openDrawer">111</button>
	<ls-drawer ref="drawer">
		<scroll-view :scroll-y="true" class="scroller">
			<view class="test">1111</view>
			<view class="test">1111</view>
			<view class="test">1111</view>
			<view class="test">1111</view>
			<view class="test">1111</view>
			<view class="test">1111</view>
			<view class="test">1111</view>
		</scroll-view>
	</ls-drawer>
</template>

<script setup>
	import { ref } from 'vue';
	import lsDrawer from '../../components/common/ls-drawer.vue';
	
	const drawer = ref(null)
	const openDrawer = () => {
		drawer.value.open()
	}
</script>

<style>
	page {
		overflow: hidden;
	}

	.scroller {
		height: 50vh;
	}
	.test {
		background-color: aqua;
		height: 180px;
		margin-bottom: 10px;
	}
</style>