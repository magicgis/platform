package com.junl.wpwx.service.vaccinate;

import com.junl.wpwx.mapper.OrderMapper;
import com.junl.wpwx.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信订单管理Service
 */
@Service
@Transactional(readOnly = true)
public class BsOrderService {
	@Autowired
	private OrderMapper mapper;

	/**
	 * 保存 order
	 */
	public int insertVacOrder(Order order){
		return mapper.insertOrder(order);
	}

	/**
	 * 订单查询
	 */
	public List<Order> findOrderList(Order order){
		return mapper.findOrderList(order);
	}
	public int updateOrderStuatus(Order order){
		return mapper.updateOrderStuatus(order);
	}

	public int updateOrderpayType(Order order){
		return mapper.updateOrderpayType(order);
	}

	/*
	* 订单号查询支付状态
	* */
	public List<Order> findOrderStatus(Order order){
		return mapper.findOrderStatus(order);
	}

}

