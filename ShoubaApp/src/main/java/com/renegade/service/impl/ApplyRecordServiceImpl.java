package com.renegade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.renegade.dao.ApplyRecordDao;
import com.renegade.domain.ApplyRecordDO;
import com.renegade.service.ApplyRecordService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service
public class ApplyRecordServiceImpl implements ApplyRecordService {
	@Autowired
	private ApplyRecordDao applyRecordDao;

	@Override
	public ApplyRecordDO getObjectById(Integer userId) {
		// TODO Auto-generated method stub
		return applyRecordDao.getObjectById(userId);
	}

	
	
}
