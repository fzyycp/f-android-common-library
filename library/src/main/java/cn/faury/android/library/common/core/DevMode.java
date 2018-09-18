package cn.faury.android.library.common.core;

/**
 * 开发模式
 */
public enum DevMode {
    DEV("dev", "开发"),
    TEST("test", "测试"),
    PROD("prod", "生产"),
    PROD_LOG("prod_log", "生产(带日志)");

    private String code;
    private String desc;

    private DevMode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取模式
     * @param code 编码
     * @return 对象
     */
    public DevMode parse(String code) {
        if (DEV.code.equals(code)) {
            return DEV;
        } else if (TEST.code.equals(code)) {
            return TEST;
        } else if (PROD.code.equals(code)) {
            return PROD;
        } else if (PROD_LOG.code.equals(code)) {
            return PROD_LOG;
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

}
