<template>
	<uni-popup ref="dialoger" type="dialog">
		<uni-popup-dialog :title="title" mode="input" @confirm="confirm" @close="close" :before-close="beforeClose">
			<slot></slot>
		</uni-popup-dialog>
	</uni-popup>
</template>

<script setup lang="ts">
	import {
		ref
	} from 'vue'

	const props = withDefaults(defineProps<{
		title : string,
		beforeClose ?: boolean
	}>(), {
		beforeClose: false
	})
	const emits = defineEmits<{
		confirm: [data: void]
	}>()

	const dialoger = ref(null)
	const open = () => {
		dialoger.value.open()
	}
	const close = () => {
		dialoger.value.close()
	}
	const confirm = () => {
		emits('confirm')
	}
	defineExpose({
		open,
		close,
		confirm
	})
</script>

<style lang="scss">

</style>