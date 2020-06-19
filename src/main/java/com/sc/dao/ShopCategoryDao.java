package com.sc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sc.entity.ShopCategory;

public interface ShopCategoryDao {
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")ShopCategory shopCategoryCondition);
}
