<template>
	<view class="main-container">
		<z-paging ref="paging" v-model="list" @query="queryList">
			<template #top>
				<uni-nav-bar title="备忘录" :border="false" :fixed="true" />
				<uni-search-bar v-model="searchText" @cancel="cancel" radius="20" bgColor="#F0F1F2" placeholder="搜索">
				</uni-search-bar>
			</template>
			<view class="item" v-for="(item,index) in list" :key="index">
				<view class="item-left">
					<view class="left-top">{{item.title}}</view>
					<view class="left-bottom">{{item.description}}</view>
				</view>
				<view class="item-right">
					<view class="right-top">{{ item.status == 0 ? '待处理': (item.status == 1 ? '已处理' : '已忽略') }}</view>
					<view class="right-bottom">{{item.remindTime}}</view>
				</view>
			</view>
			<uni-fab horizontal="right" vertical="bottom" :pop-menu="false" @click="addJot()"></uni-fab>
			<jot-detail @refreshIndex="refreshIndex()" ref="detail"></jot-detail>
		</z-paging>
	</view>
</template>

<script setup>
	import {
		onActivated,
		onMounted,
		ref
	} from 'vue';
	import JotDetail from '@/components/jot/jot-detail.vue'
	import {
		post
	} from '@/utils/request.js';

	const searchText = ref("")
	const list = ref([])
	const refreshIndex = () => {
		list.value = []
		const request = {
			"pageNum": 1,
			"pageSize": 30
		}
		post('/jotRecord/page', request, (data) => {
			list.value.push(...data.data)
		})
	}
	
	const paging = ref(null)
	const queryList = (pageNum, pageSize) => {
		console.log(pageNum,pageSize);
		const request = {
			"pageNum": pageNum,
			"pageSize": pageSize
		}
		post('/jotRecord/page', request, (data) => {
			paging.value.complete(data.data);
		}, () => {
			paging.value.complete(false);
		})
	}
	const cancel = () => {
		searchText.value = ""
	}

	const detail = ref(null)
	const detailClick = (index) => {
		const item = list.value[index]
		console.log(item);
		detail.value.openDetail(item)
	}

	const jotAdd = ref(null)
	const addJot = () => {
		uni.navigateTo({
			url: '/pages/jot/jot-add'
		})
	}

	onActivated(() => {
		refreshIndex()
	})
</script>

<style lang="scss" scoped>
	page {
		height: 100%;
	}

	.main-container {
		height: 100%;
		
		.item {
			padding: 5px 15px 5px 15px;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;
			color: #333333;
			
			.item-left {
				width: 55vw;
				
				.left-top {
					font-size: 18px;
					width: 50vw;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
					display: block;
				}
				.left-bottom {
					font-size: 10px;
					color: gray;
					width: 50vw;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
					display: block;
				}
			}
			.item-right {
				display: flex;
				flex-direction: column;
				align-items: flex-end;
				justify-content: space-between;
				
				.right-top {
					font-size: 10px
				}
				.right-bottom {
					font-size: 8px;
					color: lightgray;
				}
			}
		}

	}
	
</style>