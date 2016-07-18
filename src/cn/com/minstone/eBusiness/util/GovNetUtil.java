package cn.com.minstone.eBusiness.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.minstone.eBusiness.model.ControlInfo;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.DepartService;

/**
 * 访问政务服务资源统一接口工具类
 * @author admin
 *
 */
public class GovNetUtil {

	/**
	 * 审批接口IP地址
	 */
	private static final String GOVNET_URL = MCubeAppConfig.getInstance().getGovUrl();
//	private static final String GOVNET_URL = "http://192.168.0.152:7001/CommonService";//MCubeAppConfig.getInstance().getGovUrl();//"http://192.168.0.147:7001";
//	private static final String GOVNET_URL = "http://172.21.175.224:7001/CommonService";//现场
	/**
	 * 事项列表接口URL
	 */
	private static final String itemListURL = "/api/apprItem/itemList/1/query.v";
	/**
	 * 事项列表接口URL
	 */
//	private static final String itemFloderListURL = "/api/apprItem/itemFloderList/query.v";
	/**
	 * 事项指南接口URL
	 */
	private static final String itemGuidURL = "/api/apprItem/itemInfo/1/query.v";
	/**
	 * 用户登录验证接口URL
	 */
	private static final String loginURL = "/api/user/login.v";///CommonService/api/user/login.v
	/**
	 * 根据userCode获取审批用户信息接口URL
	 */
	private static final String userInfoURL = "/api/user/apprUserInfo/query.v";
	/**
	 * 获取审批用户列表接口URL
	 */
	private static final String userListURL = "/api/user/apprUserList/query.v";
	/**
	 * 单位或部门列表接口URL
	 * /api/unit/unitList/1/query.v
	 * /CommonService/api/unit/unitList/query.v
	 */
	private static final String deptListURL = "/api/unit/unitList/query.v";
	/**
	 * 单位或部门信息接口URL
	 */
//	private static final String deptInfoURL = "/api/unit/unitInfo/query.v";//api/unit/unitInfo/1/query.v
	/**
	 * 单位或部门信息接口URL
	 */
	private static final String deptInfoURL2_0 = "/api/unit/unitInfo/query.v";//api/unit/unitInfo/1/query.v
	/**
	 * 启动办件URL
	 */
	private static final String startItemURL = "/api/control/startControl/add.v";
	/**
	 * 查询办件进度结果URL
	 */
	private static final String ITEMPROGRESS = "/api/control/controlProgress/query.v";
	/**
	 * 获取某个用户办件进度信息列表URL
	 */
	private static final String ITEMPROGRESSLISTBYUSER = "/api/control/controlProgressList/query.v";
	/**
	 * 根据办件流水号获取办件申请人信息URL
	 */
	private static final String CONTROLGUST = "/api/control/controlCust/query.v";
	/**
	 * 根据办件流水号提交办件URL
	 */
	private static final String SUBMITCONTROL = "/api/control/submitControl/update.v";
	
	private static final String UPDATECONTROLUSER = "/api/control/controlCust/update.v";
	
//	public static final String user = "151209022703774";//现场
//	public static final String secret = "15d0ce7b0a2096e1ca171521898f1682";
//	public static final String user = "151110070843216";
//	public static final String secret = "e667d29d50b7c7139cd0ace015b190fa";
	
	public static final String user = MCubeAppConfig.getInstance().getGovUser();
	public static final String secret = MCubeAppConfig.getInstance().getGovPwd();
	
	private static HttpClient client = null;
	private static HttpClient getInstance() {
		if(client == null) {
			client = new HttpClient();
		}
		return client;
	}
	
