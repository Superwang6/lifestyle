<template>
	<ls-drawer ref="drawer" mode="right">
		<template #header>
			更多
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
		computed,
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
	const { jotRequest, bookList } = storeToRefs(jotStore)
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
			jotRequest.value.status = undefined
		} else {
			jotRequest.value.status = status 
		}
	}
	const chooseClassify = (classifyId) => {
		if(jotRequest.value.classifyId == classifyId) {
			jotRequest.value.classifyId = undefined
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

	const bookData = computed(() => {
		const result = []
		for (let book of bookList.value) {
			result.push({
				'text': book.name,
				'value': book.id
			})
		}
		return result
	})
	const bookMap = computed(() => {
		const result = {}
		for (var i = 0; i < bookList.value.length; i++) {
			result[bookList.value[i].id] = bookList.value[i]
		}
		return result
	})
	const chooseBookId = ref(jotRequest.value.bookId)
	const classifyList = computed(() => {
		return bookMap.value[chooseBookId.value].classifyList
	})
	const queryBookList = () => {
		jotStore.queryJotBookList(() => {
			if(!jotRequest.value.bookId) {
				jotRequest.value.bookId = bookList.value[0].id
				chooseBookId.value = bookList.value[0].id
			}
		})
	}
	const changeBook = (bookId) => {
		chooseBookId.value = bookId
		jotRequest.value.classifyId = undefined
	}
	
	const chooseTimeStyle = reactive({
		color: '#FFFFFF',
		backgroundColor: 'lightcoral'
	})
	const chooseTime = (index) => {
		if(jotRequest.value.timeType != index) {
			jotRequest.value.timeType = index
		} else {
			jotRequest.value.timeType = undefined
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
	}
	
	.book {
		background-color: lightblue;
		margin-bottom: 40rpx;
	}
	
</style>