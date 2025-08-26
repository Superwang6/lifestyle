<template>
	<ls-container-nav title="备忘本">
		<view class="container">
			<view class="left">
				<scroll-view class="scroller" scroll-y="true" :show-scrollbar="false">
					<template v-for="(item,index) in bookList" :key="item.id">
						<view class="left-item" @click="clickBook(item)" :class="activeBookId == item.id ? 'active' : ''">
							{{item.name}}
						</view>
					</template>
					<view class="btn set-book" @click="openBookPupup">
						<uni-icons type="gear-filled"></uni-icons>
					</view>
					<view class="display-view"></view>
				</scroll-view>
			</view>
			<view class="right">
				<uni-list v-if="activeBookId">
					<uni-swipe-action>
						<template v-for="classifyItem in queryActiveBookClassifyList()" :key="classifyItem.id">
							<uni-swipe-action-item :threshold="20" :ellipsis="1" :disabled="modifyClassifyId == classifyItem.id">
								<template #right>
									<view class="swipe-1"
										@click="openModifyClassify(classifyItem)">修改
									</view>
									<view class="swipe-2" @click="openDeleteClassifyDialog(classifyItem.id)">删除</view>
								</template>
								<uni-list-item v-if="modifyClassifyId == classifyItem.id">
									<template #body>
										<jot-save-classify :book-id="activeBookId" :classify="classifyItem" @cancel="cancelModifyClassify" @confirm="confirmModifyClassify"></jot-save-classify>
									</template>
								</uni-list-item>
								<uni-list-item v-else :title="classifyItem.name" :ellipsis="1"></uni-list-item> 
							</uni-swipe-action-item>
						</template>
					</uni-swipe-action>
					<uni-list-item v-if="addClassifyType == 1">
						<template #body>
							<jot-save-classify :book-id="activeBookId" @cancel="cancelAddClassify" @confirm="confirmAddClassify"></jot-save-classify>
						</template>
					</uni-list-item>
				</uni-list>
				<uni-icons v-if="addClassifyType == 0" class="btn add-classify" type="plusempty" @click="openAddClassify"></uni-icons>
			</view>
			
			<ls-dialog ref="deleteClassifyDialog" title="删除分类" @confirm="deleteClassifyConfirm">
				<view>是否确认删除分类？</view>
			</ls-dialog>
		</view>
		<uni-popup ref="bookPopRef" type="bottom">
			<view class="popup-container">
				<view class="title">
					备忘本设置
				</view>
				<scroll-view class="scroller" :scroll-into-view="scrollToView"  :scroll-y="true">
					<uni-list>
						<uni-swipe-action>
							<template v-for="(item,index) in bookList" :key="item.id">
								<uni-swipe-action-item :threshold="20" :ellipsis="1" :disabled="modifyItemId == item.id">
									<template #right>
										<view class="swipe-1"
											@click="openModifyBook(item)">修改
										</view>
										<view class="swipe-2" @click="openDeleteBookDialog(item.id)">删除</view>
									</template>
									<uni-list-item v-if="modifyItemId == item.id">
										<template #body>
											<jot-save-book :book="item" @confirm="modifyBookConfirm" @cancel="closeModify"></jot-save-book>
										</template>
									</uni-list-item>
									<uni-list-item v-else class="book-item" :title="item.name" :note="item.description"></uni-list-item>
								</uni-swipe-action-item>
							</template>
						</uni-swipe-action>
						<uni-list-item v-if="addBookType == 1">
							<template #body>
								<jot-save-book @confirm="addBookConfirm" @cancel="closeAdd"></jot-save-book>
							</template>
						</uni-list-item>
						<view id="bottom"></view>
					</uni-list>
				</scroll-view>
				
				<uni-icons v-if="addBookType == 0" class="btn" type="plusempty" @click="openAddBook"></uni-icons>
			</view>
			<ls-dialog ref="deleteBookDialog" title="删除备忘本" @confirm="deleteBookConfirm">
				<view>删除备忘本将同步删除此备忘本内的分类和记录，是否确认删除？</view>
			</ls-dialog>
		</uni-popup>
	</ls-container-nav>
</template>

