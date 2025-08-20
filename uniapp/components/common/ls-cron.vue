<template>
	<view class="content">
		<uni-row v-if="expression" class="expression">
			<uni-col :span="6">表达式：</uni-col>
			<uni-col :span="18"><uni-easyinput v-model="cronExpression" :clearable='false'
					disabled></uni-easyinput></uni-col>
		</uni-row>
		<uni-row class="explain">
			<uni-col :span="6">解释：</uni-col>
			<uni-col :span="18">{{explain}}</uni-col>
		</uni-row>
		<view class="tab">
			<template v-for="item in tabList">
				<span @click="changeTab(item.id)" :class="{active: type == item.id}">{{item.name}}</span>
			</template>
			<uni-icons class="refresh" @click="refreshExpression" type="refreshempty"></uni-icons>
		</view>
		<template v-for="item in tabList">
			<view class="tab-item" v-if="type == item.id">
				<view class="mode" v-if="item.modes.includes(0)" @click="chooseMode(item.id,0)"
					:class="{choose: chooseModesData[item.id].mode == 0}">
					任意(*)
				</view>
				<view class="mode" v-if="item.modes.includes(4)" @click="chooseMode(item.id,4)"
					:class="{choose: chooseModesData[item.id].mode == 4}">
					不指定(?)
				</view>
				<view class="mode" v-if="item.modes.includes(1)" @click="chooseMode(item.id,1)"
					:class="{choose: chooseModesData[item.id].mode == 1}">
					范围 {{chooseModesData[item.id].mode == 1 ? chooseModesData[item.id].x : 'x'}} {{item.name}}到
					{{chooseModesData[item.id].mode == 1 ? chooseModesData[item.id].y : 'y'}} {{item.name}}
				</view>
				<view class="mode" v-if="item.modes.includes(2)" @click="chooseMode(item.id,2)"
					:class="{choose: chooseModesData[item.id].mode == 2}">
					间隔从 {{chooseModesData[item.id].mode == 2 ? chooseModesData[item.id].x : 'x'}} {{item.name}}开始，每
					{{chooseModesData[item.id].mode == 2 ? chooseModesData[item.id].y : 'y'}} {{item.name}}执行一次
				</view>
				<view class="mode" v-if="item.modes.includes(3) && item.options">
					<span>指定：</span>
					<view class="appoint">
						<template v-for="i in item.options">
							<span @click="chooseMode(item.id,3, i)"
								:class="{choose: chooseModesData[item.id].mode == 3 && chooseModesData[item.id].chooseList && chooseModesData[item.id].chooseList.includes(i)}">{{i}}</span>
						</template>
					</view>
				</view>
			</view>
		</template>
		<uni-popup ref="popup" type="bottom" border-radius="10px 10px 0 0" background-color="#fff">
			<view class="popup">
				<view class="popup-title">
					<span @click="popupClose()">取消</span>
					<span @click="popupConfirm()">确认</span>
				</view>
				<picker-view class="picker-view" @change="pickerChange">
					<view class="picker-item-pre">x</view>
					<picker-view-column>
						<view class="picker-item" v-for="(item,index) in openPopTabData.options" :key="index">{{item}}
						</view>
					</picker-view-column>
					<view class="picker-item-pre">y</view>
					<picker-view-column>
						<view class="picker-item" v-for="(item,index) in openPopTabData.options" :key="index">{{item}}
						</view>
					</picker-view-column>
				</picker-view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup lang="ts">
	import {
		computed,
		onBeforeMount,
		onMounted,
		ref,
		watch
	} from 'vue';
	import cronstrue from 'cronstrue/i18n';

	interface ChooseMode {
		0 : {
			mode : number
			chooseList : string[]
		},
		1 : {
			mode : number
			chooseList : string[]
		},
		2 : {
			mode : number
			chooseList : string[]
		},
		3 : {
			mode : number
			chooseList : string[]
		},
		4 : {
			mode : number
		},
		5 : {
			mode : number
		},
		6 : {
			mode : number
		}
	}

	const props = defineProps<{
		modelValue ?: string,
		expression : boolean
	}>()
	const emit = defineEmits<{
		'update:modelValue' : [cron: string]
	}>()

	const tabList = ref([{
		'id': 1,
		'name': '分',
		'modes': [1, 2, 3],
		'options': ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
			"30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
			"40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
			"50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
		]
	},
	{
		'id': 2,
		'name': '时',
		'modes': [0, 1, 2, 3],
		'options': ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23"
		]
	},
	{
		'id': 3,
		'name': '日',
		'modes': [0, 1, 2, 3, 4],
		'options': ["1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
			"30", "31", "L"
		]
	},
	{
		'id': 4,
		'name': '月',
		'modes': [0, 1, 2, 3],
		'options': ["1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12"
		]
	},
	{
		'id': 5,
		'name': '周',
		'modes': [0, 1, 2, 3, 4],
		'options': ["1", "2", "3", "4", "5", "6", "7"]
	},
	{
		'id': 6,
		'name': '年',
		'modes': [0, 1, 2]
	}
	])
	const type = ref(1)
	const changeTab = (index : number) => {
		type.value = index
	}
	const initData = {
		0: {
			'mode': 3,
			'chooseList': ['0']
		},
		1: {
			'mode': 3,
			'chooseList': ['0']
		},
		2: {
			'mode': 3,
			'chooseList': ['0']
		},
		3: {
			'mode': 3,
			'chooseList': ['1']
		},
		4: {
			'mode': 0
		},
		5: {
			'mode': 4
		},
		6: {
			'mode': 0
		}
	}

	const chooseModesData = ref(initData)
	const cronExpression = computed(() => {
		let expression = ''
		Object.keys(chooseModesData.value).forEach(key => {
			const item = chooseModesData.value[key]
			if (item.mode == 0) {
				expression += '*'
			} else if (item.mode == 1) {
				expression += (item.x + '-' + item.y)
			} else if (item.mode == 2) {
				expression += (item.x + '/' + item.y)
			} else if (item.mode == 3) {
				expression += item.chooseList.join(',')
			} else if (item.mode == 4) {
				expression += '?'
			}
			expression += ' '
		})
		return expression.trim()
	})
	watch(cronExpression, (newVal) => {
		emit('update:modelValue', newVal)
	})
	const explain = computed(() => {
		return cronstrue.toString(cronExpression.value, {
			locale: "zh_CN",
			use24HourTimeFormat: true
		})
	})
	const popup = ref(null)
	const openPopTabData = ref(null)
	const chooseMode = (id : number, mode : number, option ?: string) => {
		if (mode == 1 || mode == 2) {
			openPopTabData.value = {
				...tabList.value.find(item => item.id == id)
			}
			openPopTabData.value.mode = mode
			openPopTabData.value.x = openPopTabData.value.options[0]
			openPopTabData.value.y = openPopTabData.value.options[0]
			popup.value.open()
			return
		}
		if (mode == 4) {
			if (id == 5 && chooseModesData.value[3].mode == 4) {
				chooseModesData.value[3].mode = 0
			}
			if (id == 3 && chooseModesData.value[5].mode == 4) {
				chooseModesData.value[5].mode = 0
			}
		}
		if (chooseModesData.value[id].mode != mode) {
			chooseModesData.value[id].mode = mode
			clearChooseModesData(id)
		}
		if (mode == 3) {
			if (!chooseModesData.value[id].chooseList) {
				chooseModesData.value[id].chooseList = []
			}
			if (chooseModesData.value[id].chooseList.includes(option)) {
				chooseModesData.value[id].chooseList.splice(chooseModesData.value[id].chooseList.indexOf(option), 1)
			} else {
				chooseModesData.value[id].chooseList.push(option)
			}
		}
	}

	const clearChooseModesData = (id : number) => {
		chooseModesData.value[id].chooseList = null
		chooseModesData.value[id].x = null
		chooseModesData.value[id].y = null
	}
	const pickerChange = (e : any) => {
		const value = e.detail.value
		openPopTabData.value.x = value[1]
		openPopTabData.value.y = value[3]
	}
	const popupConfirm = () => {
		clearChooseModesData(openPopTabData.value.id)
		chooseModesData.value[openPopTabData.value.id].mode = openPopTabData.value.mode
		chooseModesData.value[openPopTabData.value.id].x = openPopTabData.value.x
		chooseModesData.value[openPopTabData.value.id].y = openPopTabData.value.y
		popup.value.close()
	}
	const popupClose = () => {
		popup.value.close()
	}
	const refreshExpression = () => {
		chooseModesData.value = initData
	}

	onMounted(() => {
		if (props.modelValue != null && props.modelValue.length > 0) {
			const array = props.modelValue.split(' ')
			const tempData = initData
			for (var i = 0; i < array.length; i++) {
				tempData[i] = {}
				const item = array[i]
				if (item == '*') {
					tempData[i].mode = 0
				} else if ((i == 3 || i == 5) && item == '?') {
					tempData[i].mode = 4
				} else if (item.includes('-')) {
					tempData[i].mode = 1
					tempData[i].x = item.split('-')[0]
					tempData[i].y = item.split('-')[1]
				} else if (item.includes('/')) {
					tempData[i].mode = 2
					tempData[i].x = item.split('/')[0]
					tempData[i].y = item.split('/')[1]
				} else {
					tempData[i].mode = 3
					tempData[i].chooseList = item.split(',')
				}
			}
			chooseModesData.value = tempData
		} else {
			emit('update:modelValue', cronExpression.value)
		}
	})
