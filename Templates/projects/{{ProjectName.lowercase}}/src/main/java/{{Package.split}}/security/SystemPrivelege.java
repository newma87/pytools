package {{Package}}.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-04 10:34:31
 * Description: 系统权限定义列表
 */
public class SystemPrivelege {

    public final static int MAX_ROLE_TYPE = 32;   // 最大的用户类型定义个数

    public final static String ADMIN = "ADMIN";           // 超级用户
    public final static int ADMIN_INT = 1;       // 1

    public final static  String WEB_ADMIN = "WEB_ADMIN"; // 后台管理员
    public final static int WEB_ADMIN_INT = 2;   // 2

    public final static String USER = "USER";        // 普通用户
    public final static int USER_INT = 4; // 4

    public final static String ANONYMOUS = "ANONYMOUS"; // 未登录用户
    private final static int ANONYMOUS_INT = 0; // 0

    private static final Map<Integer, String> PRIVILEGE;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(ADMIN_INT, ADMIN);
        map.put(WEB_ADMIN_INT, WEB_ADMIN);
        map.put(USER_INT, USER);
        map.put(ANONYMOUS_INT, ANONYMOUS);
        PRIVILEGE = Collections.unmodifiableMap(map);
    }

    public static String getPrivilegeName(int authority) {
        return PRIVILEGE.get(authority);
    }

    /**
     * 通过权限的值来获取得到Spring security所需要的GrantedAuthority对象
     * @param authority 用户对象中系统角色值
     * @return
     */
    public static List<GrantedAuthority> getPrivileges(int authority) {
        List<GrantedAuthority> result = new ArrayList<>(MAX_ROLE_TYPE);
        if (authority == ANONYMOUS_INT) {           // 排除掉未认证的情况
            result.add(new SimpleGrantedAuthority(ANONYMOUS));
        } else {
            int i = 0;
            for (; i < MAX_ROLE_TYPE; i++) {
                int type = 1 << i;
                if ((type & authority) > 0 && PRIVILEGE.get(type) != null) {
                    result.add(new SimpleGrantedAuthority(PRIVILEGE.get(type)));
                }
            }
        }
        return result;
    }

    public static Integer getRevertPrivileges(Collection< ? extends GrantedAuthority> authorities) {
        return authorities.stream().mapToInt(author -> {
            if (author.getAuthority() == ADMIN) {
                return ADMIN_INT;
            } else if (author.getAuthority() == WEB_ADMIN) {
                return WEB_ADMIN_INT;
            } else if (author.getAuthority() == USER) {
                return USER_INT;
            }

            return 0;
        }).sum();
    }
}

