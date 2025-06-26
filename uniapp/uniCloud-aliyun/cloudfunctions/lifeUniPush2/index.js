'use strict';
const uniPush = uniCloud.getPushManager({
	appId: '__UNI__A38E7E5'
})
exports.main = async (event, context) => {
	console.log('event: ' + JSON.stringify(event));
	console.log('context: ' + JSON.stringify(context));
	const body = JSON.parse(event.body)
	//返回数据给客户端
	return await uniPush.sendMessage({
		push_clientid: body.cid,
		title: body.title,
		content: body.content,
		payload: body.payload
	})
};
