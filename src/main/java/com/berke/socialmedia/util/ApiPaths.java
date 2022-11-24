package com.berke.socialmedia.util;

public class ApiPaths {

    private static final String BASE_PATH = "/api";

    public static final class UserCtrl{
        public static final String CTRL = BASE_PATH + "/users";
    }

    public static final class RoleCtrl{
        public static final String CTRL = BASE_PATH + "/roles";
    }

    public static final class PrivilegeCtrl{
        public static final String CTRL = BASE_PATH + "/privileges";
    }

    public static final class AuthCtrl{
        public static final String CTRL = BASE_PATH + "/auth";
    }

    public static final class ProfileCtrl{
        public static final String CTRL = BASE_PATH + "/profiles";
    }


}
