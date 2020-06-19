package com.sc.service;

import java.io.File;

import com.sc.dto.ShopExecution;
import com.sc.entity.Shop;

public interface ShopService {
	ShopExecution addShop(Shop shop,File shopImg);
}
