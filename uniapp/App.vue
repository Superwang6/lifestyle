<script>
	import { useJotStore } from '@/stores/jot-store.js'
	import {post} from '@/utils/request.js'
	
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
	#app {
		box-sizing: border-box;
		padding-top: var(--status-bar-height);
		
		position: fixed;
		top: 0;
		right: 0;
		left: 0;
		bottom: 0;
		
	}
</style>
