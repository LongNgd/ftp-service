package vn.atdigital.ftpservice.common;

public interface Constants {

    final class API_RESPONSE {
        private API_RESPONSE() {
            throw new IllegalStateException();
        }

        public static final String RETURN_CODE_SUCCESS = "200";
        public static final String RETURN_CODE_ERROR = "400";
        public static final String RETURN_CODE_ERROR_NOTFOUND = "404";
    }

    final class STATUS_COMMON {
        public static final Boolean RESPONSE_STATUS_TRUE = true;
        public static final Boolean RESPONSE_STATUS_FALSE = false;
    }

    public static final class SESSION_VARIABLE {
        public static final String CURRENT_ACTION_USER = "currentActionUser";
        public static final String SYSDATE = "sysdate";
        public static final String COMMAND = "CurrentCommand";
    }
}
