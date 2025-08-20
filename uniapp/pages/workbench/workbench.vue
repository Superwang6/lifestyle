<template>
	<ls-container-nav title="工作台" left-icon='none'>
		<view class="container">
			<view>
				<view class="group">
					<uni-grid :highlight="true" :column="3">
						<uni-grid-item @click="goModuleSetting" v-if="moduleList.length == 0">
							<view class="item">
								<uni-icons type="plusempty" size="50" class="item-img"></uni-icons>
								添加功能
							</view>
						</uni-grid-item>
						<uni-grid-item v-else v-for="(mod, index) in moduleList" :index="index" :key="index"
							@click="clickModule(mod)">
							<view class="item">
								<image :src="settingsStore.domain + mod.iconUrl" class="item-img" mode="aspectFit"></image>
								{{mod.name}}
							</view>
						</uni-grid-item>
					</uni-grid>
				</view>
			</view>
		</view>
	</ls-container-nav>
</template>

<script setup lang="ts">
	import { onMounted, ref } from 'vue';
	import { post } from '@/utils/request';
	import { useSettingsStore } from '@/stores/settings-store';

	interface Module {
		iconUrl : string,
		name : string,
		webUrl ?: string
	}

	const settingsStore = useSettingsStore()
	const moduleList = ref([])
	const messageState = ref(false)

	const clickModule = (module : Module) => {
		uni.navigateTo({
			url: module.webUrl
		})
	}
	const goModuleSetting = () => {
		uni.navigateTo({
			url: '/pages/settings/module-setting',
		})
	}
	
	const queryModules = () => {
		post('/userModule/ownModules', {}, (data : any) => {
			moduleList.value = []
			moduleList.value.push(...data.data)
			if(moduleList.value.length == 0) {
				messageState.value = true
			} else {
				messageState.value = false
			}
		})
	}

	onMounted(() => {
		queryModules()
		uni.$on('refresh-modules', () => {
			queryModules()
		})
	})
</script>

<style lang="scss" scoped>
	.group {
		margin: 20rpx;
		background-color: var(--light-bg-color);
		border-radius: var(--item-radius);
		padding: 20rpx;

		.item {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			height: 100%;

			.item-img {
				width: 150rpx;
				height: 150rpx;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
			}
		}
	}
</style>