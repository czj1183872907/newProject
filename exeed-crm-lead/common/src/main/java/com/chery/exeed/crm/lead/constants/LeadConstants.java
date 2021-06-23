package com.chery.exeed.crm.lead.constants;


import java.util.Arrays;
import java.util.List;

public class LeadConstants {

    // 默认线索跟进计划时间常量
    public static final Integer DEFAULT_LEAD_FOLLOW_PLAN_EXPIRES_DAYS = 1;

    public static final String LEAD_LEVEL_META_NAME = "lead_level";

    public static final String LEAD_STATUS_META_NAME = "lead_status";

    /**
     * 跟进情况 话术 operate_desc
     */
    public static final String DEFEAT_APPROVAL_APPROVE_MESSAGE = "战败申请成功";
    public static final String DEFEAT_APPROVAL_REJECT_MESSAGE = "战败申请失败";
    public static final String DISTRIBUTE_MESSAGE = "线索分配至${key}";

    // 线索状态常量
    public enum LEAD_STATUS_ENUME {
        CREATED(1,"新建"),
        SEND(2,"已下发"),
        SAME_LIST(3,"已撞单"),
        ACCEPT(4,"已失效"),
        REJECT(5,"已拒绝"),
        NOT_DISTRIBUTE(6,"已接受"),
        DISTRIBUTED(7,"已分配"),
        DEFEAT_APPLY(8,"申请战败"),
        LOSE(9,"战败"),
        NOT_ARRIVAL(10,"待到店"),
        NOT_PREDICT(11,"待试驾"),
        PREDICT(12,"已试驾"),
        PRICE_OFFER(13,"已报价"),
        ORDER(14,"已下订"),
        WIN(15,"成交"),
        RETURN_VISIT(16,"已回访"),
        TEST(17,"自测"),
        FOLLOWUP(18,"跟进中"),
        TIMEOUT(19,"已超期");


        private LEAD_STATUS_ENUME(Integer value,String desc) {
            this.value = value;
        }

        private Integer value;

        private String desc;

        public Integer getValue() {
            return value;
        }

        public String getDesc(){
            return desc;
        }

        /**不可编辑的线索状态,新建,撞单,拒绝,申请战败,战败,下定,赢单,已回访*/
        public static List<Integer> cantEditStatusForDealer(){
            return Arrays.asList(CREATED.value,SAME_LIST.value,REJECT.value,DEFEAT_APPLY.value,LOSE.value,ORDER.value,WIN.value,RETURN_VISIT.value,TIMEOUT.value);
        }

        /**可见的线索状态,已下发,拒绝,已接受,已分配,申请战败,战败,待到店,...*/
        public static List<Integer> canEditStatusForDealer(){
            return Arrays.asList(SEND.value,REJECT.value,NOT_DISTRIBUTE.value,DISTRIBUTED.value,
                    DEFEAT_APPLY.value,LOSE.value,NOT_ARRIVAL.value,NOT_PREDICT.value,PREDICT.value,PREDICT.value,
                    ORDER.value,WIN.value,RETURN_VISIT.value);
        }

        public static LEAD_STATUS_ENUME valueOf(int value) {
            switch (value) {
                case 1: return LEAD_STATUS_ENUME.CREATED;
                case 2: return LEAD_STATUS_ENUME.SEND;
                case 3: return LEAD_STATUS_ENUME.SAME_LIST;
                case 5: return LEAD_STATUS_ENUME.REJECT;
                case 6: return LEAD_STATUS_ENUME.NOT_DISTRIBUTE;
                case 7: return LEAD_STATUS_ENUME.DISTRIBUTED;
                case 8: return LEAD_STATUS_ENUME.DEFEAT_APPLY;
                case 9: return LEAD_STATUS_ENUME.LOSE;
                case 10: return LEAD_STATUS_ENUME.NOT_ARRIVAL;
                case 11: return LEAD_STATUS_ENUME.NOT_PREDICT;
                case 12: return LEAD_STATUS_ENUME.PREDICT;
                case 13: return LEAD_STATUS_ENUME.PRICE_OFFER;
                case 14: return LEAD_STATUS_ENUME.ORDER;
                case 15: return LEAD_STATUS_ENUME.WIN;
                case 16: return LEAD_STATUS_ENUME.RETURN_VISIT;
                case 17: return LEAD_STATUS_ENUME.TEST;
                case 18:return LEAD_STATUS_ENUME.FOLLOWUP;
                case 19:return LEAD_STATUS_ENUME.TIMEOUT;
                default: return null;
            }
        }
    }

