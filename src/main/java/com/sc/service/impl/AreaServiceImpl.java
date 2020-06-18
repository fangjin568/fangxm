package com.sc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sc.dao.AreaDao;
import com.sc.entity.Area;
import com.sc.service.AreaService;

public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao areaDao;
	@Override
	public List<Area> getAreaList() {
		
		return areaDao.queryArea();
	}

}
