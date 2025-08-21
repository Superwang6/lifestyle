<template>
	<ls-drawer ref="drawer">
		<template #header>
			备忘本
		</template>
		<scroll-view class="filter-content" scroll-y :show-scrollbar="false">
			<uni-collapse accordion>
				<template v-for="item in bookList" :key="item.id">
					<uni-collapse-item>
						<template v-slot:title>
							<view class="collapse-title" @longpress="saveBookClick(item)">{{item.name}}</view>
						</template>
						<uni-swipe-action>
							<template v-for="classifyItem in item.classifyList" :key="classifyItem.id">
								<uni-swipe-action-item :threshold="20" :ellipsis="1">
									<template #left>
										<view class="swipe-1"
											@click="openClassDialog(item.id, classifyItem.id, classifyItem.name)">修改
										</view>
										<view class="swipe-2" @click="openDeleteDialog(classifyItem.id)">删除</view>
									</template>
									<view class="list-item">{{classifyItem.name}}</view>
								</uni-swipe-action-item>
							</template>
						</uni-swipe-action>
						<view class="add-classify-btn btn" @click="openClassDialog(item.id)">
							<uni-icons class="icon-classify" type="plusempty" size="16"></uni-icons>
						</view>
					</uni-collapse-item>
				</template>
			</uni-collapse>
		</scroll-view>
		<template #bottom>
			<view class="add-book-btn btn" @click="saveBookClick()">添加备忘本</view>
		</template>
	</ls-drawer>
	<ls-dialog ref="dialog" title="保存分类" @confirm="confirmClassify ">
		<uni-easyinput trim="all" v-model="saveClassifyName" placeholder="请输入分类名称"></uni-easyinput>
	</ls-dialog>
	<ls-dialog ref="dialogBook" title="保存备忘录" @confirm="confirmBook">
		<view class="book-form">
			<uni-easyinput class="book-form-item" trim="all" v-model="saveBook.name"
				placeholder="请输入备忘录名称"></uni-easyinput>
			<uni-easyinput class="book-form-item" trim="all" v-model="saveBook.description" placeholder="请输入备忘录描述"
				type="textarea"></uni-easyinput>
		</view>
	</ls-dialog>
	<ls-dialog ref="deleteDialog" title="删除" @confirm="deleteClassify">
		<view>是否确认删除？</view>
	</ls-dialog>
</template>

<script setup>
	import {
		onActivated,
		onMounted,
		ref
	} from 'vue'
	import {storeToRefs} from 'pinia'
	import LsDrawer from '@/components/common/ls-drawer.vue';
	import LsDialog from '@/components/common/ls-dialog.vue'
	import {
		post
	} from '@/utils/request';
	import { useJotStore } from '@/stores/jot-store';

	const jotStore = useJotStore()
	const { bookList } = storeToRefs(jotStore)
	const drawer = ref(null)
	const openHome = () => {
		drawer.value.open()
	}
	const queryBookList = () => {
		jotStore.queryJotBookList()
	}

	const dialog = ref(null)
	const addBookId = ref(null)
	const modifyClassfyId = ref(null)
	const openClassDialog = (bookId, classifyId, classifyName) => {
		drawer.value.close()

		saveClassifyName.value = ''
		modifyClassfyId.value = null
		dialog.value.open()
		addBookId.value = bookId
		modifyClassfyId.value = classifyId
		if (classifyName) {
			saveClassifyName.value = classifyName
		}
	}
	const saveClassifyName = ref('')
	const confirmClassify = () => {
		if (modifyClassfyId.value) {
			const request = {
				'id': modifyClassfyId.value,
				'bookId': addBookId.value,
				'name': saveClassifyName.value
			}
			post('/jotClassify/modify', request, (data) => {
				uni.showToast({
					icon: 'none',
					title: '修改成功！'
				})
				queryBookList()
			})
		} else {
			const request = {
				'bookId': addBookId.value,
				'name': saveClassifyName.value
			}
			post('/jotClassify/add', request, (data) => {
				uni.showToast({
					icon: 'none',
					title: '添加成功！'
				})
				queryBookList()
			})
		}
	}

	const dialogBook = ref(null)
	const saveBook = ref({})
	const saveBookClick = (item) => {
		drawer.value.close()

		saveBook.value = {}
		dialogBook.value.open()
		if (item) {
			saveBook.value.id = item.id
			saveBook.value.name = item.name
			saveBook.value.description = item.description
		}
	}
	const confirmBook = () => {
		if (saveBook.value.id) {
			const request = {
				'id': saveBook.value.id,
				'name': saveBook.value.name,
				'description': saveBook.value.description
			}
			post('/jotBook/modify', request, (data) => {
				uni.showToast({
					icon: 'none',
					title: '修改成功！'
				})
				queryBookList()
			})
		} else {
			const request = {
				'name': saveBook.value.name,
				'description': saveBook.value.description
			}
			post('/jotBook/add', request, (data) => {
				uni.showToast({
					icon: 'none',
					title: '添加成功！'
				})
				queryBookList()
			})
		}
	}
	const deleteDialog = ref(null)
	const deleteClassifyId = ref(null)
	const openDeleteDialog = (classifyId) => {
		drawer.value.close()
		
		deleteClassifyId.value = classifyId
		deleteDialog.value.open()
	}
	const deleteClassify = () => {
		post('/jotClassify/delete/' + deleteClassifyId.value, null, () => {
			uni.showToast({
				title: '删除成功！',
				icon: 'none'
			})
			queryBookList()
		})
	}

	onMounted(() => {
		queryBookList()
	})
	defineExpose({
		openHome
	})
</script>

<style lang="scss">
	.filter-content {
		height: 100vh;
		
		.collapse-title {
			font-size: var(--font-size);
			padding: 8px 0 8px 10px;
			height: 30px;
		}
		
		.swipe-1 {
			height: 40px;
			line-height: 40px;
			color: var(--reverse-text-color);
			background: lightblue;
			padding: 0 15px;
		}
		
		.swipe-2 {
			height: 40px;
			line-height: 40px;
			color: var(--reverse-text-color);
			background: lightcoral;
			padding: 0 15px;
		}
		
		.list-item {
			height: 40px;
			line-height: 40px;
			font-size: var(--font-size);
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			display: block;
			padding-left: 20px;
		
		}
		
		.add-classify-btn {
			background-color: lightblue;
			width: 60%;
			margin-left: 20px;
		}
	}
	
	.book-form {
		display: flex;
		flex-direction: column;
	
		.book-form-item {
			margin: 5px 0 5px 0;
		}
	}
</style>