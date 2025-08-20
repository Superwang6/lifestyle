<template>
	<view class="content">
		<uni-forms ref="addClassifyFormer" :model="classifyInfo">
			<uni-forms-item label="名称" name="name">
				<uni-easyinput placeholder="请输入分类名称" v-model="classifyInfo.name"></uni-easyinput>
			</uni-forms-item>
		</uni-forms>
		<view class="bottom">
			<uni-icons class="btn-del" type="closeempty" @click="closeAdd"></uni-icons>
			<uni-icons class="btn" type="checkmarkempty" @click="addClassifyClick"></uni-icons>
		</view>
	</view>
</template>

<script setup lang="ts">
	import { onMounted, ref } from 'vue';
	import { post } from '@/utils/request';
	interface Classify {
		id ?: number,
		name ?: string,
		bookId ?: number
	}
	const props = defineProps<{
		classify ?: Classify,
		bookId : number
	}>()
	const emit = defineEmits<{
		cancel : [],
		confirm : []
	}>()
	const addClassifyFormer = ref(null)
	const classifyInfo = ref<Classify>({
		name: ''
	})
	const closeAdd = () => {
		emit('cancel')
	}
	const addClassifyClick = () => {
		if (props.classify && props.classify.id) {
			addClassifyFormer.value.validate().then((res : any) => {
				const request = {
					'id': props.classify.id,
					'bookId': props.bookId,
					'name': res.name
				}
				post('/jotClassify/modify', request, () => {
					uni.showToast({
						icon: 'none',
						title: '修改成功！'
					})
					emit('confirm')
				})
			}).catch(e => { })
		} else {
			addClassifyFormer.value.validate().then((res : any) => {
				const request = {
					'bookId': props.bookId,
					'name': res.name
				}
				post('/jotClassify/add', request, () => {
					uni.$emit('jot-update')
					uni.showToast({
						icon: 'none',
						title: '添加成功！'
					})
					emit('confirm')
				})
			}).catch(e => { })
		}
	}

	onMounted(() => {
		classifyInfo.value.name = props.classify?.name
		classifyInfo.value.bookId = props.bookId
	})
</script>

<style lang="scss" scoped>
	.content {
		
		.bottom {
			display: flex;
			flex-direction: row;
			justify-content: space-around;
			.btn {
				flex: 1
			}
			.btn-del {
				flex: 1
			}
		}
	}
</style>