<template>
	<uni-drawer ref="drawer" mode="right" :width="280">
		<view class="detail">
			<view class="draw-title">{{title}}</view>
			<uni-forms class="uni-body" label-position="top">
				<uni-forms-item label="标题" name="title">
					<uni-easyinput v-model="detailItem.title"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="描述" name="description">
					<uni-easyinput type="textarea" v-model="detailItem.description" placeholder="请输入内容"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="提醒时间">
					<uni-easyinput @focus="openTimePicker" prefixIcon="calendar" v-model="detailItem.remindTime" @iconClick="openTimePicker">
					</uni-easyinput>
					<uni-popup ref="timePickerPopup" type="bottom" border-radius="10px 10px 0 0">
						<l-date-time-picker title="选择时间" v-model="value" confirm-btn="确认" cancel-btn="取消" :start="format(new Date(), 'YYYY-MM-DD HH:mm:00')"
							:end="end" @confirm="onTimerPickerConfirm" @cancel="onTimerPickerCancel" :mode="1|2|4|8|16">
						</l-date-time-picker>
					</uni-popup>
				</uni-forms-item>
				<uni-data-checkbox mode='tag' :multiple='false' v-model="detailItem.classifyId" :localdata="classifyList"
					:map="{text:'name',value:'id'}" selected-color="#ADD8E6"></uni-data-checkbox>
				<button class="btn-submit" type="default">提交</button>
			</uni-forms>
		</view>
	</uni-drawer>
</template>

<script setup>
	import {
		onMounted,
		reactive,
		ref
	} from 'vue'
	import {
		post
	} from '@/utils/request.js'
	import {
		format
	} from '@/utils/time'
	const emits = defineEmits(['refreshIndex'])
	
	const now = new Date()
	const title = ref('新增')
	const detailItem = reactive({
		'title': '',
		'description': '',
		'classifyId': 1,
		'remindTime': format(now,'YYYY-MM-DD HH:mm:00'),
		'status': 0,
		'bookId': 1,
		'status': 0
	})
	const classifyList = ref([])
	const drawer = ref(null)

	const timePickerPopup = ref(null)
	const openTimePicker = () => {
		timePickerPopup.value.open()
	}
	
	const value = ref(format(now, 'YYYY-MM-DD HH:mm:00'))
	
	const end = ref(format(new Date(now.setMonth(now.getMonth() + 1)), 'YYYY-MM-DD HH:mm:00'))
	const onTimerPickerConfirm = (value) => {
		timePickerPopup.value.close()
	}
	const onTimerPickerCancel = () => {
		timePickerPopup.value.close()
	}

	const back = () => {
		uni.navigateBack({
			delta: 1
		})
	}
	const queryJotClassify = () => {
		const request = {
			'pageNum': 1,
			'pageSize': 30
		}
		post('/jotClassify/page', request, (data) => {
			classifyList.value = []
			classifyList.value.push(...data.data)
			console.log(classifyList.value);
		})
	}
	const addJot = () => {
		drawer.value.open()
	}
	onMounted(() => {
		queryJotClassify()
	})
	defineExpose({
		addJot
	})
</script>

<style lang="scss" scoped>
	.detail {
		height: 100vh;
		display: flex;
		flex-direction: column;
		margin: 0 10px 0 10px;

		.draw-title {
			margin: 10px 0 10px 0;
		}

		.uni-body {
			flex: 1;
			display: flex;
			flex-direction: column;

			.classify {
				margin-top: 10px;
				border-radius: 25px;
				padding: 5px 20px 5px 20px;
				background-color: lightblue;
				font-size: 10px;

				align-self: flex-start;
				width: auto;
			}
		}

		.btn-submit {
			width: 80%;
			margin-bottom: 30px;

			border-radius: 30px;
		}

	}
</style>