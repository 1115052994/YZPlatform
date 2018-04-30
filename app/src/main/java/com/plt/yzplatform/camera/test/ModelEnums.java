package com.plt.yzplatform.camera.test;

/**
 * 车辆模板图片名称和说明
 */

public enum ModelEnums {

	/**
	 * 正前方
	 */
	MODEL_ZHENGQIANFANG("zhengqianfang", "正前方"),
	/**
	 * 前方45度
	 */
	MODEL_QIANFANG45DU("qianfang45", "前方45度"),
	/**
	 * 前大灯
	 */
	MODEL_QIANDADENG("qiandadeng", "前大灯"),
	/**
	 * 侧面
	 */
	MODEL_CEMIAN("cemian", "侧面"),
	/**
	 * 后车灯
	 */
	MODEL_HOUCHEDENG("houchedeng", "后车灯"),
	/**
	 * 正后方
	 */
	MODEL_ZHENGHOUFANG("zhenghoufang", "正后方"),
	/**
	 * 车钥匙
	 */
	MODEL_CHEYAOSHI("cheyaoshi", "车钥匙"),
	/**
	 * 左前车门
	 */
	MODEL_ZHUOQIANCHEMENG("zuoqianchemeng", "左前车门"),
	/**
	 * 车门操控区
	 */
	MODEL_CHIMENGCAOKONGQU("chemenchaokongqu", "车门操控区"),
	/**
	 * 前座椅
	 */
	MODEL_QIANZUOYI("qianzuoyi", "前座椅"),
	/**
	 * 中控区
	 */
	MODEL_ZHONGKONGQU("zhongkongqu", "中控区"),
	/**
	 * 方向盘
	 */
	MODEL_FANGXIANGPAN("fangxiangpan", "方向盘"),
	/**
	 * 仪表盘
	 */
	MODEL_YIBIAOPAN("yibiaopan", "仪表盘"),
	/**
	 * 显示屏
	 */
	MODEL_XIANSHIPING("xianshiping", "显示屏"),
	/**
	 * 档位
	 */
	MODEL_DANGWEI("dangwei", "档位"),
	/**
	 * 内车顶（天窗）
	 */
	MODEL_TIANCHUANG("tianchuang", "内车顶（天窗）"),
	/**
	 * 后座椅
	 */
	MODEL_HOUZUOYI("houzuoyi", "后座椅"),
	/**
	 * 后备箱
	 */
	MODEL_HOUBEIXIANG("houbeixiang", "后备箱"),
	/**
	 * 发动机舱整体
	 */
	MODEL_FADONGJI("fadongjicangzhengti", "发动机舱整体"),
	/**
	 * 底盘
	 */
	MODEL_DIPAN("dipan", "底盘"),
	/**
	 * 车轮
	 */
	MODEL_CHELUN("chelun", "车轮"),
	/**
	 * 灯光控制
	 */
	MODEL_DENGGUANGKONGZHI("dengguangkongzhi", "灯光控制"),
	/**
	 * 雨刷器
	 */
	MODEL_YUSHUAQI("yushuaqi", "雨刷器"),
	/**
	 * 钥匙孔
	 */
	MODEL_YAOSHIKONG("yaoshikong", "钥匙孔"),
	;

	public String name;
	public String remark;

	ModelEnums(String name, String remark){
		this.name = name;
		this.remark = remark;
	}

	/**
	 * 根据枚举名称值，构建排序类型枚举
	 *
	 * @param value
	 * @return
	 * @author LLS
	 */
	public static ModelEnums getByName(String value) {

		ModelEnums[] arr = ModelEnums.values();
		for (int i = 0; null != arr && i < arr.length; i++) {
			ModelEnums enumValue = arr[i];
			if (value.equalsIgnoreCase(enumValue.name)) {
				return enumValue;
			}
		}
		return null;
	}
}
