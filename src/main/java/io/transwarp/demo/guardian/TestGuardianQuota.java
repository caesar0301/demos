package io.transwarp.demo.guardian;

import io.transwarp.guardian.client.GuardianAdmin;
import io.transwarp.guardian.client.GuardianAdminFactory;
import io.transwarp.guardian.client.GuardianClient;
import io.transwarp.guardian.client.GuardianClientFactory;
import io.transwarp.guardian.common.exception.GuardianClientException;
import io.transwarp.guardian.common.model.QuotaVo;

import java.util.*;

public class TestGuardianQuota {
    private static String QUOTA_COMPONENT = "inceptor1";
    private static String QUOTA_USER_POOLS = "is.quota.user.permitted.pools";
    private static String QUOTA_USER_QUERIES_POOL = "is.quota.user.pool";
    private static String QUOTA_USER_SESSIONS = "is.quota.user.session";
    private static String QUOTA_USER_TOTAL = "is.quota.user.total";

    public static void main(String[] args) throws GuardianClientException {
        GuardianClient client = GuardianClientFactory.getInstance();
        client.login();
        GuardianAdmin admin = GuardianAdminFactory.getInstance();

        // create new quota for chen
        List<String> dataSource = Arrays.asList("FURION_SCHEDULER_USER", "chenxm");
        QuotaVo quota = new QuotaVo(QUOTA_COMPONENT, dataSource);
        try {
            admin.deleteQuota(quota);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(QUOTA_USER_POOLS, "H0:1,M0:1,L0:1");
        props.put(QUOTA_USER_QUERIES_POOL, "10");
        props.put(QUOTA_USER_SESSIONS, "10");
        props.put(QUOTA_USER_TOTAL, "1");
        quota.setProperties(props);
        admin.addQuota(quota);

        System.out.println(client.readQuota(new QuotaVo(QUOTA_COMPONENT, dataSource)).toString());
        List<QuotaVo> allQuotas = client.listQuotas(QUOTA_COMPONENT, Arrays.asList("FURION_SCHEDULER_USER"));

        dataSource = Arrays.asList("FURION_SCHEDULER_USER", "king");
        quota = new QuotaVo(QUOTA_COMPONENT, dataSource);
        try {
            admin.deleteQuota(quota);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        props.clear();
        props.put(QUOTA_USER_POOLS, "*");
        props.put(QUOTA_USER_QUERIES_POOL, "5");
        props.put(QUOTA_USER_SESSIONS, "2");
        props.put(QUOTA_USER_TOTAL, "3");
        quota.setProperties(props);
        admin.addQuota(quota);

        System.out.println(client.readQuota(new QuotaVo(QUOTA_COMPONENT, dataSource)).toString());
    }
}
