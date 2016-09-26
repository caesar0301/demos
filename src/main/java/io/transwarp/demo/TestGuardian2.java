package io.transwarp.demo;

import io.transwarp.guardian.client.GuardianAdmin;
import io.transwarp.guardian.client.GuardianAdminFactory;
import io.transwarp.guardian.client.GuardianClient;
import io.transwarp.guardian.client.GuardianClientFactory;
import io.transwarp.guardian.common.exception.GuardianClientException;
import io.transwarp.guardian.common.model.EntityPermissionVo;
import io.transwarp.guardian.common.model.PermissionVo;
import io.transwarp.guardian.common.model.RoleVo;
import io.transwarp.guardian.common.model.UserVo;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestGuardian2 {

    public static void main(String[] args) throws GuardianClientException {
        GuardianClient client = GuardianClientFactory.getInstance();
        client.login();
        GuardianAdmin gAdmin = GuardianAdminFactory.getInstance();
        PermissionVo perm1 = new PermissionVo("RestAdminImplTest", "RestAdminImpl/Test", "TEST");
        PermissionVo perm2 = new PermissionVo("RestAdminImplTest", "RestAdminImpl/Test", "NON_EXISTING");
        List<PermissionVo> perms = new ArrayList<PermissionVo>();
        perms.add(perm1);
        perms.add(perm2);

        RoleVo role = new RoleVo();
        role.setRoleName("public");
        LinkedList<RoleVo> roles = new LinkedList<RoleVo>();
        roles.add(role);

        UserVo meme = new UserVo("chenxm");
        meme.setUserPassword("123");
        //meme.setRoles(roles);

        try {
            gAdmin.delUser(meme);
            System.out.println("Succeeded to delete users");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Failed to delete users");
        }

        try {
            gAdmin.addUser(meme);
            System.out.println("Succeeded to add users");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Failed to add users");
        }

        gAdmin.grant(EntityPermissionVo.UserPerm(meme.getUserName(), perm1));
  }
}
