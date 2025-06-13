<template>
	<ls-drawer ref="drawer">
		<template #header>
			备忘本
		</template>
		<view>
			<uni-collapse accordion>
				<template v-for="item in bookList">
					<uni-collapse-item :title="item.name" @longpress="saveBookClick(item)">
						<uni-list :border="false">
							<template v-for="classifyItem in item.classifyList">
								<uni-list-item :border="false" :title="classifyItem.name" :ellipsis="1"
									class="list-item" @longpress="openClassDialog(item.id, classifyItem.id, classifyItem.name)">
								</uni-list-item>
							</template>
						</uni-list>
						<view class="add-classify-btn" @click="openClassDialog(item.id)">
							<uni-icons class="icon-classify" type="plusempty" size="24"></uni-icons>
						</view>
					</uni-collapse-item>
				</template>
			</uni-collapse>
		</view>
		<template #bottom>
			<view class="add-book-btn" @click="saveBookClick()">添加备忘本</view>
		</template>
	</ls-drawer>
	<ls-dialog ref="dialog" title="添加分类" @confirm="confirmClassify ">
		<uni-easyinput trim="all" v-model="saveClassifyName" placeholder="请输入分类名称"></uni-easyinput>
	</ls-dialog>
	<ls-dialog ref="dialogBook" title="添加备忘录" @confirm="confirmBook">
		<view class="book-form">
			<uni-easyinput class="book-form-item" trim="all" v-model="saveBook.name" placeholder="请输入备忘录名称"></uni-easyinput>
			<uni-easyinput class="book-form-item" trim="all" v-model="saveBook.description" placeholder="请输入备忘录描述" type="textarea"></uni-easyinput>
		</view>
	</ls-dialog>
</template>

<script setup>
	import {
		onActivated,
		onMounted,
		ref
	} from 'vue'
	import LsDrawer from '@/components/common/ls-drawer.vue';
	import LsDialog from '@/components/common/ls-dialog.vue'
	import {
		post
	} from '@/utils/request';

	const drawer = ref(null)
	const openHome = () => {
		drawer.value.open()
	}
	const bookList = ref([])
	const queryBookList = () => {
		const request = {
			"pageNum": 1,
			"pageSize": 30
		}
		post('/jotBook/page', request, (data) => {
			console.log(data);
			bookList.value.push(...data.data)
		})
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
		console.log(classifyId + '-' + classifyName);
		modifyClassfyId.value = classifyId
		if(classifyName) {
			saveClassifyName.value = classifyName
		}
	}
	const saveClassifyName = ref('')
	const confirmClassify = () => {
		if(modifyClassfyId.value) {
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
				bookList.value = []
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
				bookList.value = []
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
		if(item) {
			saveBook.value.id = item.id
			saveBook.value.name = item.name
			saveBook.value.description = item.description
		}
	}
	const confirmBook = () => {
		if(saveBook.value.id) {
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
				bookList.value = []
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
				bookList.value = []
				queryBookList()
			})
		}
	}

	onMounted(() => {
		queryBookList()
	})
	defineExpose({
		openHome
	})
</script>

<style lang="scss">
	.list-item {
		margin-left: 10px;
	}

	.add-classify-btn {
		background-color: lightblue;
		display: flex;
		flex-direction: row;
		justify-content: center;
		border-radius: 20px;
		margin: 0 20px 0 20px;

		.icon-classify {
			margin: 5px;
		}
	}
	
	.add-book-btn {
		background-color: lightgreen;
		border-radius: 20px;
		height: 40px;
		line-height: 40px;
		text-align: center;
	}
	.add-book-btn:active {
		transform: scale(0.96);
		background-color: lightseagreen;
		box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
	}
	
	.book-form {
		display: flex;
		flex-direction: column;
		.book-form-item {
			margin: 5px 0 5px 0;
		}
	}
</style>