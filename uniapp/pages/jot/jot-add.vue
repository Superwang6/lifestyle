<template>
	<view class="detail">
		<uni-nav-bar :title="title" left-icon="left" @clickLeft="back" :border="false" :fixed="true" />
		<uni-forms class="uni-body" label-position="top" ref="form" :model="detailItem" :rules="rules">
			<uni-forms-item label="标题" name="title" required>
				<uni-easyinput v-model="detailItem.title"></uni-easyinput>
			</uni-forms-item>
			<uni-forms-item label="描述" name="description">
				<uni-easyinput type="textarea" v-model="detailItem.description" placeholder="请输入内容"></uni-easyinput>
			</uni-forms-item>
			<uni-forms-item label="提醒时间" name="remindTime">
				<uni-easyinput @focus="openTimePicker" prefixIcon="calendar" :value="detailItem.remindTime" @iconClick="openTimePicker">
				</uni-easyinput>
				<uni-popup ref="timePickerPopup" type="bottom" border-radius="10px 10px 0 0">
					<l-date-time-picker format='YYYY-MM-DD HH:mm:00' title="选择时间" confirm-btn="确认" cancel-btn="取消" :start="format(new Date(), 'YYYY-MM-DD HH:mm:00')"
						:end="format(new Date(now.setMonth(now.getMonth() + 1)), 'YYYY-MM-DD HH:mm:00')" @confirm="onTimerPickerConfirm" @cancel="onTimerPickerCancel" :mode="1|2|4|8|16">
					</l-date-time-picker>
				</uni-popup>
			</uni-forms-item>
			<uni-forms-item label="分类" name="classifyId">
				<uni-data-checkbox mode='tag' :multiple='false' v-model="detailItem.classifyId" :localdata="classifyList" :wrap="true"
					:map="{text:'name',value:'id'}" selected-color="#ADD8E6"></uni-data-checkbox>
			</uni-forms-item>
		</uni-forms>
		<button class="btn-submit" type="default" @click="submit">保存</button>
	</view>
</template>

<script setup>
	import { onLoad } from '@dcloudio/uni-app'
	import {
		onMounted,
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
	const mode = ref(0)
	const form = ref(null)
	const detailItem = ref({
		'classifyId': 1,
		'remindTime': format(now, 'YYYY-MM-DD HH:mm:00'),
		'status': 0,
		'bookId': 1,
		'status': 0
	})
	const classifyList = ref([])
	const rules = ref({
		title: {
			rules: [
				{
					required: true,
					errorMessage: '请填写标题！'
				}
			]
		},
		
	})

	const timePickerPopup = ref(null)
	const openTimePicker = () => {
		timePickerPopup.value.open()
	}
	const onTimerPickerConfirm = (value) => {
		detailItem.value.remindTime = value
		timePickerPopup.value.close()
	}
	const onTimerPickerCancel = () => {
		timePickerPopup.value.close()
	}

	const back = () => {
		uni.switchTab({
			url: '/pages/jot/jot'
		})
	}
	const queryJotClassify = () => {
		const request = {
			'pageNum': 1,
			'pageSize': 30,
			'bookId': detailItem.value.bookId
		}
		post('/jotClassify/page', request, (data) => {
			classifyList.value = []
			classifyList.value.push(...data.data)
		})
	}
	const submit = () => {
		if(mode.value == 0) {
			form.value.validate().then(res => {
				const request = {
					'title': detailItem.value.title,
					'description': detailItem.value.description,
					'classifyId': detailItem.value.classifyId,
					'bookId': detailItem.value.bookId,
					'status': detailItem.value.status,
					'remindTime': detailItem.value.remindTime
				}
				post('/jotRecord/add', request, () => {
					uni.showToast({
						title: "新增成功！"
					})
					back()
				})
			}).catch(err => {
				
			})
		} else if (mode.value == 1) {
			form.value.validate().then(res => {
				const request = {
					'id': detailItem.value.id,
					'title': detailItem.value.title,
					'description': detailItem.value.description,
					'classifyId': detailItem.value.classifyId,
					'bookId': detailItem.value.bookId,
					'status': detailItem.value.status,
					'remindTime': detailItem.value.remindTime
				}
				post('/jotRecord/modify', request, () => {
					uni.showToast({
						title: "修改成功！"
					})
					back()
				})
			}).catch(err => {
				
			})
		}
		uni.$emit('jot-update')
	}
	onMounted(() => {
		queryJotClassify()
	})
	onLoad((option) => {
		mode.value = option.mode
		if(option.mode == 0) {
			title.value = "新增"
			detailItem.value.bookId = option.bookId
		} else if (option.mode == 1){
			title.value = "修改"
			detailItem.value = JSON.parse(decodeURIComponent(option.item))
		}
	})
</script>

<style lang="scss" scoped>
	.detail {
		height: 100vh;
		display: flex;
		flex-direction: column;
		margin: 0 10px 0 10px;

		.uni-body {
			flex: 1;
			display: flex;
			flex-direction: column;

			.classify {
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
			border-radius: 30px;
			margin-bottom: 20px;
		}

	}
</style>