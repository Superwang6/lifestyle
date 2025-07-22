<template>
	<ls-drawer ref="draw" mode="right">
		<template #header>
			详情
		</template>
		<view class="uni-body">
			<view class="top">
				<view class="title">
					{{ detailItem.title }}
				</view>
				<view class="status">
					{{ detailItem.status == 0 ? "待处理" : (detailItem.status == 1 ? "已处理" : "已忽略") }}
				</view>
			</view>
			<view class="time">
				{{ detailItem.remindTime }}
			</view>
			<view class="des">
				{{ detailItem.description }}
			</view>
			<view v-if="detailItem.classifyName" class="classify">
				{{ detailItem.classifyName }}
			</view>
		</view>
		<template #bottom>
			<view class="card-actions">
				<view class="card-actions-item" @click="modifyStatus(3)">
					<uni-icons type="closeempty" size="25" color="#999"></uni-icons>
					<text class="card-actions-item-text">失败</text>
				</view>
				<view class="card-actions-item" @click="modifyStatus(1)">
					<uni-icons type="checkmarkempty" size="25" color="#999"></uni-icons>
					<text class="card-actions-item-text">完成</text>
				</view>
			</view>
		</template>
	</ls-drawer>
</template>

<script setup>
	import {ref} from 'vue'
	import {post} from '@/utils/request.js'
	import lsDrawer from '@/components/common/ls-drawer.vue'
	
	const emits = defineEmits(['refreshIndex'])
	const detailItem = ref(null)
	const draw = ref(null)
	
	const openDetail = (detail) => {
		detailItem.value = detail
		draw.value.open()
	}
	const modifyDetail = () => {
		uni.navigateTo({
			url: '/pages/jot/jot-add?mode=1&item=' + encodeURIComponent(JSON.stringify(detailItem.value))
		})
	}
	const modifyStatus = (status) => {
		const request = {
			"id": detailItem.value.id,
			"status": status
		}
		post('/jotRecord/modify', request, () => {
			emits('refreshIndex')
			draw.value.close()
			uni.showToast({
				icon: "none",
				title: status == 1 ? "已完成备忘，太棒啦！" : "未完成备忘，勿灰心哦！"
			})
		})
	}
	defineExpose({
		openDetail
	})
</script>

<style lang="scss" scoped>
	.uni-body {
		flex: 1;
		display: flex;
		flex-direction: column;
		
		.top {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			
			.title {
				width: 80%;
				font-size: var(--font-size-llg);
				font-weight: bold;
				margin: 10px 0px 10px 0px;
				
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				display: block;
			}
			.status {
				font-size: var(--font-size-sm)
			}
		}
		
		.time {
			font-size: var(--font-size-ssm);
			color: lightgray
		}
		.des {
			margin-top: 20px;
			font-size: var(--font-size);
			background-color: #EAF1F5;
			border-radius: 10px;
			padding: 10px;
			min-height: 30vh;
			white-space: normal;
			word-break: break-all;
		}
		.classify {
			margin-top: 10px;
			border-radius: var(--button-radius);
			padding: 5px 20px 5px 20px;
			background-color: var(--primary-color);
			font-size: var(--font-size-sm);
			
			align-self: flex-start;
			width: auto;
		}
	}
	
	.card-actions {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		
		.card-actions-item {
			display: flex;
			flex-direction: column;
		}
	}
</style>