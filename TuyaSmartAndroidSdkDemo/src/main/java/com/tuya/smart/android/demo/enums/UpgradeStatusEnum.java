package com.tuya.smart.android.demo.enums;

/**
 * Created by letian on 16/4/20.
 */
public enum UpgradeStatusEnum {

    /**
     * 0  升级中
     * 1  无升级
     * 2  有强制或提醒升级
     * 3  有检测升级
     */
    //升级中
    UPGRADING(0),

    //无升级
    NO_UPGRADE(1),

    //有强制或提醒升级
    HAS_FORCE_OR_REMIND_UPGRADE(2),

    //有检测升级
    HAS_CHECK_UPGRADE(3);
    private int type;

    UpgradeStatusEnum(int type) {
        this.type = type;
    }

    public static UpgradeStatusEnum to(int type) {
        for (UpgradeStatusEnum mode : values()) {
            if (mode.type == type) {
                return mode;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
