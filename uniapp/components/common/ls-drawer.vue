<template>
	<uni-drawer ref="draw" :mode="props.mode">
		<view class="content">
			<view class="header">
				<slot name="header"></slot>
			</view>
			<view class="body">
				<slot></slot>
			</view>
			<view class="bottom">
				<slot name="bottom"></slot>
			</view>
		</view>
	</uni-drawer>
</template>

<script setup>
	import {
		ref
	} from 'vue'
	const props = defineProps(['mode'])

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
		height: 100vh;
		display: flex;
		flex-direction: column;
		margin: 0 10px 0 10px;
		

		.header {
			padding-top: var(--status-bar-height);
			margin: 10px 0 10px 0;
		}

		.body {
			flex: 1
		}

		.bottom {
			margin: 10px 0 10px 0;
		}
	}
</style>