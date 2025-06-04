<template>
	<view>
		<view v-if="$slots.header" class="model-header">
			<slot name="header"></slot>
		</view>
		<view v-else>
			<view>{{title}}</view>
		</view>
		<form @submit="$emit('submit')">
			<slot />
			<button form-type="submit" type="primary" plain="true" size="mini">提交</button>
		</form>
	</view>
</template>

<script setup>
	const props = defineProps(['title'])
</script>

<style lang="scss">

</style>