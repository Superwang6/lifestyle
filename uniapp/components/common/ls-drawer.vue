<template>
	<uni-drawer ref="draw" :mode="mode">
		<ls-container ref="conRef" class="content">
			<view class="header">
				<slot name="header"></slot>
			</view>
			<scroll-view class="body" scroll-y :show-scrollbar="false">
				<slot></slot>
			</scroll-view>
			<view class="bottom">
				<slot name="bottom"></slot>
			</view>
		</ls-container>
	</uni-drawer>
</template>

<script setup lang="ts">
	import {
		onMounted,
		ref
	} from 'vue'
	
	const props = defineProps<{
		mode?: string
	}>()

	const draw = ref(null)
	const conRef = ref(null)
	
	const open = () => {
		draw.value.open()
	}
	const close = () => {
		draw.value.close()
	}
	
	defineExpose({
		open,
		close
	})
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction: column;
		margin: 0 10px 0 10px;

		.header {
			margin: 10px 0 10px 0;
			max-height: 5%;
		}

		.body {
			flex: 1;
			height: 85%;
		}

		.bottom {
			margin: 10px 0 10px 0;
			max-height: 10%;
		}
	}
</style>