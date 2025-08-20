<template>
	<ls-container-nav title="功能管理">
		<uni-list>
			<template v-for="item in moduleList">
				<uni-list-item :title="item.name" :showSwitch="true" :disabled="disable" :switchChecked="item.checked" @switchChange="(e:any) => switchChange(e, item.id)"></uni-list-item>
			</template>
		</uni-list>
	</ls-container-nav>
</template>

<script setup lang="ts">
	import { onMounted, ref } from 'vue';
	import { post } from '@/utils/request';

	const moduleList = ref([])
	const disable = ref(false)

	const switchChange = (e:any, id:number) => {
		disable.value = true
		const request = {
			moduleId: id,
			checked: e.value
		}
		post('/userModule/save', request, () => {
			disable.value = false
			uni.$emit('refresh-modules')
		})
	}
	onMounted(() => {
		post('/userModule/modules', {}, (data: any) => {
			moduleList.value = data.data
		})
	})
</script>

<style lang="scss" scoped>
</style>