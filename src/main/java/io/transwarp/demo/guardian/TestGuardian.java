package io.transwarp.demo.guardian;

import io.transwarp.guardian.client.GuardianAdmin;
import io.transwarp.guardian.client.GuardianAdminFactory;
import io.transwarp.guardian.client.GuardianClient;
import io.transwarp.guardian.client.GuardianClientFactory;
import io.transwarp.guardian.common.exception.GuardianClientException;
import io.transwarp.guardian.common.model.EntityPermissionVo;
import io.transwarp.guardian.common.model.PermissionVo;

import java.util.ArrayList;
import java.util.List;

public class TestGuardian {

  public static void main(String[] args) throws GuardianClientException {
    GuardianClient client = GuardianClientFactory.getInstance();
    GuardianAdmin gAdmin = GuardianAdminFactory.getInstance();
    PermissionVo perm1 = new PermissionVo("RestAdminImplTest", "RestAdminImpl/Test", "TEST");
    PermissionVo perm2 = new PermissionVo("RestAdminImplTest", "RestAdminImpl/Test", "NON_EXISTING");
    List<PermissionVo> perms = new ArrayList<PermissionVo>();
    perms.add(perm1);
    perms.add(perm2);

    gAdmin.grant(EntityPermissionVo.UserPerm("admin", perm1));

    boolean checkPerm1 = client.checkAccess("admin", perm1);
    boolean checkPerm2 = client.checkAccess("admin", perm2);
    boolean checkPerm3 = client.checkAccess("admin", perms);
    assert checkPerm1;
    assert !checkPerm2;
    assert !checkPerm3;

    gAdmin.delPermission(perm1);
  }
}
