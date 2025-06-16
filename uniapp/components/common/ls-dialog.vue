<template>
	<uni-popup ref="dialoger" type="dialog">
		<uni-popup-dialog :title="title" mode="input" @confirm="confirm">
			<slot></slot>
		</uni-popup-dialog>
	</uni-popup>
</template>

<script setup>
	import {
		ref
	} from 'vue'

	const props = defineProps(['title'])
	const emits = defineEmits(['confirm'])

	const dialoger = ref(null)
	const open = () => {
		dialoger.value.open()
	}
	const close = () => {
		dialoger.value.close()
	}
	const confirm = () => {
		emits('confirm')
		dialoger.value.close()
	}
	defineExpose({
		open,
		close,
		confirm
	})
</script>

<style lang="scss">

</style>