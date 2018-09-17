package com.junl.wpwx.mapper;

import com.junl.wpwx.model.Order;

import java.util.List;


/**
 * 订单mapper
 * @author fuxin
 * @date 2017年10月18日 下午9:12:08
 * @description 
 *		TODO
 */
public interface OrderMapper extends CrudMapper<Order> {

	/**
	 * 保存 order
	 */
	int insertOrder(Order order);

	/**
	 * 订单查询
	 */
	List<Order> findOrderList(Order order);


	int updateOrderStuatus(Order order);

	int updateOrderpayType(Order order);

	List<Order> findOrderStatus(Order order);



}
