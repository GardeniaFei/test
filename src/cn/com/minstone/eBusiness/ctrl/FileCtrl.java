package cn.com.minstone.eBusiness.ctrl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cn.com.minstone.eBusiness.model.EbFileImg;
import cn.com.minstone.eBusiness.model.EbMapFile;
import cn.com.minstone.eBusiness.service.ImgService;
import cn.com.minstone.eBusiness.service.MapService;
import cn.com.minstone.eBusiness.util.FileHelper;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.PathUtil;

import com.jfinal.core.Controller;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;

public class FileCtrl extends Controller{
	
	/**
	 * 根据任务id获取图片数据列表
	 * @throws IOException 
	 * */
	public void index() throws IOException{
		String distrId = this.getPara("distrId");//获取到对应的分发任务id
		boolean success = false;
		String message = "数据为空";
		
		ImgService imgSer = new ImgService();
		List<EbFileImg> images = imgSer.findImgByDistr(distrId);
		if(images.size()>0){
			success = true;
			message = "成功!";
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			
			for(int i=0;i<images.size();i++){
				EbFileImg image = images.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				
				resultHash.put("imageName",getCheckedString(image.getImageName()));//就把图片名称放进返回数据
				resultHash.put("distrId",getCheckedString(image.getDistrId()+""));//就把图片名称放进返回数据
				String imageUrl = MCubeAppConfig.getInstance().baseUrl;
				imageUrl += "/img/icon?imageName="+image.getImageName();
				resultHash.put("imageUrl",getCheckedString(imageUrl));
				result.add(resultHash);
			}
			this.setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 检查字符串是否为空，为空则传递“”
	 * @param str
	 * @return
	 */
	private String getCheckedString(String str){
		if(str != null){
			return str;
		}
		return "";
	}
	
	/**
	 * 根据图片id获取图片
	 * */
	public void icon() throws IOException{
		String imageName = this.getPara("imageName");
		EbFileImg image = null;
		ImgService imgSer = new ImgService();
		image = imgSer.findImg(imageName);
		if(image != null){
			byte[] bs = image.getBytes("image_conten");
			this.getResponse().setContentLength(bs.length);
			this.getResponse().setContentType("image/*;charset=UTF-8");
			this.getResponse().setHeader("Content-Disposition", "filename="+ URLEncoder.encode(image.getStr("image_name"), "utf-8"));
			this.getResponse().getOutputStream().write(bs);
		}else{
		}
		this.renderNull();
	}

//	/**
//	 * 模拟上传图片页面显示
//	 */
//	public void testImgView(){
//		this.setAttr("distrId", "");//任务分发Id
//		this.setAttr("taskId", "");//任务Id
//		this.setAttr("itemId", "");//事项id
//		this.setAttr("sid", "");//登录标记
//		
//		render(PathUtil.getIndexPath("testUpload.html"));
//	}
	
	/**
	 * 带参数传递测试
	 * @throws IOException 
	 */
	public void uploadMap() throws IOException{
		UploadFile uploadFile = getFile();
		String mapType = this.getPara("mapType");
		
		String mapCode= "";//初始化图片id
		
		//取到文件保存
		if(uploadFile!=null){
			byte[] bs = FileHelper.toBytes(uploadFile.getFile());
			String nameImg = uploadFile.getFileName();
			String[] strs=nameImg.split("\\.");
			mapCode = strs[0];
			
			//检测数据库里图片的id是否已经存在
			MapService imgSer = new MapService();
			EbMapFile img = imgSer.findMapFile(mapCode);
			if(img == null){
				img = new EbMapFile();
				//建立文件路径
				File dirFile = new File(MCubeAppConfig.getInstance().getImagUrl());
				if(!dirFile.exists()){
					dirFile.mkdirs();
				}
					
				File desFile = new File(dirFile, mapCode);//定义文件名
				boolean success = false;
				try {
					 success = uploadFile.getFile().renameTo(desFile);//jfinal批量上传文件时重命名
				} catch (Exception e) {
					e.printStackTrace();
				}
				setAttr("error", success ? 0 : 1);
				if(success){
					img.set("map_id", "EB_MAP_seq.nextval");
					img.set("map_code", mapCode);
//					img.set("map_type",1);
					img.set("map_type", new BigDecimal(mapType));//图片类型，1为中心地图
					img.set("map_CONTEN",bs);
					String mapUrl = MCubeAppConfig.getInstance().baseUrl +"/img/iconMap?imgName="+mapCode;
					img.setMapUrl(mapUrl);
					if(img.save()){
						setAttr("message", "上传成功");
					}
				}else{
					setAttr("message", "上传失败");
				}		
			}
		}
		setAttr("imgName",mapCode);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 根据图片id获取图片
	 * */
	public void iconMap() throws IOException{
		String imageCode = this.getPara("imgName");
		EbMapFile image = null;
		MapService imgSer = new MapService();
		image = imgSer.findMapFile(imageCode);
		if(image != null){
			byte[] bs = image.getBytes("map_conten");
			this.getResponse().setContentLength(bs.length);
			this.getResponse().setContentType("image/*;charset=UTF-8");
			this.getResponse().setHeader("Content-Disposition", "filename="+ URLEncoder.encode(image.get("map_code")+"", "utf-8"));
			this.getResponse().getOutputStream().write(bs);
		}else{
		}
		this.renderNull();
	}
}