<script lang="ts" setup>
	import { ref, onMounted,nextTick } from 'vue'
	import {storeToRefs} from 'pinia'
	import { post } from '@/utils/request'
	import jotSaveBook from '@/components/jot/jot-save-book.vue'
	import lsDialog from '@/components/common/ls-dialog.vue'
	import jotSaveClassify from '@/components/jot/jot-save-classify.vue'
	import { useJotStore } from '@/stores/jot-store'
	
	interface Book {
		id ?: number,
		name: string,
		description?:string
	}
	const jotStore = useJotStore()
	const {
		bookList
	} = storeToRefs(jotStore)
	const queryBookList = () => {
		jotStore.queryJotBookList(() =>{
			if(!activeBookId.value) {
				activeBookId.value = bookList.value[0].id
			}
		})
	}
	
	const activeBookId = ref(null)
	const clickBook = (item: any) => {
		activeBookId.value = item.id
	}
	const queryActiveBookClassifyList = () => {
		for (let item of bookList.value) {
			if(item.id == activeBookId.value) {
				return item.classifyList
			}
		}
	}
	
	const bookPopRef = ref(null)
	const openBookPupup = () => {
		bookPopRef.value.open()
	}
	
	// 新增备忘本
	const scrollToView = ref(null)
	const addBookType = ref(0)
	const openAddBook = () => {
		if(addBookType.value == 0) {
			addBookType.value = 1
			scrollToButtom()
		}
	}
	const scrollToButtom = () => {
		nextTick(() => {
			scrollToView.value = 'bottom'
			nextTick(() => {
				scrollToView.value = null
			})
		})
	}
	const closeAdd = () => {
		if(addBookType.value == 1) {
			addBookType.value = 0
		}
	}
	const addBookConfirm = () => {
		addBookType.value = 0
		queryBookList()
	}
	
	// 修改备忘本
	const modifyItemId = ref(-1)
	const openModifyBook =(item: Book) => {
		modifyItemId.value = item.id
	}
	const modifyBookConfirm = () => {
		modifyItemId.value = -1
		queryBookList()
	}
	const closeModify = () => {
		modifyItemId.value = -1
	}
	
	// 删除备忘本
	const deleteBookDialog = ref(null)
	const deleteBookId = ref(null)
	const openDeleteBookDialog = (id: number) => {
		deleteBookId.value = id
		deleteBookDialog.value.open()
	}
	const deleteBookConfirm = () => {
		post('/jotBook/delete/' + deleteBookId.value, null, () => {
			uni.$emit('jot-update')
			uni.showToast({
				title: '删除成功！',
				icon: 'none'
			})
			queryBookList()
		})
	}
	
	// 添加分类
	const addClassifyType = ref(0)
	const openAddClassify = () => {
		addClassifyType.value = 1
		modifyClassifyId.value = -1
	}
	const cancelAddClassify = () => {
		addClassifyType.value = 0
	}
	const confirmAddClassify = () => {
		addClassifyType.value = 0
		queryBookList()
	}
	
	// 修改分类
	const modifyClassifyId = ref(-1)
	const openModifyClassify = (classify: any) => {
		modifyClassifyId.value = classify.id
		addClassifyType.value = 0
	}
	const cancelModifyClassify = () => {
		modifyClassifyId.value = -1
	}
	const confirmModifyClassify = () => {
		modifyClassifyId.value = -1
		queryBookList()
	}
	//删除分类
	const deleteClassifyDialog = ref(null)
	const deleteClassifyId = ref(null)
	const openDeleteClassifyDialog = (classifyId:number) => {
		deleteClassifyDialog.value.open()
		deleteClassifyId.value = classifyId
	}
	const deleteClassifyConfirm = () => {
		post('/jotClassify/delete/' + deleteClassifyId.value, null, () => {
			uni.$emit('jot-update')
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
</script>

<style lang="scss" scoped>
	.container {
		flex-direction: row;
		height: 100%;
		
		.left {
			box-sizing: border-box;
			width: 30vw;
			border-top: 1px solid #e5e5e5;
			height: 100%;
			
			.scroller {
				height: 100%;
				
				.left-item {
					padding: 20rpx;
				}
				.set-book {
					height: 30rpx;
					line-height: 30rpx;
					text-align: center;
					margin: 20rpx;
				}
				.display-view {
					height: 10rpx;
				}
				.active {
					background-color: var(--light-bg-color);
				}
			}
		}
		
		.right {
			width: 70vw;
			background-color: var(--light-bg-color);
			overflow-y: auto;
			
			.add-classify {
				display: block;
				width: 80%;
				height: 10px;
				line-height: 10px;
				margin: 10rpx auto;
			}
		}
		
	}
	
	.popup-container {
		background-color: var(--light-bg-color);
		border-top-left-radius: var(--item-radius);
		border-top-right-radius: var(--item-radius);
		padding: 20rpx;
		height: 65vh;
		display: flex;
		flex-direction: column;
		
		.title {
			font-size: var(--font-size-lg);
			font-weight: bold;
			text-align: center;
			margin-bottom: 20rpx;
		}
		.scroller {
			flex: 1;
			height: 0;
		}
		
		.add-book-form {
			width: 100%;
			
			.bottom {
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				
				.btn {
					flex: 1;
				}
				.btn-del {
					flex: 1
				}
			}
		}
	}
	
	.swipe-1 {
		display: flex;
		flex-direction: column;
		justify-content: center;
		height: 100%;
		color: var(--reverse-text-color);
		background: lightblue;
		padding: 0 15px;
	}
	
	.swipe-2 {
		display: flex;
		flex-direction: column;
		justify-content: center;
		height: 100%;
		color: var(--reverse-text-color);
		background: lightcoral;
		padding: 0 15px;
	}
	
</style>