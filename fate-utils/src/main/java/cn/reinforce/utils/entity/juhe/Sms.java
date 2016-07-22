package cn.reinforce.utils.entity.juhe;

/**
 * 对应短信API服务
 *
 * @author Fate
 */
public class Sms {

    private String reason;

    private long error_code;

    private Result result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getError_code() {
        return error_code;
    }

    public void setError_code(long error_code) {
        this.error_code = error_code;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        /**
         * 短信的id
         */
        private String sid;

        /**
         * 发送数量
         */
        private int count;

        /**
         * 扣款条数
         */
        private int fee;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }


    }
}
