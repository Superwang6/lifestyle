<template>
	<uni-popup ref="popup" @change="popChange" type="bottom" background-color="#fff" border-radius="10px 10px 0 0">
		<view class="content">
			<view class="title">{{title}}</view>
			<template v-for="item in options">
				<view class="option" @click="chooseOption(item)">
					{{item.name}}
				</view>
			</template>
			<view class="split"></view>
			<view class="option" @click="cancel">取消</view>
		</view>
	</uni-popup>
</template>

<script setup lang="ts">
	import {
		ref
	} from 'vue';

	const props = defineProps<{
		title: string
	}>()
	interface Option {
		name: string
	}

	const popup = ref(null)
	const options = ref(null)
	let popResolve = undefined
	let popReject = undefined
	const open = (opts: Option[]) => {
		options.value = opts
		popup.value.open()
		return new Promise((resolve,reject) => {
			popResolve = resolve
			popReject = reject
		})
	}
	const chooseOption = (item: Option) => {
		if(popResolve) {
			popResolve(item)
			popResolve = undefined
			popReject = undefined
		}
		popup.value.close()
	}
	const cancel = () => {
		if(popReject) {
			popReject()
			popResolve = undefined
			popReject = undefined
		}
		popup.value.close()
	}
	const popChange = (e) => {
		if(!e.show) {
			if(popReject) {
				popReject()
				popResolve = undefined
				popReject = undefined
			}
		}
	}
	defineExpose({
		open
	})
</script>

<style lang="scss" scoped>
	.content {
		text-align: center;
		
		.title {
			font-weight: bold;
			margin: 10px;
		}
		.option {
			padding: 10px;
		}
		.split {
			height: 5px;
			background-color: var(--bg-color);
		}
	}
	
</style>