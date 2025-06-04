<template>
	<Model @submit="submit" title="新增/修改备忘录">
		<view class="uni-flex uni-column">
			<view class="flex-item">
				<view>title</view>
				<input v-model:value="data.title" class="uni-input" focus />
			</view>
			<view class="flex-item">
				<view>title</view>
				<input v-model:value="data.title" class="uni-input" focus />
			</view>
			<view class="flex-item">
				<view>title</view>
				<input v-model:value="data.title" class="uni-input" focus />
			</view>
		</view>
		
	</Model>
</template>

<script setup>
	import { reactive, ref } from 'vue';
	import Model from '../common/Model.vue'
	
	const data = reactive({
		"title": ""
	})
	const submit = () => {
		console.log(data.title);
	}
</script>

<style lang="scss">
	input {
		margin: 5px;
		border: 1px solid skyblue
	}
</style>