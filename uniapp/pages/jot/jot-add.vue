<template>
	<ls-container-scroll-nav :title="title">
		<view class="uni-body">
			<uni-forms label-position="top" ref="form" :model="detailItem" :rules="rules">
				<uni-forms-item label="标题" name="title" required>
					<uni-easyinput v-model="detailItem.title"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="描述" name="description">
					<uni-easyinput type="textarea" v-model="detailItem.description" placeholder="请输入内容"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="分类" name="classifyId">
					<uni-data-checkbox mode='tag' :multiple='false' v-model="detailItem.classifyId"
						:localdata="classifyList" :wrap="true" :map="{text:'name',value:'id'}"
						selected-color="#ADD8E6"></uni-data-checkbox>
				</uni-forms-item>
				<uni-forms-item label="提醒方式" name="remindType">
					<view class="remind-title">
						<span class="remind-title-left" @click="remidTypeClick(0)"
							:class="{active: detailItem.remindType == 0}">单次</span>
						<span class="remind-title-right" @click="remidTypeClick(1)"
							:class="{active: detailItem.remindType == 1}">cron表达式</span>
					</view>
					<view class="remind-body">
						<view v-if="detailItem.remindType == 0">
							<uni-easyinput @focus="openTimePicker" prefixIcon="calendar" :value="detailItem.remindTime"
								@iconClick="openTimePicker">
							</uni-easyinput>
							<uni-popup ref="timePickerPopup" type="bottom" border-radius="10px 10px 0 0">
								<l-date-time-picker format='YYYY-MM-DD HH:mm:00' title="选择时间" confirm-btn="确认"
									cancel-btn="取消" :start="format(new Date(), 'YYYY-MM-DD HH:mm:00')"
									:end="format(new Date(now.setMonth(now.getMonth() + 1)), 'YYYY-MM-DD HH:mm:00')"
									@confirm="onTimerPickerConfirm" @cancel="onTimerPickerCancel" :mode="1|2|4|8|16">
								</l-date-time-picker>
							</uni-popup>
						</view>
						<view v-else>
							<ls-cron :expression='true' v-model="detailItem.cronExpression"></ls-cron>
						</view>
					</view>
				</uni-forms-item>
				<uni-forms-item v-if="detailItem.remindType == 1" label="提醒次数" name="triggerTimes">
					<uni-easyinput placeholder="不填表示无限次" v-model="detailItem.triggerTimes"></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
			<view class="btn-submit btn" @click="submit">保存</view>
		</view>
	</ls-container-scroll-nav>
</template>

<script setup>
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		onMounted,
		ref,
		getCurrentInstance,
		nextTick
	} from 'vue'
	import {
		post
	} from '@/utils/request.js'
	import {
		format
	} from '@/utils/time'
	import lsCron from '@/components/common/ls-cron.vue'

	const now = new Date()
	const title = ref('新增')
	const mode = ref(0)
	const form = ref(null)
	const detailItem = ref({
		'remindTime': format(now, 'YYYY-MM-DD HH:mm:00'),
		'status': 0,
		'remindType': 0
	})
	const classifyList = ref([])
	const rules = ref({
		title: {
			rules: [{
				required: true,
				errorMessage: '请填写标题！'
			}]
		},

	})
	const remidTypeClick = (remindType) => {
		detailItem.value.remindType = remindType
	}
	const pickerData = ref([
		['a', 'b'],
		['c', 'd']
	])
	const index = ref(0)
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
		uni.navigateBack()
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
		if (mode.value == 0) {
			form.value.validate().then(res => {
				const request = {
					'title': detailItem.value.title,
					'description': detailItem.value.description,
					'classifyId': detailItem.value.classifyId,
					'bookId': detailItem.value.bookId,
					'status': detailItem.value.status,
					'remindType': detailItem.value.remindType,
				}
				if (detailItem.value.remindType == 0) {
					request['remindTime'] = detailItem.value.remindTime
				} else {
					request['cronExpression'] = detailItem.value.cronExpression
					request['triggerTimes'] = detailItem.value.triggerTimes
				}
				post('/jotRecord/add', request, () => {
					uni.$emit('jot-update')
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
					'remindType': detailItem.value.remindType,
				}
				if (detailItem.value.remindType == 0) {
					request['remindTime'] = detailItem.value.remindTime
				} else {
					request['cronExpression'] = detailItem.value.cronExpression
					if (detailItem.value.triggerTimes) {
						request['triggerTimes'] = detailItem.value.triggerTimes
					} else {
						request['triggerTimes'] = -1
					}
				}
				post('/jotRecord/modify', request, () => {
					uni.$emit('jot-update')
					uni.showToast({
						title: "修改成功！"
					})
					back()
				})
			}).catch(err => {

			})
		}
	}

	onMounted(() => {
		queryJotClassify()
	})
	onLoad((option) => {
		const instance = getCurrentInstance().proxy
		const eventChannel = instance.getOpenerEventChannel();
		mode.value = option.mode
		if (option.mode == 0) {
			title.value = "新增"
			detailItem.value.bookId = option.bookId
		} else if (option.mode == 1) {
			title.value = "修改"
			eventChannel.on('modify-jot-info', (data) => {
				detailItem.value = data
				const remindTimeObj = JSON.parse(data.remindTimeJson)
				detailItem.value.cronExpression = remindTimeObj.cron
				detailItem.value.triggerTimes = remindTimeObj.triggerTimes
			})
			if (detailItem.value.triggerTimes == -1) {
				detailItem.value.triggerTimes = null
			}
		}
	})
</script>

<style lang="scss" scoped>
	.uni-body {
		padding: 20rpx;
		display: flex;
		flex-direction: column;
		overflow: hidden;

		.remind-title {
			display: flex;
			flex-direction: row;
			margin-bottom: 5px;

			.remind-title-left {
				flex: 1;
				height: 30px;
				line-height: 30px;
				text-align: center;
				border-top-left-radius: var(--button-radius);
				border-bottom-left-radius: var(--button-radius);
				background-color: var(--light-bg-color);
			}

			.remind-title-right {
				flex: 1;
				height: 30px;
				line-height: 30px;
				text-align: center;
				border-top-right-radius: var(--button-radius);
				border-bottom-right-radius: var(--button-radius);
				background-color: var(--light-bg-color);
			}
		}
		
		.active {
			background-color: var(--primary-color) !important;
		}
	}

	.btn-submit {
		margin: 10px 0 10px 0;
	}
</style>