	//TODO
	public static void main(String[] args) {

//		final List<String> strAlias = new ArrayList<String>();
//		strAlias.add("zhengwuban");
//		final String content = "政务办别名消息推送测试111";
//		PushUtil.pushNotifyToTagert(strAlias, content);
		
//		Hashtable<String, String> hash = new Hashtable<String, String>();
//		hash.put("name", "张三");
//		hash.put("sex", "男");
//		JSONObject json = new JSONObject(hash);
//		System.out.println(json.toString());
		
//		TestUtil.init();
//		getUserByCode("csr2");
//		java.sql.Date date = TimeUtil.getSqlDate(new Date(), "yyyy/MM/dd HH:mm:ss");
//		LogUtil.log(StringUtil.valiatePhone("02074700073") + "");
		
//		System.out.println(System.currentTimeMillis());
//		login("csr2", "11", new HashMap<String, Integer>());
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		getEbUserInfos("", 2, "10", map);
//		System.out.println("操作后i的值为：" + map.get("page"));
//		getDeparts(1, "999", new HashMap<String, Integer>(), "");
//		getDeptInfo("99999999");
//		getEbItems("", 1, "10", new HashMap<String, Integer>());
//		getItemInfo("1153");
//		startItem("1154", "csr2");
//		System.out.println(TimeUtil.getTimeStyle(System.currentTimeMillis()+"", "yyyy-MM-dd HH:mm"));
		/**
		 * 启动办件
		 * @param 事项流水号
		 * @param 企业用户账号
		 * @return 办件流水号
		 */
//		startItem("4206", "m_hup");
		/**
		 * 更新办件申请人信息
		 */
//		updateControlUser("20164614651", "联想高能", "张三", "", "", "15920911500", "");
		/**
		 * 根据办件流水号提交办件
		 */
//		commitControl("20164614651");//提交办件
		
//		getProgressListByUser("m_hup", "", "", "");
		getItemProgress("20164633117");
		
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new TimerTask() {
//			
//			@Override
//			public void run() {
//				System.out.println("打印定时器：");
//			}
//		}, 1000, 60000);
//		
//		String indexUrl = "http://172.0.0.1:7001/test";
//		System.out.println("输出字符串：" + indexUrl.substring(0, indexUrl.lastIndexOf("/")));
//		
//		StringBuffer strBuffer = new StringBuffer(indexUrl);
//		System.out.println("倒序输出：" + strBuffer.reverse());
	}
	
	/**
	 * 
	 * 获取单位、部门列表
	 * @param pageIndex
	 * @param pageRowNum 每页显示多少行
	 * @param map 必传
	 * @param unitName 非必传
	 * @return
	 */
	public static List<EbDepart> getDeparts(int pageIndex, String pageRowNum, Map<String, Integer> map, String unitName) {
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		String url = GOVNET_URL + deptListURL + "?user=" + user + 
				"&signDate=" + signDate + "&areaSeq=1" + 
				"&sign=" + MD5Util.strToMD5L32(signDate) + 
				"&pageIndex=" + pageIndex;
		if(pageRowNum != null && !pageRowNum.equals("") && MD5Util.isNumeric(pageRowNum)) {
			url = url + "&pageRowNum=" + pageRowNum;
		}
		
		if (unitName != null && !unitName.equals("") && !unitName.equalsIgnoreCase("null") ) {
			try {
				unitName = URLEncoder.encode(unitName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url = url + "&unitName=" + unitName;
		}
		
		GetMethod get = new GetMethod(url);
		get.setRequestHeader("Connection", "close");
//		get.setFollowRedirects(true);//自动处理转发
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				LogUtil.log("getDeparts：解析单位列表data数据：" + resultStr);
			
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					List<EbDepart> list = new ArrayList<EbDepart>();
					
					//TODO 解析json数据，目前接口文档中未给出数据结构出来
					JSONObject data = response.optJSONObject("data");
					
					JSONObject objPage = data.optJSONObject("pageObj");
					map.put("total", objPage.optInt("totalRow"));//总条数
					map.put("pages", objPage.optInt("totalPage"));//总页数
					
					JSONArray dataList = data.optJSONArray("dataList");
					
					
					for (int i = 0; i < dataList.length(); i++) {
						JSONObject obj = dataList.optJSONObject(i);

						int unitSeq = obj.optInt("unitSeq");//单位流水号
						String unitNm = obj.optString("unitName");//单位名称
//						String unitLeader = obj.optString("unitLeader");//单位负责人
//						String unitUID = obj.optString("unitUID");//组织机构代码
//						String orderNum = obj.optString("orderNum");//单位排序号
//						String areaSeq = obj.optString("areaSeq");//地区流水号
//						String pareUnitSeq = obj.optString("pareUnitSeq");//父单位流水号
//						String unitCode = obj.optString("unitCode");//单位编码
//						String pareUnitCode = obj.optString("pareUnitCode");//父单位编码
						String monitorPhone = obj.optString("monitorPhone");//监督电话
						String unitAddr = obj.optString("unitAddr");//单位地址
						
						EbDepart dept = new EbDepart();
						dept.setDepartId(new BigDecimal(unitSeq));
						if(unitNm != null){
							unitNm = unitNm.replace("\n", "");
						}
						dept.setDepartName(unitNm);
//						dept.setUserId(new BigDecimal(unitLeader));
						dept.setDepartType(new BigDecimal(2));//部门类型、区级
						dept.setWorkTel(monitorPhone);
						dept.setWorkAddress(unitAddr);
						
						//从本地数据库中查询是否有该部门
						EbDepart deptInfo = new DepartService().getDeptById(unitSeq+"");
						if(deptInfo != null) {
							if(deptInfo.getUserCode() != null) {//如果存在该部门，再查询是否有部门签收人id
								EbUserInfo userInfo = getUserByCode(deptInfo.getUserCode().toString());
								dept.setUserInfo(userInfo);//设置部门签收人
							}
						}
						
						list.add(dept);
					}

					get.releaseConnection();
					//方法一
//					HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
					//方法二
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					//方法三
//					client.getHttpConnectionManager().closeIdleConnections(0);
					//方法四 在getMethod类后设置http头
//					get.setRequestHeader("Connection", "close");
					return list;
				}else {
					LogUtil.tag("getDeparts：获取单位列表失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("getDeparts：访问失败！" + url);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return null;
	}
	
	/**
	 * 获取某个部门信息
	 * @param unitSeq
	 * @return
	 */
	public static EbDepart getDeptInfo(String unitSeq) {
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + deptInfoURL2_0 + "?user=" + user + 
				"&signDate=" + signDate + "&unitSeq=" + unitSeq + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				LogUtil.log("getDeptInfo：解析单位列表data数据：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					//TODO 解析json数据，目前接口文档中未给出数据结构出来
					JSONObject obj = response.optJSONObject("data");

					int unitSeqT = obj.optInt("unitSeq");//单位流水号
					String unitName = obj.optString("unitName");//单位名称
//					String unitLeader = obj.optString("unitLeader");//单位负责人
//					String unitUID = obj.optString("unitUID");//组织机构代码
//					String orderNum = obj.optString("orderNum");//单位排序号
//					String areaSeq = obj.optString("areaSeq");//地区流水号
//					String pareUnitSeq = obj.optString("pareUnitSeq");//父单位流水号，为0时，单位是合并后的单位
//					String unitCode = obj.optString("unitCode");//单位编码
//					String pareUnitCode = obj.optString("pareUnitCode");//父单位编码
					String monitorPhone = obj.optString("monitorPhone");//监督电话
					String unitAddr = obj.optString("unitAddr");//单位地址
					
					EbDepart dept = new EbDepart();
					dept.setDepartId(new BigDecimal(unitSeqT));
					if(unitName != null){
						unitName = unitName.replace("\n", "");
					}
					dept.setDepartName(unitName);
//					if(unitName.equals(MCubeAppConfig.getInstance().getZwb_name())){
//						dept.setDepartType(new BigDecimal(2));//（ 1 招商局 、 2 政务办 、 3 其他部门）
//					}else if(unitName.equals(MCubeAppConfig.getInstance().getZsj_name())){
//						dept.setDepartType(new BigDecimal(1));
//					}else{
						dept.setDepartType(new BigDecimal(2));
//					}
					dept.setWorkTel(monitorPhone);
					dept.setWorkAddress(unitAddr);

					LogUtil.tag("getDeptInfo：测试执行步奏");

					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return dept;
				}else {
					LogUtil.tag("getDeptInfo：获取单位信息失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("getDeptInfo：访问失败！" + url);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return null;
	}
	
	/**
	 * 从审批接口中获取事项列表
	 * @return
	 */
	public static List<EbItem> getEbItems(String unitSeq, int pageIndex, String pageRowNum, Map<String, Integer> map) {
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + itemListURL + "?user=" + user + 
				"&signDate=" + signDate + "&hasUnit=1" + "&areaSeq=1" + 
				"&sign=" + MD5Util.strToMD5L32(signDate) + 
				"&pageIndex=" + pageIndex;

		if(pageRowNum != null && !pageRowNum.equals("") && MD5Util.isNumeric(pageRowNum)) {
			url = url + "&pageRowNum=" + pageRowNum;
		}
		
		if(unitSeq != null && !"".equals(unitSeq) && !"null".equalsIgnoreCase(unitSeq)) {
			url = url + "&unitSeq=" + unitSeq;
		}
		
		GetMethod get = new GetMethod(url);
		
		List<EbItem> list = new ArrayList<EbItem>();
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				LogUtil.log("getEbItems：解析单位列表data数据：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					//TODO 解析json数据，目前接口文档中未给出数据结构出来
					JSONObject data = response.optJSONObject("data");
					
					JSONObject objPage = data.optJSONObject("pageObj");
					map.put("pages", objPage.optInt("totalPage"));
					map.put("total", objPage.optInt("totalRow"));
					
					JSONArray dataList = data.optJSONArray("dataList");
					for (int i = 0; i < dataList.length(); i++) {
						JSONObject obj = dataList.optJSONObject(i);
						
						String approveSeq = obj.optString("approveSeq");//事项流水号
						String approveName = obj.optString("approveName");//事项名称
//						String itemCode = obj.optString("itemCode");//事项编码
//						String orderNum = obj.optString("orderNum");//事项排序号
//						String isOnlineApply = obj.optString("isOnlineApply");//是否可在线申请。0-否，1-是
						String unitSeqT = obj.optString("unitSeq");//事项所属单位流水号
						String unitName = obj.optString("unitName");//事项所属单位名称
//						String unitUnitSeq = obj.optString("unitUniteSeq");//事项所属合并单位流水号
//						String unitUnitName = obj.optString("unitUniteName");//事项所属合并单位名称
						
						EbItem item = new EbItem();
						item.setItemId(new BigDecimal(approveSeq));
						item.setItemName(approveName);
						item.setItemType(new BigDecimal("1"));//事项来源为审批接口
						item.setDepartId(new BigDecimal(unitSeqT));
						if(unitName != null){
							unitName = unitName.replace("\n", "");
						}
						item.setDepartName(unitName);
						
						list.add(item);
					}
					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return list;
				}else {
					LogUtil.tag("getEbItems：获取事项列表失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("getEbItems：访问失败！" + url);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return null;
	}
	
	/**
	 * 根据事项流水号获取事项办事指南信息
	 * @param approveSeq
	 * @return
	 */
	public static EbItem getItemInfo(String approveSeq) {
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + itemGuidURL + "?approveSeq=" + approveSeq + 
				"&user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				LogUtil.log("getItemInfo：解析单位列表data数据：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
					
					String apprSeq = data.optString("approveSeq");//事项流水号
					String approveName = data.optString("approveName");//事项名称
//					String itemCode = data.optString("itemCode");//事项编码
					String unitSeq = data.optString("unitSeq");//所属单位流水号
					String unitName = data.optString("unitName");//所属单位名称
					String serviceObject = NullUtil.ifNull(data.optString("serviceObject"),"  ");//办理对象（文字）
					String applyRange = NullUtil.ifNull(data.optString("applyRange"),"  ");//办理条件
					
					//所需材料
					String strStuff = "";
					JSONArray stuffs = data.optJSONArray("recStuff");
					if(stuffs != null) {
						for (int i = 0; i < stuffs.length(); i++) {
							JSONObject obj = stuffs.optJSONObject(i);
							String rsName = NullUtil.ifNull(obj.optString("rsName"),"无");//材料/分类名
							String rsRemark = NullUtil.ifNull(obj.optString("rsRemark"),"无");//材料说明
							String rsMemo = NullUtil.ifNull(obj.optString("rsMemo"),"无");//材料份数
							String isNeed = NullUtil.ifNull(obj.optString("isNeed"),"无");//是否必须上传
//							String type = obj.optString("type");//类型。1-分类，2-材料
//							String typeId = obj.optString("typeId");//分类id
//							String pareTypeId = obj.optString("pareTypeId");//所属分类id,当类型为2时有值，否则为null

//							if(i < stuffs.length() - 1) {
//								strStuff += "材料名：" + rsName + ", 材料说明：" + rsRemark + ", 所需材料份数：" + rsMemo + 
//										", 是否必须上传：" + isNeed + ";";
//							}else {
//								strStuff += "材料名：" + rsName + ", 材料说明：" + rsRemark + ", 所需材料份数：" + rsMemo + 
//										", 是否必须上传：" + isNeed;
//							}
/*****************2015-11-11 feini******************************/							
							if(i < stuffs.length() - 1) {
								if(!rsName.equals("无")){
									strStuff += (i+1) +"、"+rsName;
								}
								if(!rsRemark.equals("无")){
									strStuff += ","+rsRemark;
								}
								if(!rsMemo.equals("无")){
									strStuff += ","+rsMemo;
								}
								strStuff +=  ";<br>";
//								strStuff += "材料名：" + rsName + ",<br> 材料说明：" + rsRemark + ",<br> 所需材料份数：" + rsMemo + 
//										", <br>是否必须上传：" + isNeed + ";<br><br>";
							}else {
								if(!rsName.equals("无")){
									strStuff += (i+1) +"、"+rsName;
								}
								if(!rsRemark.equals("无")){
									strStuff += ","+rsRemark;
								}
								if(!rsMemo.equals("无")){
									strStuff += ","+rsMemo;
								}
//								strStuff += "材料名：" + rsName + ",<br> 材料说明：" + rsRemark + ", <br>所需材料份数：" + rsMemo + 
//										", <br>是否必须上传：" + isNeed;
							}
/****************************************************/
						}
					}
					
					String windowWorkflow = NullUtil.ifNull(data.optString("windowWorkflow"),"无");//窗口办理流程（文字）
					String onlineWorkflow = NullUtil.ifNull(data.optString("onlineWorkflow"),"无");//网上办理流程（文字）
					String legalLimit = NullUtil.ifNull(data.optString("legalLimit"),"无");//法定办理时限（数字）
//					String legalLimitDesc = data.optString("legalLimitDesc");//法定办理时限（文字）
					String promiseLimit = data.optString("promiseLimit");//承诺办理时限（数字）
//					String promiseLimitDesc = data.optString("promiseLimitDesc");//承诺办理时限（文字）
					
					//办事窗口
					String strWins = "";
					JSONArray apprWins = data.optJSONArray("approveWin");
					if(apprWins != null) {
						for (int i = 0; i < apprWins.length(); i++) {
							JSONObject obj = apprWins.optJSONObject(i);
							
							String winName = NullUtil.ifNull(obj.optString("winName"),"无");//窗口名称
							String workTime = NullUtil.ifNull(obj.optString("workTime"),"无");//办公时间
							String winAddr = NullUtil.ifNull(obj.optString("winAddr"),"无");//窗口地址
							String telphone = NullUtil.ifNull(obj.optString("telphone"),"无");//联系电话
							String trafficGuide = NullUtil.ifNull(obj.optString("trafficGuide"),"无");//公交指引
							String metroGuide = NullUtil.ifNull(obj.optString("metroGuide"),"无");//地铁指引
							
							if(i < stuffs.length() - 1) {
								strWins += "窗口名称：" + winName + ", 办公时间：" + workTime + ", 窗口地址：" + winAddr + 
										", 联系电话：" + telphone + ", 公交路线：" + trafficGuide + ", 地铁路线：" + metroGuide + ";";
							}else {
								strWins += "窗口名称：" + winName + ", 办公时间：" + workTime + ", 窗口地址：" + winAddr + 
										", 联系电话：" + telphone + ", 公交路线：" + trafficGuide + ", 地铁路线：" + metroGuide;
							}
						}
					}
					
					String approveAim = data.optString("approveAim");//收费标准
					
					//办理依据
					String strLaw = "";
					strLaw = data.optString("dealBaseDesc");//办理依据文字说明
//					JSONArray dealBases = data.optJSONArray("dealBase");
//					if(dealBases != null) {
//						for (int i = 0; i < dealBases.length(); i++) {
//							JSONObject obj = dealBases.optJSONObject(i);
//							
//							String lawName = obj.optString("lawName");//法律名称
//							
//							if(i < stuffs.length() - 1) {
//								strLaw += "法律名称：" + lawName + ";";
//							}else {
//								strLaw += "法律名称：" + lawName;
//							}
//						}
//					}
					
					EbItem item = new EbItem();
					//TODO
					item.setItemId(new BigDecimal(NullUtil.ifNull(apprSeq, "")));//事项流水号
					item.setItemName(NullUtil.ifNull(approveName, ""));//事项名称
					item.setDepartId(new BigDecimal(NullUtil.ifNull(unitSeq, 0)));//事项所属部门
					
					//设置部门
					if(unitName != null){
						unitName = unitName.replace("\n", "");
					}
					item.setDepartName(unitName);
					EbDepart dept = new EbDepart();
					dept.setDepartId(new BigDecimal(NullUtil.ifNull(unitSeq, 0)));
					dept.setDepartName(unitName);
					item.setDept(dept);
					
					item.setTransactionObject(serviceObject);//办理对象
					item.setNeedCondition(applyRange);//办理条件
					item.setNeedMaterial(strStuff);//所需材料
//					item.setMaterialAnnex();//材料附件URL
					item.setItemFlow("窗口办理流程：" + windowWorkflow + "\n网上办理流程：" + onlineWorkflow);//办理流程
//					item.setFlowAnnex("");//流程附件
					item.setTransactionWindow(strWins);//办事窗口
					item.setItemCharge(approveAim);//收费标准
//					item.setItemQuestions("");//常见问题
					item.setGistLaw(strLaw);//办理依据
					item.setTimeLimit(new BigDecimal(NullUtil.ifNull(promiseLimit, 15)));//办理时限
					item.setItemType(new BigDecimal("1"));//事项来源

					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return item;
				}else {
					LogUtil.tag("getItemInfo：获取事项办事指南失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("getItemInfo：访问失败！" + url);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return null;
	}
	
	/**
	 * 获取某个单位审批用户列表
	 * @param unitSeq 单位流水号
	 * @param page 页码
	 * @param map 用来存储总页数pages、总数据条数total
	 * @return
	 */
	/**
	 * 获取某个单位审批用户列表
	 * @param unitSeq 单位流水号
	 * @param pageIndex 页码
	 * @param pageRowNum 每行显示多少条数据
	 * @param map 用来存储总页数pages、总数据条数total
	 * @return
	 */
	public static List<EbUserInfo> getEbUserInfos(String unitSeq, int pageIndex, String pageRowNum, Map<String, Integer> map) {
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		String url = GOVNET_URL + userListURL + "?user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate) + "&hasUnit=1" + 
				"&pageIndex=" + pageIndex;

		if(pageRowNum != null && !pageRowNum.equals("") && MD5Util.isNumeric(pageRowNum)) {
			url = url + "&pageRowNum=" + pageRowNum;
		}
		
		if(unitSeq != null && !"".equals(unitSeq) && !"null".equalsIgnoreCase(unitSeq)) {
			url = url + "&unitSeq=" + unitSeq;
		}
		
		System.out.println("请求头：" + url);
		
		GetMethod get = new GetMethod(url);
		
		List<EbUserInfo> list = new ArrayList<EbUserInfo>();
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
				LogUtil.log("GovNetUtil:getEbUserInfos(); 结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
					
					JSONObject objPage = data.optJSONObject("pageObj");
					map.put("pages", objPage.optInt("totalPage"));
					map.put("total", objPage.optInt("totalRow"));
					
					JSONArray dataList = data.optJSONArray("dataList");
					
					System.out.println("用户数据总量：" + dataList.length());
					
					for (int i = 0; i < dataList.length(); i++) {
						JSONObject obj = dataList.getJSONObject(i);
						//TODO 缺少用户类型，缺少单位属性；是招商局还是政务办还是其他单位
//						int userType = obj.optInt("userType");
						
						String userCode1 = obj.optString("userCode");//用户登录账号
						String userName = obj.optString("userName");//名称
//						String userNum = obj.optString("userNum");//工号
						String sex = obj.optString("sex");//性别
//						String mobilePhone = obj.optString("mobilePhone");//手机号码
						String telPhone = obj.optString("telPhone");//直拨电话
//						String subTelPhone = obj.optString("subTelPhone");//个人分机
						String userImgUrl = obj.optString("userImgUrl");//个人照片
						String unitSeqT = obj.optString("unitSeq");//单位编号
						String unitName = obj.optString("unitName");//单位名称
						if(unitName != null){
							unitName = unitName.replace("\n", "");
						}
						EbUserInfo userInfo = new EbUserInfo();
						
						userInfo.setUserAccount(userCode1);
						if(MD5Util.isNumeric(userCode1)) {
							userInfo.setUserId(new BigDecimal(userCode1));
						}
						userInfo.setUserName(userName);
						userInfo.setRoleType(new BigDecimal("1"));//默认为区级办事人员
						String zwbName = MCubeAppConfig.getInstance().getZwb_name();//此为后台设置的区级政务办名称
						if(unitName.equals(zwbName)){
							userInfo.setRoleType(new BigDecimal(5));//5区级政务办
						}
/************************************2015-11-20 feini 更改用户为多个部门**************************************/
						if(MD5Util.isNumeric(unitSeqT)) {
							//如果获取到的用户部门id有多个，则保存单个用户
							userInfo.setDepartId(new BigDecimal(unitSeqT));
							userInfo.setDepartName(unitName);
							userInfo.setIsMDept(new BigDecimal("0"));//IS_MDEPT 是否多角色 1 是、0 否
						}else{
							//用户部门为多角色用户，多个部门
							userInfo.setMyDepartId(unitSeqT);
							userInfo.setMyDepartName(unitName);
							userInfo.setDepartName(unitName);
							userInfo.setIsMDept(new BigDecimal("1"));
							//判断部门是否包含政务办
							String MDeptName = userInfo.getDepartName();
							String[] mdptName =  MDeptName.split(",");
							for(int j = 0; j < mdptName.length; j++){
								String dptName = mdptName[j];
								if(dptName.equals(zwbName)){
									userInfo.setRoleType(new BigDecimal(5));//5区级政务办
									break;
								}
							}
							System.out.print("GOV内打印获取到的多部门名称："+userInfo.getMyDepartName());
						}
/************************************2015-11-20 feini end**************************************/						
						userInfo.setUserPhoto(userImgUrl);
						userInfo.setUserTel(telPhone);
						userInfo.setSex(sex);
						
						list.add(userInfo);
					}

					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return list;
				}else {
					LogUtil.tag("GovNetUtil:getEbUserInfos(); 获取用户列表失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("GovNetUtil:getEbUserInfos(); 访问失败！" + url);
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return list;
	}
	
	/**
	 * 根据userCode获取审批用户信息
	 * @param userCode
	 * @return
	 */
	public static EbUserInfo getUserByCode(String userCode) {
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + userInfoURL + "?userCode=" + userCode + 
				"&user=" + user + "&signDate=" + signDate + "&hasUnit=1" + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
//		System.out.println("请求头：" + url);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
				LogUtil.log("getUserByCode：结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
					JSONObject obj = data.optJSONObject("userInfo");
					
//					int userType = data.optInt("userType");//用户类型 1 审批用户、 2 网厅用户
					
					String userCode1 = obj.optString("userCode");//用户登录账号
					String userName = obj.optString("userName");//名称
//					String userNum = obj.optString("userNum");//工号
//					String sex = obj.optString("sex");//性别
//					String mobilePhone = obj.optString("mobilePhone");//手机号码
					String telPhone = obj.optString("telPhone");//直拨电话
//					String subTelPhone = obj.optString("subTelPhone");//个人分机
					String userImgUrl = obj.optString("userImgUrl");//个人照片
					
					String unitSeq = "";//单位编号
					String unitName = "";//单位名称
					JSONArray units = obj.optJSONArray("unit");
					//TODO 可能要改
//					if(units != null) {
//						for (int i = 0; i < units.length();) {
//							unitSeq = units.getJSONObject(i).optString("unitSeq");
//							unitName = units.getJSONObject(i).optString("unitName");
//							break;
//						}
//					}
					EbUserInfo userInfo = new EbUserInfo();
/***********************2015-11-20 feini 一个用户多个部门****************************************/
					String MdeptId = "";
					String MdeptName = "";
					if(units != null){
						if(units.length() == 1){
							//当用户为单个部门的时候
							for (int i = 0; i < units.length();) {
								unitSeq = units.getJSONObject(i).optString("unitSeq");
								unitName = units.getJSONObject(i).optString("unitName");
								break;
							}
							if(unitSeq != null && !unitSeq.equals("") && !unitSeq.equalsIgnoreCase("null")) {
								userInfo.setDepartId(new BigDecimal(unitSeq));
							}
							userInfo.setDepartName(unitName);
							userInfo.setIsMDept(new BigDecimal("0"));//IS_MDEPT 是否多角色 1 是、0 否
							//设置用户权限标识
							if(unitName.contains(MCubeAppConfig.getInstance().zwb_name)) {
								userInfo.setRoleType(new BigDecimal("5"));//（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
//								userInfo.setAuthority(new BigDecimal("1"));//
							}else {
								//其他单位
								userInfo.setRoleType(new BigDecimal("1"));
//								userInfo.setAuthority(new BigDecimal("5"));
							}
						}else{
							//当用户为多个部门的时候
//							int auth = 0;
							for (int i = 0; i < units.length(); i++) {
								if (!NullUtil.isEmpty(units.getJSONObject(i).optString("unitSeq"))) {
									unitSeq = units.getJSONObject(i).optString("unitSeq");
									unitName = units.getJSONObject(i).optString("unitName");
									if(unitName != null){
										unitName = unitName.replace("\n", "");
									}
									if(i == (units.length()-1)){
										MdeptId += unitSeq;
										MdeptName += unitName;
									}else{
										MdeptId += unitSeq + ",";
										MdeptName += unitName + ",";
									}
								}
							}
							
							//如果MdeptId 是以","结尾的字符串则去除该逗号
							if (!NullUtil.isEmpty(MdeptId) && MdeptId.substring(MdeptId.lastIndexOf(",") + 1).equals("")) {
								MdeptId = MdeptId.substring(0, MdeptId.lastIndexOf(",") - 1);
								if (!NullUtil.isEmpty(MdeptName)) {
									MdeptName = MdeptName.substring(0, MdeptName.lastIndexOf(",") - 1);
								}
							}
							
							if(!NullUtil.isEmpty(MdeptId)) {
								userInfo.setMyDepartId(MdeptId);
							}
							userInfo.setMyDepartName(MdeptName);
							userInfo.setDepartName(unitName);
							if (MdeptId.contains(",")) {
								userInfo.setIsMDept(new BigDecimal("1"));//IS_MDEPT 是否多角色 1 是、0 否
							}else{
								userInfo.setDepartId(new BigDecimal(MdeptId));
								userInfo.setDepartName(MdeptName);
								userInfo.setIsMDept(new BigDecimal("0"));//IS_MDEPT 是否多角色 1 是、0 否
							}
							if(MdeptName.contains(MCubeAppConfig.getInstance().zwb_name)) {
								userInfo.setRoleType(new BigDecimal("5"));//（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
							}else {
								//其他单位
								userInfo.setRoleType(new BigDecimal("1"));
							}
						}
					}
/***********************2015-11-20 feini 一个用户多个部门end****************************************/
					
					userInfo.setUserAccount(userCode1);
//					userInfo.setUserId(new BigDecimal(userNum));
					userInfo.setUserName(userName);
					
					//设置用户权限标识
					userInfo.setAuthority(new BigDecimal("4"));
					
//					if(unitSeq != null && !unitSeq.equals("") && !unitSeq.equalsIgnoreCase("null")) {
//						userInfo.setDepartId(new BigDecimal(unitSeq));
//					}
//					userInfo.setDepartName(unitName);
					userInfo.setUserPhoto(userImgUrl);
					userInfo.setUserTel(telPhone);
//					userInfo.setSex(sex);

					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return userInfo;
				}else {
					LogUtil.tag("getUserByCode;获取用户信息失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("getUserByCode:访问失败！" + url);
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return null;
	}
	
	/**
	 * 登录校验并获取审批用户信息
	 * @param userCode
	 * @param userPWD
	 * @return
	 */
	public static EbUserInfo login(String userCode, String userPWD, Map<String, Integer> map) {
		//先进行登录验证，登录成功后再获取用户信息
		int state = valiateLogin(userCode, userPWD);//valiateLogin(userCode, userPWD);
		if(state == 1) {
			map.put("status", state);
			LogUtil.log("登录成功！");
			
			return getUserByCode(userCode);
		}else if(state == 2) {
			map.put("status", state);
			LogUtil.tag("此用户不存在！");
		}else if(state == 3) {
			map.put("status", state);
			LogUtil.tag("用户密码错误");
		}
		return null;
	}
	
	/**
	 * 进行登录校验
	 * @param userCode
	 * @param userPWD
	 * @return 返回登录验证状态 1-登录成功；2-此用户不存在；3-用户密码有误
	 */
	public static int valiateLogin(String userCode, String userPWD) {
		
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + loginURL + "?userCode=" + userCode + 
				"&userPWD=" + userPWD + "&user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
//				LogUtil.log("结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
					//返回登录验证状态
					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return data.optInt("status");
				}else {
					LogUtil.tag("valiateLogin:登录验证失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("valiateLogin:访问失败！");
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return 2;
	}
	
	/**
	 * 启动办件
	 * @param approveSeq 事项流水号
	 * @param userCode 用户账号
	 * @return controSeq 办件流水号
	 */
	public static String startItem(String approveSeq, String userCode) {
		
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + startItemURL + "?userCode=" + userCode + 
				"&approveSeq=" + approveSeq + "&user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
//		System.out.println("请求头：" + url);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
				LogUtil.log("结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
					//返回登录验证状态
					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return data.optString("controlSeq");
				}else {
					LogUtil.tag("valiateLogin:登录验证失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("valiateLogin:访问失败！" + httpStatus);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return "";
	}
	
	/**
	 * 获取办件进度结果信息
	 * @param controlSeq 办件流水号
	 * @return 
	 */
	public static ControlInfo getItemProgress(String controlSeq) {
		
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + ITEMPROGRESS + "?controlSeq=" + controlSeq + 
				"&user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
		System.out.println("请求头：" + url);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
				LogUtil.log("结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
					
					if (data != null) {
						ControlInfo cInfo = new ControlInfo();
						cInfo.setControlSeq(data.optString("controlSeq"));
						cInfo.setBeginDate(data.optString("beginDate"));
						cInfo.setApproveSeq(data.optString("approveSeq"));
						cInfo.setApproveName(data.optString("approveName"));
						cInfo.setUnitSeq(data.optString("unitSeq"));
						cInfo.setUnitName(data.optString("unitName"));
						cInfo.setUnitUniteSeq(data.optString("unitUniteSeq"));
						cInfo.setUnitUniteName(data.optString("unitUniteName"));
						cInfo.setApproveStatus(data.optString("approveStatus"));
						cInfo.setApproveResult(data.optString("approveResult"));
						cInfo.setCustName(data.optString("custName"));
						cInfo.setCustContactPerson(data.optString("custContactPerson"));
						cInfo.setCustType(data.optString("custType"));
						cInfo.setApproveDealWay(data.optString("approveDealWay"));
						cInfo.setLegalLimit(data.optString("legalLimit"));
						cInfo.setPromiseLimit(data.optString("promiseLimit"));
						
						get.releaseConnection();
						((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
						return cInfo;
					}
				}else {
					LogUtil.tag("valiateLogin:登录验证失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("valiateLogin:访问失败！" + httpStatus);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return null;
	}
	
	/**
	 * 根据用户code获取某个用户办件进度信息列表
	 * @param userCode 用户code
	 * @param approveStatus 办件状态（不传默认为查询所有）
	 * @param pageRowNum 每页显示条数
	 * @param pageIndex 当前页
	 * @return
	 */
	public static List<ControlInfo> getProgressListByUser(String userCode, String approveStatus, 
			String pageRowNum, String pageIndex) {
		
		getInstance();
		List<ControlInfo> list = new ArrayList<ControlInfo>();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + ITEMPROGRESSLISTBYUSER + "?userCode=" + userCode + 
				"&approveStatus=" + approveStatus + "&pageRowNum=" + pageRowNum + 
				"&pageIndex=" + pageIndex + 
				"&user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
		System.out.println("请求头：" + url);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
				LogUtil.log("结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
					JSONArray jArry = data.optJSONArray("dataList");
					if (jArry != null) {
						for (int i = 0; i < jArry.length(); i++) {
							ControlInfo cInfo = new ControlInfo();
							JSONObject job = (JSONObject) jArry.opt(i);
							cInfo.setControlSeq(job.optString("controlSeq"));
							cInfo.setBeginDate(job.optString("beginDate"));
							cInfo.setApproveSeq(job.optString("approveSeq"));
							cInfo.setApproveName(job.optString("approveName"));
							cInfo.setUnitSeq(job.optString("unitSeq"));
							cInfo.setUnitName(job.optString("unitName"));
							cInfo.setUnitUniteSeq(job.optString("unitUniteSeq"));
							cInfo.setUnitUniteName(job.optString("unitUniteName"));
							cInfo.setApproveStatus(job.optString("approveStatus"));
							cInfo.setApproveResult(job.optString("approveResult"));
							list.add(cInfo);
						}
					}
					
					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					return list;
				}else {
					LogUtil.tag("valiateLogin:登录验证失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("valiateLogin:访问失败！" + httpStatus);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return list;
	}
	
	/**
	 * 根据办件流水号获取申请人信息
	 * @param controlSeq 办件流水号
	 * @return 
	 */
	public static String getUserInfoByControlSeq(String controlSeq) {
		
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + CONTROLGUST + "?controlSeq=" + controlSeq + 
				"&user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
		System.out.println("请求头：" + url);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
				LogUtil.log("结果值：" + resultStr);
				
//				JSONObject response = new JSONObject(resultStr);
//				int status = response.optInt("status");
//				if(status == 200) {
//					JSONObject data = response.optJSONObject("data");
//					//返回登录验证状态
//					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
//					return data.optInt("status");
//				}else {
//					LogUtil.tag("valiateLogin:登录验证失败结果描述：" + response.optString("desc") + "\n" + url);
//				}
				return "";
			}else {
				LogUtil.tag("valiateLogin:访问失败！" + httpStatus);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return "";
	}
	
	/**
	 * 根据办件流水号提交办件
	 * @param controlSeq 办件流水号
	 * @return boolean
	 */
	public static boolean commitControl(String controlSeq) {
		
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + SUBMITCONTROL + "?controlSeq=" + controlSeq + 
				"&user=" + user + "&signDate=" + signDate + 
				"&sign=" + MD5Util.strToMD5L32(signDate);
		
		System.out.println("请求头：" + url);
		
		GetMethod get = new GetMethod(url);
		
		try {
			int httpStatus = client.executeMethod(get);
			if(httpStatus == 200) {
				String resultStr = get.getResponseBodyAsString();
				
				LogUtil.log("结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
//					//返回登录验证状态
					get.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					if (!NullUtil.isEmpty(data.optString("controlSeq"))) {
						return true;
					}else {
						return false;
					}
				}else {
					LogUtil.tag("valiateLogin:登录验证失败结果描述：" + response.optString("desc") + "\n" + url);
				}
			}else {
				LogUtil.tag("valiateLogin:访问失败！" + httpStatus);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		get.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		return false;
	}
	
	/**
	 * 更新办件申请人信息
	 * @param controlSeq 办件流水号
	 * @param businessName 企业名称
	 * @param contactName 企业联系人
	 * @param cardType 证件类型 （默认10）
	 * @param cardId 证件账号 （默认：000000000000000000）
	 * @param mobilePhone 手机号码
	 * @param telephhone 固话（可不传）
	 * @return boolean 
	 */
	public static boolean updateControlUser(String controlSeq, String businessName, 
			String contactName, String cardType, String cardId, String mobilePhone, String telephhone) {
		getInstance();
		
		String signDate = TimeUtil.getCurrentTime();
		
		String url = GOVNET_URL + UPDATECONTROLUSER;
		
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		
		method.setParameter("controlSeq", controlSeq);
		method.setParameter("user", user);
		method.setParameter("signDate", signDate);
		method.setParameter("sign", MD5Util.strToMD5L32(signDate));
		
		if(!NullUtil.isEmpty(businessName)) {
			method.setParameter("custName", businessName);
		}
		
		if(!NullUtil.isEmpty(contactName)) {
			method.setParameter("custContactPerson", contactName);
		}
		
		method.setParameter("custCardType", "10");
		method.setParameter("custCardId", "000000000000000000");
		
		
		if(!NullUtil.isEmpty(mobilePhone)) {
			method.setParameter("mobilePhone", mobilePhone);
		}
		
		System.out.println("请求头：" + url);
		
		try {
			int httpStatus = client.executeMethod(method);
			if(httpStatus == 200) {
				String resultStr = method.getResponseBodyAsString();
				
				LogUtil.log("结果值：" + resultStr);
				
				JSONObject response = new JSONObject(resultStr);
				int status = response.optInt("status");
				if(status == 200) {
					JSONObject data = response.optJSONObject("data");
//					//返回登录验证状态
					method.releaseConnection();
					((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
					if (!NullUtil.isEmpty(data.optString("controlSeq"))) {
						return true;
					}else {
						return false;
					}
				}else {
					LogUtil.tag("valiateLogin:登录验证失败结果描述：" + response.optString("desc") + "\n" + url);
				}
				return false;
			}else {
				LogUtil.tag("valiateLogin:访问失败！" + httpStatus);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		method.releaseConnection();
		((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		
		return false;
	}
	
}
