<template>
	<ls-container>
		<uni-nav-bar :left-icon="leftIcon" @clickLeft="clickLeft" :title="title" :border="border" :shadow="shadow" :right-icon="rightIcon" :right-text="rightText" @clickRight="clickRight"></uni-nav-bar>
		<scroll-view class="container" scroll-y="true" :style="{height: containerHeight + 'px'}"  :show-scrollbar="false">
			<slot></slot>
		</scroll-view>
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
	const containerHeight = ref(0)
	
	onMounted(() => {
		const systemInfo = uni.getSystemInfoSync()
		containerHeight.value = systemInfo.windowHeight - 44 - systemInfo.statusBarHeight
	})
</script>

<style lang="scss" scoped>
</style>