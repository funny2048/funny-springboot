package com.funny.example.enums;

/**
 * @date 2022/1/10 19:13
 * @description 调用来源枚举类
 */
public enum ClientEnum {

    DCC_OPERATE(101, "dcc_operate"),
    ADMIN_OPERATE(102, "admin_operate"),
    TASK_OPERATE(103, "task_operate"),
    MQ_OPERATE(104, "mq_operate"),
    NEW_RETAIL_BUSINESS_OPERATE(105,"new_retail_business"),
    ;

    private int value;

    private String desc;

    ClientEnum(int v, String desc) {
        this.value = v;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(Integer status){
        ClientEnum[] statusEnums = ClientEnum.values();
        String remark = "";
        for(ClientEnum statusEnum : statusEnums){
            if(statusEnum.getValue() == status.intValue()){
                remark = statusEnum.getDesc();
                break;
            }
        }
        return remark;
    }

}
