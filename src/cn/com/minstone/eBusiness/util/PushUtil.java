package cn.com.minstone.eBusiness.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * 消息推送工具类
 * 
 * @author admin
 * 
 */
public class PushUtil {

	// static String appId = "oQAn9YSLSo7LFzgVWQG5a4";
	// static String appkey = "dwjO9QABnu8yVHBadUzO11";
	// static String master = "lDoOHpBezKApsP9amUBj44";

//	static String appId = "oHbCrrSDZv6eQ3txFFmmj1";
//	static String appKey = "mvxPwI0r1D8yf5fCl64Bv7";
//	static String appMaster = "EZBy8z8DC08Mr8TDTEYnu";

//	static String appId = "HIifMLMPkj896icRVwheS2";
//	static String appKey = "2Qzz8zJXBi7Gf3rdXWG4M9";
//	static String appMaster = "JsDZIlqwAhAFpsNcVxII99";


//	static String appKey = "19b64d2d7777fec157bf9786";
//	static String appMaster = "af7601b4a60e17b33f15d7b0";
	
	 private String appId;
	 private String appKey;
	 private String appMaster;

	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";

	public static void init(String appid, String appkey, String master) {
		instance.appId = appid;
		instance.appKey = appkey;
		instance.appMaster = master;
	}

	public static PushUtil instance = new PushUtil();

	public static void main(String[] args) {
		final List<String> strAlias = new ArrayList<String>();
		strAlias.add("test");
//		final String content = StringUtil.getJson("1", "政务办别名消息推送测试111");
		
		Hashtable<String, String> map = new Hashtable<String, String>();
		map.put("newsType", "1");
		map.put("content", "aaa");
		map.put("status", "1");
		map.put("programId", "你好" + "");
		
//		System.out.println(StringUtil.getJson(map));
		
//		try {
//			JSONObject job = new JSONObject(StringUtil.getJson(map));
//			System.out.println(job.toString());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
//		new Thread(new Runnable() {
//			
//			public void run() {
//				try {
//					long nowTime = System.currentTimeMillis();
//					long startTime = Long.parseLong("1441728000000");
//					long days = TimeUtil.getWorkDaysBylong(startTime, nowTime);//从签收到现在的天数
//					long deday = days/24/3600/1000;
//					int overTime = (int)deday - 20;//超时时间（工作日）
//					System.out.println("签收日期：" + TimeUtil.getTimeStyle(startTime + "", "yyyy-MM-dd"));
//					System.out.println("当前日期：" + TimeUtil.getTimeStyle(nowTime + "", "yyyy-MM-dd"));
//					System.out.println("当前日期：" + System.currentTimeMillis() * 2);
					pushNotifyToTagert(strAlias, StringUtil.getJson(map));
//					pushTransmissionToList(strAlias, content);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
////				pushAll(content);
//			}
//		}).start();
	}

