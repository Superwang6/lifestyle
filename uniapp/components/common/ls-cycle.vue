<template>
	<view class="content">
		<view class="title" @click="openPop">
			<view>xxxxxxxx</view>
		</view>
		<view class="expression">

		</view>
		<view class="run-time">

		</view>
		<uni-popup ref="popup" type="bottom" border-radius="10px 10px 0 0" background-color="#fff">
			<picker-view class="picker-view">
				<picker-view-column>
					<view class="picker-item" v-for="(item,index) in second" :key="index">{{item}}秒</view>
				</picker-view-column>
				<picker-view-column>
					<view class="picker-item" v-for="(item,index) in min" :key="index">{{item}}分</view>
				</picker-view-column>
				<picker-view-column>
					<view class="picker-item" v-for="(item,index) in hour" :key="index">{{item}}时</view>
				</picker-view-column>
				<picker-view-column>
					<view class="picker-item" v-for="(item,index) in dayOfWeek" :key="index">{{item}}</view>
				</picker-view-column>
				<picker-view-column>
					<view class="picker-item" v-for="(item,index) in dayOfMonth" :key="index">{{item}}月</view>
				</picker-view-column>
			</picker-view>
		</uni-popup>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	const popup = ref(null)
	const type = ref(0)
	const second = ref([0])
	const min = ref(["每",0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59])
	const hour = ref(["每",0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23])
	const dayOfWeek = ref(["无","周一","周二","周三","周四","周五","周六","周天"])
	const dayOfMonth = ref([1,2,3,4,5,6,7,8,9,10,11,12])
	const openPop = () => {
		popup.value.open()
	}
</script>

<style lang="scss">
	.picker-view {
		width: 100vw;
		height: 30vh;

		.picker-item {
			line-height: 5vh;
			text-align: center;
		}
	}
</style>