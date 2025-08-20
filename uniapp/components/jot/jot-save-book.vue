<template>
	<view class="add-book-form">
		<uni-forms ref="addBookFormer" :model="bookInfo">
			<uni-forms-item label="名称" name="name">
				<uni-easyinput placeholder="请输入备忘本名称" v-model="bookInfo.name"></uni-easyinput>
			</uni-forms-item>
			<uni-forms-item label="描述" name="description">
				<uni-easyinput placeholder="请输入备忘本描述" v-model="bookInfo.description"></uni-easyinput>
			</uni-forms-item>
		</uni-forms>
		<view class="bottom">
			<uni-icons class="btn-del" type="closeempty" @click="closeAdd"></uni-icons>
			<uni-icons class="btn" type="checkmarkempty" @click="addBookClick"></uni-icons>
		</view>
	</view>
</template>

<script setup lang="ts">
	import { onMounted, ref } from 'vue'
	import { post } from '@/utils/request'
	
	interface Book {
		id ?: number,
		name?: string,
		description?: string
	}

	const props = defineProps<{
		'book' ?: Book
	}>()
	const emit = defineEmits<{
		cancel : [],
		confirm : []
	}>()
	
	const bookInfo = ref({
		name: '',
		description: ''
	})

	const addBookFormer = ref(null)
	const closeAdd = () => {
		emit('cancel')
	}
	const addBookClick = () => {
		if (props?.book?.id) {
			addBookFormer.value.validate().then((res : any) => {
				const request = {
					'id': props.book.id,
					'name': res.name,
					'description': res.description
				}
				post('/jotBook/modify', request, () => {
					uni.$emit('jot-update')
					uni.showToast({
						icon: 'none',
						title: '修改成功！'
					})
					emit('confirm')
				})
			}).catch((e) => {
			})
		} else {
			addBookFormer.value.validate().then((res : any) => {
				const request = {
					'name': res.name,
					'description': res.description
				}
				post('/jotBook/add', request, () => {
					uni.showToast({
						icon: 'none',
						title: '添加成功！'
					})
					emit('confirm')
				})
			}).catch((e) => {
			})
		}
	}
	
	onMounted(() => {
		bookInfo.value.name = props?.book?.name
		bookInfo.value.description = props?.book?.description
	})
</script>

<style lang="scss" scoped>
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
</style>