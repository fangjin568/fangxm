package com.sc.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.dto.ShopExecution;
import com.sc.entity.Area;
import com.sc.entity.PersonInfo;
import com.sc.entity.Shop;
import com.sc.entity.ShopCategory;
import com.sc.enums.ShopStateEnum;
import com.sc.service.AreaService;
import com.sc.service.ShopCategoryService;
import com.sc.service.ShopService;
import com.sc.util.HttpServletRequestUtil;
import com.sc.util.ImageUtil;
import com.sc.util.PathUtil;
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	
	@RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	
	@RequestMapping(value = "/registershop",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//1.接收并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","上传图片不能为空");
			return modelMap;
		}
		//2.注册店铺
		if(shop!=null && shopImg!=null) {
			PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);
			shop.setOwner(owner);
			File shopImgFile = new File(PathUtil.getImgBasePath()+ImageUtil.getRandomFileName());
			try {
				shopImgFile.createNewFile();
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
			try {
				inputStreamToFile(shopImg.getInputStream(),shopImgFile);
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
			ShopExecution se =shopService.addShop(shop, shopImgFile);
			if(se.getState() == ShopStateEnum.CHECK.getState()) {
				modelMap.put("success", true);
			}else {
				modelMap.put("errMsg", se.getStateInfo());
			}
			
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","请输入店铺信息");
			return modelMap;
		}
		//3.返回结果
		
	}
	private static void inputStreamToFile(InputStream ins,File file) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while((bytesRead=ins.read(buffer))!=-1) {
				os.write(buffer,0,bytesRead);
			}
			
		}catch (Exception e) {
			throw new RuntimeException("调用inputStreamToFile产生异常:"+e.getMessage());
		}finally {
			try {
				if(os!=null) {
					os.close();
				}
				if(ins!=null) {
					ins.close();
				}
				
			}catch (Exception e) {
				throw new RuntimeException("inputStreamToFile关闭io产生异常:"+e.getMessage());
			}
		}
	}
}
