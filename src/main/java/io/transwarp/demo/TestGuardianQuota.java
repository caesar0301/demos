package io.transwarp.demo;

import io.transwarp.guardian.client.GuardianAdmin;
import io.transwarp.guardian.client.GuardianAdminFactory;
import io.transwarp.guardian.client.GuardianClient;
import io.transwarp.guardian.client.GuardianClientFactory;
import io.transwarp.guardian.common.exception.GuardianClientException;
import io.transwarp.guardian.common.model.QuotaVo;

import java.util.HashMap;
import java.util.Map;

public class TestGuardianQuota {
    private static String QUOTA_COMPONENT = "INCEPTOR_SCHEDULER";
    private static String QUOTA_USER_POOLS = "is.user.permitted.pools";
    private static String QUOTA_USER_QUERIES_POOL = "is.max.active.queries.per.pool";
    private static String QUOTA_USER_SESSIONS = "is.max.active.sessions.per.user";
    private static String QUOTA_USER_QUERIES_SESSION = "is.max.active.queries.per.session";

    public static void main(String[] args) throws GuardianClientException {
        GuardianClient client = GuardianClientFactory.getInstance();
        client.login();
        GuardianAdmin admin = GuardianAdminFactory.getInstance();

        // create new quota
        QuotaVo quota = new QuotaVo(QUOTA_COMPONENT, "chenxm");
        try {
            admin.deleteQuota(quota);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(QUOTA_USER_POOLS, "H0:1,M0:1,L0:1");
        props.put(QUOTA_USER_QUERIES_POOL, "1");
        props.put(QUOTA_USER_SESSIONS, "2");
        props.put(QUOTA_USER_QUERIES_SESSION, "1");
        quota.setProperties(props);
        admin.addQuota(quota);

        System.out.println(client.readQuota(new QuotaVo(QUOTA_COMPONENT, "chenxm")).toString());

        quota = new QuotaVo(QUOTA_COMPONENT, "king");
        try {
            admin.deleteQuota(quota);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        props.clear();
        props.put(QUOTA_USER_POOLS, "**");
        props.put(QUOTA_USER_QUERIES_POOL, "5");
        props.put(QUOTA_USER_SESSIONS, "2");
        props.put(QUOTA_USER_QUERIES_SESSION, "3");
        quota.setProperties(props);
        admin.addQuota(quota);

        System.out.println(client.readQuota(new QuotaVo(QUOTA_COMPONENT, "king")).toString());

        quota = new QuotaVo(QUOTA_COMPONENT, "anonymous");
        try {
            admin.deleteQuota(quota);
        } catch (Exception e) {
            System.out.println(e);
        }
        props.clear();
        props.put(QUOTA_USER_POOLS, "H0,M0");
        props.put(QUOTA_USER_QUERIES_POOL, "1");
        props.put(QUOTA_USER_SESSIONS, "2");
        props.put(QUOTA_USER_QUERIES_SESSION, "1");
        quota.setProperties(props);
        admin.addQuota(quota);

        System.out.println(client.readQuota(new QuotaVo(QUOTA_COMPONENT, "anonymous")).toString());
    }
}
