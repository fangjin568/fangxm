package com.sc.service.impl;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sc.dao.ShopDao;
import com.sc.dto.ShopExecution;
import com.sc.entity.Shop;
import com.sc.enums.ShopStateEnum;
import com.sc.exceptions.ShopOperationException;
import com.sc.service.ShopService;
import com.sc.util.ImageUtil;
import com.sc.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, File shopImg) {
		//空值判断
		if(shop==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			throw new ShopOperationException("店铺创建失败");
			//ShopOperationException会回滚exception不会
			/*
			 * if (effectedNum <= 0) {
			 * 
			 * }else { if (shopImg!=null) { //存储图片 try { addShopImg(shop,shopImg); } catch
			 * (Exception e) { throw new
			 * ShopOperationException("addShopImg error:"+e.getMessage()); } //更新店铺的图片地址
			 * effectedNum = shopDao.updateShop(shop); if (effectedNum <= 0) { throw new
			 * ShopOperationException("更新图片地址失败"); } } }
			 */
		} catch (Exception e) {
			/* throw new ShopOperationException("addShop error:"+e.getMessage()); */
			return null;
		}
		
		/* return new ShopExecution(ShopStateEnum.CHECK,shop); */
	}
	private void addShopImg(Shop shop, File shopImg) {
		//获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
		shop.setShopImg(shopImgAddr);
	}

}
