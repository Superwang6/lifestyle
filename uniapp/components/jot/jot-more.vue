<template>
	<ls-drawer ref="drawer" mode="right">
		<template #header>
			筛选
		</template>
		<scroll-view class="filter-content" scroll-y :show-scrollbar="false">
			<uni-forms label-position="top" :modelValue="jotRequest">
				<uni-forms-item name="book" label="备忘本">
					<uni-data-select v-model="jotRequest.bookId" :localdata="bookData" :clear="false" @change="changeBook"></uni-data-select>
				</uni-forms-item>
				<uni-forms-item name="status" label="状态">
					<view class="flex-row">
						<template v-for="item in statusList">
							<view class="button" :class="jotRequest.status == item.status ? 'active' : ''" @click="chooseStatus(item.status)">{{item.name}}</view>
						</template>
					</view>
				</uni-forms-item>
				<uni-forms-item name="time" label="时间">
					<view class="time">
						<view class="button" :class="jotRequest.timeType == 4 ? ' active' : ''" @click="chooseTime(4)">历史</view>
						<view class="button" :class="jotRequest.timeType == 3 ? ' active' : ''" @click="chooseTime(3)">将来</view>
						<view class="button" :class="jotRequest.timeType == 5 ? ' active' : ''" @click="chooseTime(5)">表达式</view>
					</view>
				</uni-forms-item>
				<uni-forms-item name="classify" label="分类">
					<view class="classify">
						<template v-for="(item, index) in classifyList">
							<view class="button text-hidden" :class="jotRequest.classifyId == item.id ? 'active': ''" 
								@click="chooseClassify(item.id)">{{item.name}}</view>
						</template>
					</view>
				</uni-forms-item>
			</uni-forms>
		</scroll-view>
		<template #bottom>
			<view class="btn confirm-btn" @click="confirm()">确认</view>
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
			'name': '已完成'
		},
		{
			'status': 3,
			'name': '已失败'
		}
	])
	const chooseStatus = (status) => {
		if(jotRequest.value.status == status) {
			jotRequest.value.status = null
		} else {
			jotRequest.value.status = status 
		}
	}
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
		if(jotRequest.value.status == -1) {
			jotRequest.value.status = undefined
		}
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

<style lang="scss" scoped>
	.filter-content {
		height: 100%;
	}
	.time {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		overflow-y: auto;
		max-height: 20vh;
	}
	.classify {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
	}
	.active {
		color: #FFFFFF;
		background-color: lightblue;
	}
</style>