package com.junl.wpwx.mapper;

import java.util.List;

import com.junl.wpwx.model.BsOrder;
import com.junl.wpwx.model.VacOrder;



/**
 * 订单mapper
 * @author fuxin
 * @date 2017年10月18日 下午9:12:08
 * @description 
 *		TODO
 */
public interface VacOrderMapper extends CrudMapper<VacOrder> {

	/**
	 * 保存 bsorder
	 * @author fuxin
	 * @date 2017年10月18日 下午9:11:59
	 * @description 
	 *		TODO
	 * @param tempOrder
	 *
	 */
	int insertBsOrder(BsOrder tempOrder);

	/**
	 * 订单查询
	 * @author fuxin
	 * @date 2017年10月19日 上午11:01:41
	 * @description 
	 *		TODO
	 * @param bsOrder
	 * @return
	 *
	 */
	List<BsOrder> findBsOrderList(BsOrder bsOrder);


	
	
}
