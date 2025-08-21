<template>
	<ls-container-nav title="备忘录" >
		<z-paging ref="paging" class="container zpaging" v-model="list" @query="queryList" :default-page-size="30" :fixed="false">
			<template #top>
				<view class="top-info">
					<view class="top-btn" @click="goBook">备忘本</view>
					<view class="top-btn" @click="moreClick">筛选</view>
				</view>
				<uni-search-bar class="search" v-model="jotRequest.search" @confirm="searchQueryList()" @clear="cancel" @cancel="cancel" radius="20"
					bgColor="#F0F1F2" placeholder="搜索"></uni-search-bar>
			</template>
			<uni-swipe-action ref="swiper">
				<template v-for="(item,index) in list" :key="item.id">
					<uni-swipe-action-item class="item-content" :right-options="options" @click="swipeClick($event, item)" :threshold="0">
						<view :class="'item ' + (item.status == 1 ? 'success': (item.status == 3 ? 'fail' : ''))" @click="detailClick(index)">
							<view class="title-area">
								<view class="title text-hidden">{{item.title}}</view>
								<view class="status">
									{{ item.status == 0 ? '待处理': (item.status == 1 ? '已完成' : '已失败') }}
								</view>
							</view>
							<view class="extra-area">
								<view class="remind-info">
									{{ reminInfoFormat(item) }}
								</view>
								<view class="classify text-hidden">{{item.classifyName}}</view>
							</view>
							<view v-if="item.description" class="description" :class="detailIndex == index ? '' : 'text-hidden'">
								{{item.description}}
							</view>
							<view v-if="detailIndex == index && item.status == 0" class="card-actions">
								<view class="card-actions-item fail" @click.stop="modifyStatus(item.id, 3)">
									失败
									<uni-icons type="closeempty" size="18" color="#999"></uni-icons>
								</view>
								<view class="card-actions-item success" @click.stop="modifyStatus(item.id, 1)">
									完成
									<uni-icons type="checkmarkempty" size="18" color="#999"></uni-icons>
								</view>
							</view>
							<uni-icons v-if="item.status == 0" :type="detailIndex == index ? 'up' : 'down'" color="lightgray"></uni-icons>
						</view>
					</uni-swipe-action-item>
				</template>
			</uni-swipe-action>
		</z-paging>
		<jot-more ref="more" @filterParam="filterParam"></jot-more>
		<uni-fab horizontal="right" vertical="bottom" :pop-menu="false" :pattern="{buttonColor: 'var(--primary-color)'}"
			@click="addJot()"></uni-fab>
	</ls-container-nav>
</template>

