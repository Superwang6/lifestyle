<template>
	<uni-popup ref="timePickerPopup" type="bottom" border-radius="10px 10px 0 0">
		<l-date-time-picker format='YYYY-MM-DD HH:mm:ss' title="选择时间" confirm-btn="确认" cancel-btn="取消"
			:start="props.start ? props.start : '1900-01-01 00:00:00'"
			:end="props.end ? props.end : '2100-01-01 23:59:59'"
			@confirm="onTimerPickerConfirm" @cancel="onTimerPickerCancel" :mode="props.mode">
		</l-date-time-picker>
	</uni-popup>
</template>

<script setup>
	import {
		ref
	} from 'vue'
	import dayjs from 'dayjs'
	
	const props = defineProps(['mode'])

	const timePickerPopup = ref(null)
	let resolvePop = undefined
	const onTimerPickerConfirm = (value) => {
		if(resolvePop) {
			resolvePop(value)
		}
		timePickerPopup.value.close()
	}
	const onTimerPickerCancel = () => {
		timePickerPopup.value.close()
	}
	const open = () => {
		timePickerPopup.value.open()
		return new Promise((resolve) => {
			resolvePop = resolve
		})
	}
	defineExpose({
		open
	})
</script>

<style>
</style>