    // 线索任务 状态常量
    public enum LEAD_TASK_STATUS_ENUME {
        NEW(1,"新建"),
        ASSIGN(2,"已分配"),
        CALLED(3,"首次呼叫完成"),
        FAILED(4,"一次联系不上"),
        REFAILED(5,"二次联系不上"),
        REJECT(6,"三次联系不上"),
        COMPLETED(7,"已完成"),
        DONT_CONTACT(8,"已拒绝"),
        ERROR_NBR(9,"无效电话"),
        TEST(10,"不再联系"),
        FAILED_FOURTH(11,"四次联系不上"),
        FAILED_FIFTH(12,"五次联系不上"),
        FAILED_SIX(13,"六次联系不上"),
        FAILED_SEVEN(14,"七次联系不上"),
        FAILED_EIGHT(15,"八次联系不上"),
        FAILED_NINE(16,"九次联系不上"),
        FAILED_TEN(17,"十次联系不上");

        private LEAD_TASK_STATUS_ENUME(Integer value,String desc) {
            this.value = value;
            this.desc = desc;
        }

        private Integer value;
        private String desc;

        public Integer getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    // 线索跟进 状态常量
    public enum LEAD_FOLLOW_STATUS_ENUME {
        // 未跟进状态
        STATUS_NOT_FOLLOW(0),
        // 已跟进状态
        STATUS_FOLLOWED(1);

        private LEAD_FOLLOW_STATUS_ENUME(Integer value) {
            this.value = value;
        }

        private final Integer value;

        public Integer getValue() {
            return value;
        }
    }

    // 审批状态 状态常量
    public enum DEFEAT_APPROVAL_STATUS_ENUME {
        // 同意战败
        APPROVE_STATUS(1),
        // 拒绝战败
        REJECT_STATUS(2);

        private DEFEAT_APPROVAL_STATUS_ENUME(Integer value) {
            this.value = value;
        }

        private final Integer value;

        public Integer getValue() {
            return value;
        }
    }

    // 跟进业务类型常量
    public enum FOLLOWUP_BIZ_TYPE_ENUME {
        // 跟进
        LEAD_FOLLOWUP(1),
        // 试乘试驾
        PREDICT(2),
        // 报价
        PRICE_OFFER(3),
        // 下订
        ORDER(4),
        // 申请战败
        DEFEAT_APPLY(5),
        // 战败审批
        DEFEAT_APPLY_APPROVAL(6),
        // 线索分配
        DISTRIBUTE(7);

        private FOLLOWUP_BIZ_TYPE_ENUME(Integer value) {
            this.value = value;
        }

        private final Integer value;

        public Integer getValue() {
            return value;
        }
    }

    // 客户状态常量
    public enum CUSTOMER_STATUS_ENUME {
        // 潜客
        POTENTIAL_CUSTOMER(1),
        // 保客
        RETAIN_CUSTOMER(2);

        private CUSTOMER_STATUS_ENUME(Integer value) {
            this.value = value;
        }

        private final Integer value;

        public Integer getValue() {
            return value;
        }
    }

    // 客户状态常量
    public enum IGNORE_DEALER_IS_DISTRIBUTED {
        // 汽车之家
        DCC_QCZJ("102011"),
        // 懂车帝
        DCC_DCD("102012"),
        //易车网
        DCC_YCW("102013"),
        //太平洋汽车网
        DCC_TPYQCW("102014"),
        //D02汽车之家
        DCC_DO_QCZJ("D0201"),
        //D02易车
        DCC_DO_YC("D0202"),
        //D02爱卡
        DCC_DO_AK("D0203"),
        //D02太平洋汽车网
        DCC_DO_TPY("D0204"),
        //D02展厅
        DCC_DO_ZT("D0205"),
        //D02懂车帝
        DCC_DO_DCD("D0206"),
        //D02 17汽车
        DCC_DO_QC("DCC17QC");

        private IGNORE_DEALER_IS_DISTRIBUTED(String value) {
            this.value = value;
        }

        private final String value;

        public String getValue() {
            return value;
        }

        public static List<String> ignoreDealerIsDistributed(){
            return Arrays.asList(DCC_QCZJ.value,DCC_DCD.value,DCC_YCW.value,DCC_TPYQCW.value,
                    DCC_DO_QCZJ.value,DCC_DO_YC.value,DCC_DO_AK.value,DCC_DO_TPY.value,
                    DCC_DO_ZT.value,DCC_DO_DCD.value,DCC_DO_QC.value);
        }
    }
}