<script setup lang="ts">
	import {
		onLoad,
		onUnload
	} from '@dcloudio/uni-app'
	import {
		onActivated,
		onMounted,
		ref,
		reactive,
		computed
	} from 'vue';
	import {
		storeToRefs
	} from 'pinia'
	import JotDetail from '@/components/jot/jot-detail.vue'
	import JotMore from '@/components/jot/jot-more.vue';
	import {
		post
	} from '@/utils/request';
	import {
		useJotStore
	} from '@/stores/jot-store';
	import cronstrue from 'cronstrue/i18n';

	const jotStore = useJotStore()
	const {
		jotRequest
	} = storeToRefs(jotStore)
	const searchText = ref("")
	const list = ref([])
	const paging = ref(null)
	const queryList = (pageNum: number, pageSize:number ) => {
		jotRequest.value.pageNum = pageNum
		jotRequest.value.pageSize = pageSize
		post('/jotRecord/page', jotRequest.value, (data: any) => {
			detailIndex.value = -1
			paging.value.complete(data.data);
		}, () => {
			paging.value.complete(false);
		})
	}
	const searchQueryList = () => {
		queryList(1, 30)
	}
	const filterParam = () => {
		detailIndex.value = -1
		paging.value.reload()
	}
	const refreshIndex = () => {
		detailIndex.value = -1
		paging.value.refresh()
	}
	const cancel = () => {
		jotRequest.value.search = null
		queryList(1, 30)
	}
	const more = ref(null)
	const moreClick = () => {
		more.value.openMore()
	}
	const reminInfoFormat = (item: any) => {
		if (item.remindType == 0) {
			return item.remindTime
		} else {
			const remindInfo = JSON.parse(item.remindTimeJson)
			let remindStr = ''
			if (remindInfo && remindInfo.cron) {
				const remindCronStr = cronstrue.toString(remindInfo.cron, {
					locale: "zh_CN",
					use24HourTimeFormat: true
				})
				remindStr += remindCronStr
			}
			if (remindInfo && remindInfo.triggerTimes && remindInfo.triggerTimes > 0) {
				if (remindStr.length > 0) {
					remindStr += ','
				}
				remindStr += '共提醒' + remindInfo.triggerTimes + "次"
			}
			return remindStr
		}
	}

	const detailIndex = ref(-1)
	const detailClick = (index: number) => {
		if(detailIndex.value == index) {
			detailIndex.value = -1
		} else {
			detailIndex.value = index
		}
	}

	const jotAdd = ref(null)
	const addJot = () => {
		if (jotRequest.value.bookId) {
			uni.navigateTo({
				url: '/pages/jot/jot-add?mode=0&bookId=' + jotRequest.value.bookId
			})
		} else {
			uni.showToast({
				icon: 'none',
				title: '请选择备忘录'
			})
		}
	}

	const swiper = ref(null)
	const options = ref([{
			text: '编辑',
			style: {
				backgroundColor: 'var(--primary-color)'
			}
		},
		{
			text: '删除',
			style: {
				backgroundColor: 'var(--dangrous-color)'
			}
		}
	])
	const swipeClick = (e :any, item: any) => {
		if(e.index == 0) {
			uni.navigateTo({
				url: '/pages/jot/jot-add?mode=1',
				success: (res) => {
					res.eventChannel.emit('modify-jot-info', item)
				}
			})
		} else if(e.index == 1) {
			post('/jotRecord/delete/' + item.id, {} , () => {
				refreshIndex()
				uni.showToast({
					title: '删除成功！',
					icon: 'none'
				})
			})
		}
		swiper.value.closeAll()
	}

	const modifyStatus = (id:number, status:number) => {
		const request = {
			"id": id,
			"status": status
		}
		post('/jotRecord/modifyStatus', request, () => {
			uni.showToast({
				icon: "none",
				title: status == 1 ? "已完成备忘，太棒啦！" : "未完成备忘，勿灰心哦！"
			})
			detailIndex.value = -1
			refreshIndex()
		})
	}
	
	const goBook = () => {
		uni.navigateTo({
			url: '/pages/jot/jot-book'
		})
	}

	onLoad(() => {
		uni.$on('jot-update', () => {
			refreshIndex()
		})
	})
	onUnload(() => {
		uni.$off('jot-update')
	})
	onMounted(() => {
		jotStore.initJotRequest()
	})
</script>

<style lang="scss" scoped>
	.zpaging {
		flex: 1;
		.top-info {
			width: 100%;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: center;
			.search {
				flex: 1;
			}
			.top-btn {
				flex: 1;
				background-color: var(--primary-color);
				text-align: center;
				height: 60rpx;
				line-height: 60rpx;
			}
		}
		
		.item-content {
			margin-bottom: 10px;
			
			.item {
				padding: 8px 15px 8px 15px;
				margin: 0 10px 0 10px;
				display: flex;
				flex-direction: column;
				justify-content: space-around;
				color: var(--primary-text-color);
				background-color: var(--light-bg-color);
				border-radius: var(--item-radius);
				
				transform: height 3s ease;
				
				.title-area {
					display: flex;
					flex-direction: row;
					width: 100%;
					
					.title {
						flex: 4;
						font-size: var(--font-size-lg);
					}
					.status {
						flex: 1;
						text-align: right;
						font-size: var(--font-size-ssm);
					}
				}
				.extra-area {
					display: flex;
					flex-direction: row;
					width: 100%;
					
					.remind-info {
						flex: 4;
						font-size: var(--font-size-ssm);
						color: gray;
						margin: 5rpx 0 5rpx 0;
					}
					.classify {
						flex: 2;
						font-size: var(--font-size-sm);
						text-align: right;
					}
				}
				
				.description {
					padding: 5rpx 0 5rpx 0;
					font-size: var(--font-size);
					color: var(--light-text-color);
				}
			}
			.success {
				background-color: #a6f6a675;
			}
			.fail {
				background-color: #f6a7a775;
			}
			
		}
		
		.card-actions {
			display: flex;
			flex-direction: row;
			justify-content: space-around;
			margin: 5px;
			
			.card-actions-item {
				display: flex;
				flex-direction: row;
				justify-content: center;
				height: 30px;
				line-height: 30px;
				font-size: var(--font-size);
				border-radius: var(--button-radius);
				padding: 2px;
				width: 80px;
			}
			.success {
				background-color: lightblue;
			}
			.fail {
				background-color: lightgray;
			}
		}
		
	}
</style>