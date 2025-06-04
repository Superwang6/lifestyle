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
				<uni-data-checkbox mode='tag' :multiple='false' v-model="detailItem.classifyId" :localdata="classifyList"
					:map="{text:'name',value:'id'}" selected-color="#ADD8E6"></uni-data-checkbox>
			</uni-forms-item>
		</uni-forms>
		<button class="btn-submit" type="default" @click="submit">提交</button>
	</view>
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
	const form = ref(null)
	const detailItem = reactive({
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
		detailItem.remindTime = value
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
	const submit = () => {
		form.value.validate().then(res => {
			console.log(detailItem);
			post('/jotRecord/add', detailItem, () => {
				uni.showToast({
					title: "新增成功！"
				})
				back()
			})
		}).catch(err => {
			
		})
	}
	onMounted(() => {
		queryJotClassify()
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