	/**
	 * 发送通知消息给特定别名组
	 * @param targets
	 * @param content
	 * @throws Exception
	 */
	public static void pushNotifyToTagert(final List<String> targets, final String content) {
		
		new Thread(new Runnable() {
			
			public void run() {
//				JPushClient jClient = new JPushClient(instance.appMaster, instance.appKey);
				JPushClient jClient = new JPushClient(instance.appMaster, instance.appKey);
				
				PushPayload payload = builPushPayload(targets, content);
				
				try {
					PushResult result = jClient.sendPush(payload);
					System.out.println("极光推送结果：" + result.toString());
				} catch (APIConnectionException e) {
					e.printStackTrace();
				} catch (APIRequestException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 激光推送PushPayLoad对象获取
	 * @param content
	 * @return
	 */
	public static PushPayload builPushPayload(List<String> strAlias, String content) {
		
		String strNoty = "";
		JSONObject job = null;
		try {
			job = new JSONObject(content);
			strNoty = job.getString("content");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(
						Audience.newBuilder()
								.addAudienceTarget(
										AudienceTarget.alias(strAlias)// 别名
								)
								.build()
//						Audience.all()
								)
				.setMessage(Message.newBuilder().setMsgContent(content).build())
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setAlert(strNoty)
												.addExtra("content", content)
												.setTitle("易企办").build())
								.addPlatformNotification(
										IosNotification.newBuilder()
												.setAlert(strNoty)
												.addExtra("content", content)
												.setCategory("易企办").build())
								.build())// 通知消息
				.build();
	}

	/**
	 * 为设定别名的用户推送透传消息
	 * 
	 * @param strAlias
	 *            别名组
	 * @param content
	 *            消息内容
	 */
	public static void pushTransmissionToList(List<String> strAlias,
			String content) {
		System.setProperty("gexin_pushList_needDetails", "true");
		final IGtPush push = new IGtPush(host, instance.appKey, instance.appMaster);
		TransmissionTemplate template = null;
		try {
			template = TransmissionTemplateDemo(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ListMessage message = new ListMessage();

		message.setData(template);

		message.setOffline(true);
		message.setOfflineExpireTime(1000 * 3600 * 24);
		// 传递消息到不同类型用户客户端处，推送目标列表
		List<Target> targets = new ArrayList<Target>();
		for (int i = 0; i < strAlias.size(); i++) {
			Target target = new Target();
			target.setAppId(instance.appId);
			// target.setClientId(CID); //按用户ClientId推送
			target.setAlias(strAlias.get(i)); // 按用户别名推送，用户别名需要在客户端进行绑定一个别名
			targets.add(target);
		}

		String taskId = push.getContentId(message, "任务别名_LIST");
		IPushResult ret = push.pushMessageToList(taskId, targets);
		System.out.println(ret.getResponse().toString());
	}

	/**
	 * 透传消息对象
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static TransmissionTemplate TransmissionTemplateDemo(String content)
			throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(instance.appId);
		template.setAppkey(instance.appKey);
		template.setTransmissionType(1);
		template.setTransmissionContent(content);
		return template;
	}

	/**
	 * 发送消息给所有的用户
	 * @param content
	 */
	public static void pushAll(String content) {
		final IGtPush push = new IGtPush(host, instance.appKey,
				instance.appMaster);

		NotificationTemplate temp = new NotificationTemplate();
		temp.setAppId(instance.appId);
		temp.setAppkey(instance.appKey);
		temp.setTitle("易企办");
		temp.setText(content);
		temp.setIsVibrate(true);
		temp.setIsRing(true);
		temp.setIsClearable(true);

		AppMessage msg = new AppMessage();
		msg.setData(temp);
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(instance.appId);
		msg.setAppIdList(appIdList);

//		push.pushMessageToApp(msg);
		IPushResult ret = push.pushMessageToApp(msg);;
		System.out.println(ret.getResponse().toString());
	}
	
	public static void pushToGetuiNot(List<String> targets, String content)
			throws Exception {
		final IGtPush push = new IGtPush(host, instance.appKey, 
				instance.appMaster);

		NotificationTemplate temp = new NotificationTemplate();
		temp.setAppId(instance.appId);
		temp.setAppkey(instance.appKey);
		temp.setTitle("易企办");
		temp.setText(content);
		temp.setIsVibrate(true);
		temp.setIsRing(true);
		temp.setIsClearable(true);

		ListMessage message = new ListMessage();
		message.setData(temp);
		message.setOffline(true);

		List<Target> tagList = new ArrayList<Target>();

		for (String tag : targets) {
			Target target = new Target();
			target.setAppId(instance.appId);
			// target.setClientId(CID); //按用户ClientId推送
			target.setAlias(tag); // 按用户别名推送，用户别名需要在客户端进行绑定一个别名
			tagList.add(target);
		}

		String contentId = push.getContentId(message);
		if (contentId == null) {
			throw new Exception("获取不到contentId");
		}
		
		IPushResult ret = push.pushMessageToList(contentId, tagList);
		System.out.println(ret.getResponse().toString());
	}
}
