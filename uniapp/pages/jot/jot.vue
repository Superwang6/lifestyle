<template>
	<view class="main-container">
		<uni-nav-bar title="备忘录" :border="false" :fixed="true" right-icon="more-filled" @clickRight="moreClick()"
			left-icon="wallet" @clickLeft="openHome()" />
		<uni-search-bar v-model="jotRequest.search" @confirm="searchQueryList()" @cancel="cancel" radius="20"
			bgColor="#F0F1F2" placeholder="搜索"></uni-search-bar>
		<z-paging ref="paging" class="zpaging" v-model="list" @query="queryList" :default-page-size="30" :fixed="false">
			<uni-swipe-action ref="swiper">
				<template v-for="(item,index) in list" :key="index">
					<uni-swipe-action-item :right-options="options" @click="swipeClick($event, item)" :threshold="0">
						<view class="item" @click="detailClick(item)">
							<view class="item-left">
								<view class="left-top">{{item.title}}</view>
								<view class="left-bottom">{{item.description}}</view>
							</view>
							<view class="item-right">
								<view class="right-top">
									{{ item.status == 0 ? '待处理': (item.status == 1 ? '已处理' : '已忽略') }}
								</view>
								<view class="right-bottom">{{item.remindTime}}</view>
							</view>
						</view>
					</uni-swipe-action-item>
				</template>
			</uni-swipe-action>
		</z-paging>
		<jot-detail ref="detail" @refreshIndex="refreshIndex()"></jot-detail>
		<jot-more ref="more" @filterParam="filterParam"></jot-more>
		<jot-home ref="home"></jot-home>
		<uni-fab horizontal="right" vertical="bottom" :pop-menu="false" :pattern="{buttonColor: '#90EE90'}"
			@click="addJot()"></uni-fab>
	</view>
</template>

<script setup>
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		onActivated,
		onMounted,
		ref,
		reactive
	} from 'vue';
	import {
		storeToRefs
	} from 'pinia'
	import JotDetail from '@/components/jot/jot-detail.vue'
	import JotMore from '@/components/jot/jot-more.vue';
	import JotHome from '@/components/jot/jot-home.vue';
	import {
		post
	} from '@/utils/request.js';
	import {
		useJotStore
	} from '@/stores/jot-store.js';

	const jotStore = useJotStore()
	const {
		jotRequest
	} = storeToRefs(jotStore)
	const searchText = ref("")
	const list = ref([])
	const paging = ref(null)
	const queryList = (pageNum, pageSize) => {
		jotRequest.value.pageNum = pageNum
		jotRequest.value.pageSize = pageSize
		post('/jotRecord/page', jotRequest.value, (data) => {
			paging.value.complete(data.data);
		}, () => {
			console.log('query jot error');
			paging.value.complete(false);
		})
	}
	const searchQueryList = () => {
		queryList(1, 30)
	}
	const filterParam = () => {
		paging.value.reload()
	}
	const refreshIndex = () => {
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

	const detail = ref(null)
	const detailClick = (item) => {
		detail.value.openDetail(item)
	}

	const jotAdd = ref(null)
	const addJot = () => {
		if (!jotRequest.value.bookId) {
			queryDefaultBook()
		}
		if (jotRequest.value.bookId) {
			uni.navigateTo({
				url: '/pages/jot/jot-add?mode=0&bookId=' + jotRequest.value.bookId
			})
		}
	}

	const swiper = ref(null)
	const options = ref([{
		text: '编辑',
		style: {
			backgroundColor: '#007aff'
		}
	}])
	const swipeClick = (e, item) => {
		uni.navigateTo({
			url: '/pages/jot/jot-add?mode=1&item=' + encodeURIComponent(JSON.stringify(item))
		})
		swiper.value.closeAll()
	}

	const home = ref(null)
	const openHome = () => {
		home.value.openHome()
	}

	onLoad(() => {
		uni.$on('jot-update', () => {
			refreshIndex()
		})
	})
	onMounted(() => {
		jotStore.initJotRequest()
	})
</script>

<style lang="scss" scoped>
	page {
		height: 100%;
	}

	.main-container {
		height: 100%;
		display: flex;
		flex-direction: column;

		.zpaging {
			flex: 1;

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
						font-size: 20px;
						width: 50vw;
						overflow: hidden;
						white-space: nowrap;
						text-overflow: ellipsis;
						display: block;
					}

					.left-bottom {
						font-size: 12px;
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
						font-size: 12px
					}

					.right-bottom {
						font-size: 10px;
						color: gray;
					}
				}
			}
		}

	}
</style>