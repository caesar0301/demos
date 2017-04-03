package io.transwarp.demo.guardian;

import io.transwarp.guardian.client.GuardianAdmin;
import io.transwarp.guardian.client.GuardianAdminFactory;
import io.transwarp.guardian.client.GuardianClient;
import io.transwarp.guardian.client.GuardianClientFactory;
import io.transwarp.guardian.common.exception.GuardianClientException;
import io.transwarp.guardian.common.model.EntityRoleVo;
import io.transwarp.guardian.common.model.PrincipalType;
import io.transwarp.guardian.common.model.RoleVo;
import io.transwarp.guardian.common.model.UserVo;

import java.util.LinkedList;
import java.util.List;

public class TestGuardianUser {

    public static void main(String[] args) throws GuardianClientException {
        GuardianClient client = GuardianClientFactory.getInstance();
        client.login();
        GuardianAdmin gAdmin = GuardianAdminFactory.getInstance();

        // Add new admin role
        RoleVo adminRole = new RoleVo("admin");
        try {
            gAdmin.addRole(adminRole);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Add new user
        List<String> roles = new LinkedList<String>();
        roles.add("public");
        UserVo me = new UserVo("chenxm");
        me.setUserPassword("123");
        try {
            gAdmin.delUser(me);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            gAdmin.addUser(me);
        } catch (Exception e) {
            System.out.println(e);
        }

        UserVo test = new UserVo("test");
        test.setUserPassword("123");
        try {
            gAdmin.delUser(test);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            gAdmin.addUser(test);
        } catch (Exception e) {
            System.out.println(e);
        }


        // Add admin user
        roles.add("admin");
        UserVo king = new UserVo("king");
        king.setUserPassword("123");
        try {
            gAdmin.delUser(king);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            gAdmin.addUser(king);
            // assign role admin to user king
            EntityRoleVo entityRoleVo = new EntityRoleVo(king.getUserName(), PrincipalType.USER, "admin", false);
            gAdmin.assign(entityRoleVo);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Add new user
        roles = new LinkedList<String>();
        roles.add("public");
        UserVo anonymous = new UserVo("anonymous");
        anonymous.setUserPassword("123");
        try {
            gAdmin.delUser(anonymous);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            gAdmin.addUser(anonymous);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
