package com.xtzhangbinbin.jpq.enums;


/**
 * 个人订单消费状态
 * 
 * @author LLS
 *
 * @date 2018年4月26日 上午10:25:26
 *
 * @version 1.0
 */
public enum OrderPersState {
	/**
	 * 未支付
	 */
	wzf,
	/**
	 * 已取消
	 */
	yqx,
	/**
	 * 已支付未使用
	 */
	yzfwsy,
	/**
	 * 已使用
	 */
	ysy,
	/**
	 * 申请退款中
	 */
	sqtkz,
	/**
	 * 已退款
	 */
	ytk;
	/**
	 * 根据枚举名称值，构建排序类型枚举
	 * 
	 * @param value
	 * @return
	 * @author LLS
	 */
	public static OrderPersState valueOfAll(String value) {
		OrderPersState[] arr = OrderPersState.values();
		for (int i = 0; null != arr && i < arr.length; i++) {
			OrderPersState enumValue = arr[i];
			if (value.equalsIgnoreCase(enumValue.name())) {
				return enumValue;
			}
		}
		return null;
	}
}
