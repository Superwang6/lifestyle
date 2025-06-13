<template>
	<ls-drawer ref="drawer" mode="right">
		<template #header>
			筛选
		</template>
		<uni-forms label-position="top" :modelValue="formData">
			<uni-forms-item name="book" label="备忘本">
				<uni-data-select v-model="formData.bookId" :localdata="bookData" :clear="false" @change="changeBook"></uni-data-select>
			</uni-forms-item>
			<uni-forms-item name="classify" label="分类">
				<view class="classify">
					<template v-for="(item, index) in classifyList">
						<view :style="selectClassifyIndex == index ? chooseStyle : ''" class="classify-item"
							@click="chooseClassify(index)">{{item.name}}</view>
					</template>
				</view>
			</uni-forms-item>
			<uni-forms-item name="status" label="状态">
				<uni-data-checkbox mode='tag' :multiple='false' v-model="formData.status" :localdata="statusList"
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

	const emits = defineEmits([
		'filterParam'
	])

	const drawer = ref(null)
	const formData = ref({})
	const openMore = () => {
		const req = uni.getStorageSync('jot_more_filter')
		if (req.status) {
			formData.value.status = req.status
		} else {
			formData.value.status = 0
		}
		if (req.bookId) {
			formData.value.bookId = req.bookId
			classifyList.value = bookMap.value[req.bookId].classifyList
			if (req.classifyId) {
				formData.value.classifyId = req.classifyId
			}
		} else {
			formData.value.bookId = bookList.value[0].id
			classifyList.value = bookList.value[0].classifyList
		}
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
	const selectClassifyIndex = ref(null)
	const chooseClassify = (index) => {
		formData.value.classifyId = classifyList.value[index].id
		if (selectClassifyIndex.value == index) {
			selectClassifyIndex.value = null
		} else {
			selectClassifyIndex.value = index
		}
	}
	const chooseStyle = reactive({
		color: '#FFFFFF',
		backgroundColor: 'lightblue'
	})
	const confirm = () => {
		emits('filterParam', formData.value)
		uni.setStorageSync('jot_more_filter', formData.value)
		drawer.value.close()
	}

	const bookData = ref([])
	const bookList = ref([])
	const bookMap = ref({})
	const queryBookList = () => {
		const request = {
			"pageNum": 1,
			"pageSize": 30
		}
		post('/jotBook/page', request, (data) => {
			bookList.value.push(...data.data)
			for (var i = 0; i < data.data.length; i++) {
				const res = {
					'text': data.data[i].name,
					'value': data.data[i].id
				}
				bookData.value.push(res)
				bookMap.value[data.data[i].id] = data.data[i]
			}
		})
	}
	const changeBook = (bookId) => {
		classifyList.value = bookMap.value[bookId].classifyList
		selectClassifyIndex.value = null
		formData.value.classifyId = null
		console.log(formData.value);
	}
	onMounted(() => {
		queryBookList()
	})
	defineExpose({
		openMore
	})
</script>

<style lang="scss">
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
		background-color: deepskyblue;
		text-align: center;
		height: 30px;
		line-height: 30px;
		border-radius: 30px;
	}

	.confirm-btn:active {
		transform: scale(0.96);
		background-color: lightskyblue;
		box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
	}
</style>