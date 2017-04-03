package io.transwarp.demo.guardian;

import io.transwarp.guardian.common.exception.GuardianClientException;
import io.transwarp.guardian.plugins.inceptor.GuardianSchedulerConfProvider;
import io.transwarp.inceptor.scheduler.AccessType;
import io.transwarp.inceptor.scheduler.PrivilegedEntity;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.hive.conf.HiveConf;

import java.io.IOException;
import java.io.InputStream;


public class TestGuardianConfig {

    public static void main(String[] args) throws IOException, GuardianClientException {
        HiveConf hconf = new HiveConf();
        hconf.set("hive.service.id", "inceptor1");
        GuardianSchedulerConfProvider conf = new GuardianSchedulerConfProvider(hconf);

        // FAIR configuration
        InputStream is = conf.getConfigurationInputStream("fair");
        System.out.println(IOUtils.toString(is));

        // Furion configuration
        is = conf.getConfigurationInputStream("furion");
        System.out.println(IOUtils.toString(is));

        // Check permission
        // chenxm, root
        PrivilegedEntity pe = new PrivilegedEntity(PrivilegedEntity.EntityType.QUEUE, "root");
        conf.checkPermission(AccessType.SUBMIT_APP, pe, "chenxm");


    }
}
