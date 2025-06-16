<template>
	<ls-drawer ref="drawer" mode="right">
		<template #header>
			筛选
		</template>
		<uni-forms label-position="top" :modelValue="jotRequest">
			<uni-forms-item name="book" label="备忘本">
				<uni-data-select v-model="jotRequest.bookId" :localdata="bookData" :clear="false" @change="changeBook"></uni-data-select>
			</uni-forms-item>
			<uni-forms-item name="time" label="时间">
				<view class="time">
					<view class="time-item" :style="jotRequest.timeType == 4 ? chooseTimeStyle: ''" @click="chooseTime(4)">历史</view>
					<view class="time-item" :style="jotRequest.timeType == 0 ? chooseTimeStyle: ''" @click="chooseTime(0)">当日</view>
					<view class="time-item" :style="jotRequest.timeType == 3 ? chooseTimeStyle: ''" @click="chooseTime(3)">将来</view>
					<view class="time-item" :style="jotRequest.timeType == 1 ? chooseTimeStyle: ''" @click="chooseTime(1)">近三天</view>
					<view class="time-item" :style="jotRequest.timeType == 2 ? chooseTimeStyle: ''" @click="chooseTime(2)">近七天</view>
				</view>
			</uni-forms-item>
			<uni-forms-item name="classify" label="分类">
				<view class="classify">
					<template v-for="(item, index) in classifyList">
						<view :style="jotRequest.classifyId == item.id ? chooseStyle : ''" class="classify-item"
							@click="chooseClassify(item.id)">{{item.name}}</view>
					</template>
				</view>
			</uni-forms-item>
			<uni-forms-item name="status" label="状态">
				<uni-data-checkbox mode='tag' :multiple='false' v-model="jotRequest.status" :localdata="statusList"
					:map="{text:'name',value:'status'}" selected-color="#A6E22E"></uni-data-checkbox>
			</uni-forms-item>
		</uni-forms>
		<template #bottom>
			<view class="confirm-btn" @click="confirm()">确认</view>
		</template>
	</ls-drawer>
</template>

<script setup>
	import {
		onMounted,
		reactive,
		ref
	} from 'vue';
	import {
		post
	} from '@/utils/request';
	import LsDrawer from '@/components/common/ls-drawer.vue';
	import { format } from '@/utils/time';
	import { useJotStore } from '@/stores/jot-store';
	import { storeToRefs } from 'pinia'

	const emits = defineEmits([
		'filterParam'
	])

	const drawer = ref(null)
	const jotStore = useJotStore()
	const { jotRequest } = storeToRefs(jotStore)
	const openMore = () => {
		drawer.value.open()
	}
	const statusList = ref([{
			'status': 0,
			'name': '待处理'
		},
		{
			'status': 1,
			'name': '已处理'
		},
		{
			'status': 2,
			'name': '已忽略'
		}
	])
	const classifyList = ref([])
	const chooseClassify = (classifyId) => {
		if(jotRequest.value.classifyId == classifyId) {
			jotRequest.value.classifyId = null
		} else {
			jotRequest.value.classifyId = classifyId
		}
	}
	const chooseStyle = reactive({
		color: '#FFFFFF',
		backgroundColor: 'lightblue'
	})
	const confirm = () => {
		emits('filterParam')
		uni.setStorageSync('jot_more_filter', jotRequest.value)
		drawer.value.close()
	}

	const bookData = ref([])
	const bookMap = ref({})
	const queryBookList = () => {
		const request = {
			"pageNum": 1,
			"pageSize": 30
		}
		post('/jotBook/page', request, (data) => {
			for (var i = 0; i < data.data.length; i++) {
				const res = {
					'text': data.data[i].name,
					'value': data.data[i].id
				}
				bookData.value.push(res)
				bookMap.value[data.data[i].id] = data.data[i]
			}
			if(jotRequest.value.bookId) {
				classifyList.value = bookMap.value[jotRequest.value.bookId].classifyList
			} else {
				classifyList.value = data.data[0].classifyList
			}
		})
	}
	const changeBook = (bookId) => {
		classifyList.value = bookMap.value[bookId].classifyList
		jotRequest.value.classifyId = null
	}
	
	const chooseTimeStyle = reactive({
		color: '#FFFFFF',
		backgroundColor: 'lightcoral'
	})
	const chooseTime = (index) => {
		if(jotRequest.value.timeType != index) {
			jotRequest.value.timeType = index
		} else {
			jotRequest.value.timeType = null
		}
	}
	
	onMounted(() => {
		queryBookList()
	})
	defineExpose({
		openMore
	})
</script>

<style lang="scss">
	.time {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		
		.time-item {
			height: 25px;
			line-height: 25px;
			margin: 5px;
			padding: 3px 15px 3px 15px;
			background-color: lightgray;
			border-radius: 10px;
		}
	}
	.classify {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;

		.classify-item {
			height: 25px;
			line-height: 25px;
			margin: 5px;
			padding: 3px 15px 3px 15px;
			background-color: lightgray;
			border-radius: 10px;

			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			display: block;
		}
	}

	.confirm-btn {
		background-color: lightgreen;
		border-radius: 20px;
		height: 40px;
		line-height: 40px;
		text-align: center;
	}

	.confirm-btn:active {
		transform: scale(0.96);
		background-color: lightseagreen;
		box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
	}
</style>