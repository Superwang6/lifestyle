<template>
	<uni-popup ref="message" type="message">
		<uni-popup-message :message="messageText" :duration="2000"></uni-popup-message>
	</uni-popup>
</template>

<script setup>
	const messageText = ref('')
	const message = ref(null)
	const openMessage = (msgType, msgText) => {
		messageText.value = msgText
		message.open(msgType)
	}
</script>

<style>
</style>