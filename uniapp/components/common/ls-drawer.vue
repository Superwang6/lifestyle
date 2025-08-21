<template>
	<uni-drawer ref="draw" :mode="mode">
		<ls-container class="content">
			<view class="header">
				<slot name="header"></slot>
			</view>
			<view class="body">
				<slot></slot>
			</view>
			<view class="bottom">
				<slot name="bottom"></slot>
			</view>
		</ls-container>
	</uni-drawer>
</template>

<script setup lang="ts">
	import {
		ref
	} from 'vue'
	const props = defineProps<{
		mode?: string
	}>()

	const draw = ref(null)
	const open = () => {
		draw.value.open()
	}
	const close = () => [
		draw.value.close()
	]
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
		}

		.body {
			flex: 1;
			overflow: hidden;
		}

		.bottom {
			margin: 10px 0 10px 0;
		}
	}
</style>