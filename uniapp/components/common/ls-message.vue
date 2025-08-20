<template>
	<view class="content">
		<transition-group name="message">
			<template v-for="(message, index) in messageList" :key="index">
				<view class="message" :class="'uni-popup__' + message.msgType" @click="removeMessage(message.id)">
					{{message.messageText}}
				</view>
			</template>
		</transition-group>
	</view>
</template>

<script setup lang="ts">
	import {
		onMounted,
		ref,
	} from 'vue';
	interface Message {
		id: string,
		msgType: string,
		messageText: string,
		timer?: any
	}

	const props = withDefaults(defineProps<{
		duration : number
	}>(), {
		duration: 2000
	})

	const msgType = ref(null)
	const messageList = ref([])
	const isOpen = ref(false)

	const open = (type:string, text:string) => {
		if (!isOpen.value) {
			isOpen.value = true
		}
		addMessage(type, text)
	}
	const addMessage = (type:string, text:string) => {
		if (messageList.value.length > 3) {
			messageList.value.shift()
		}
		const message:Message = {
			id: "" + Date.now() + Math.random(),
			msgType: type,
			messageText: text
		}
		message.timer = setTimeout(() => {
			removeMessage(message.id)
		}, props.duration)
		messageList.value.push(message)
	}
	const removeMessage = (id:string) => {
		const index = messageList.value.findIndex(m => m.id === id)
		if (index !== -1) {
			clearTimeout(messageList.value[index].timer)
			messageList.value.splice(index, 1)
		}
		if (messageList.value.length <= 0) {
			closePop()
		}
	}
	const success = (text:string) => {
		console.log('success');
		open('success', text)
	}

	const closePop = () => {
		isOpen.value = false
	}

	defineExpose({
		success,
		open
	})
</script>

<style lang="scss" scoped>
	.content {
		position: fixed;
		top: 20rpx;
		width: 80vw;
		left: 50%;
		transform: translateX(-50%);
		z-index: 99999;

		.message {
			text-align: center;
			background-color: lightblue;
			border-radius: var(--button-radius);
			margin: 10rpx;
			padding: 15rpx;
			font-size: var(--font-size);
		}

		.message-enter-active,
		.message-leave-active {
			transition: all 0.5s ease;
		}

		.message-enter-from,
		.message-leave-to {
			opacity: 0;
			transform: translateY(-30px);
		}

		.uni-popup__success {
			background-color: #e1f3d8;
			color: #67C23A;
		}

		.uni-popup__warn {
			background-color: #faecd8;
			color: #E6A23C;
		}


		.uni-popup__error {
			background-color: #fde2e2;
			color: #F56C6C;
		}

		.uni-popup__info {
			background-color: #F2F6FC;
			color: #909399;
		}
	}
</style>