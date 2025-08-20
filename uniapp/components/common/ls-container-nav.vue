<template>
	<ls-container>
		<uni-nav-bar :left-icon="leftIcon" @clickLeft="clickLeft" :title="title" :border="border" :shadow="shadow" :right-icon="rightIcon" :right-text="rightText" @clickRight="clickRight"></uni-nav-bar>
		<slot></slot>
	</ls-container>
</template>

<script setup lang="ts">
import { onMounted ,ref} from 'vue'

	const props = withDefaults(defineProps<{
		title : string,
		rightText ?: string,
		leftIcon ?: string,
		rightIcon ?: string,
		border ?: boolean,
		shadow?: boolean
	}>(), {
		leftIcon: 'left',
		border: false,
		shadow: false
	})
	const emit = defineEmits<{
		clickRight : [],
		clickLeft : []
	}>()
	const clickLeft = () => {
		if (props.leftIcon == 'left') {
			back()
		} else {
			emit('clickLeft')
		}
	}
	const clickRight = () => {
		emit('clickRight')
	}
	const back = () => {
		uni.navigateBack()
	}
</script>

<style lang="scss" scoped>
</style>