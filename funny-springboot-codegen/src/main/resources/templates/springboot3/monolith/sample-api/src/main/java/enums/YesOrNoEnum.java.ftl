
package ${package}.enums;

public enum YesOrNoEnum {

    YES(1, "是"),

    NO(0, "否");

    private Integer value;

    private String desc;

    YesOrNoEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