</script>

<style lang="scss" scoped>
	.content {
		.expression {
			display: flex;
			flex-direction: row;
			align-items: center;
			margin-bottom: 10px;
		}

		.explain {
			margin-bottom: 10px;
		}

		.tab {
			display: flex;
			flex-direction: row;
			justify-content: space-around;

			border-top-left-radius: var(--item-radius);
			border-top-right-radius: var(--item-radius);

			span {
				height: 30px;
				line-height: 30px;
				flex: 1;
				text-align: center;

				border-top-left-radius: var(--item-radius);
				border-top-right-radius: var(--item-radius);
			}

			.refresh {
				display: flex;
				flex-direction: column;
				justify-content: center;
				flex: 1;
			}
		}

		.active {
			background-color: var(--bg-color);
		}

		.choose {
			border-radius: var(--item-radius);
			background-color: var(--primary-color);
		}

		.tab-item {
			background-color: var(--bg-color);

			padding: 10px;

			.mode {
				padding: 5px;

				.appoint {
					display: flex;
					flex-direction: row;
					flex-wrap: wrap;
					justify-content: start;

					span {
						width: 16.66%;
						text-align: center;
						height: 30px;
						line-height: 30px;
					}
				}
			}
		}

		.popup {
			padding: 10px;

			.popup-title {
				display: flex;
				justify-content: space-between;
				padding: 5px;
			}

			.picker-view {
				width: 100vw;
				height: 30vh;

				.picker-item-pre {
					height: 30vh;
					line-height: 30vh;
					text-align: center;
					width: 20vw;
				}

				.picker-item {
					line-height: 5vh;
					text-align: center;
				}
			}
		}
	}
</style>