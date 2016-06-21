/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.client.file.options;

import alluxio.CommonTestUtils;
import alluxio.Constants;
import alluxio.client.ClientContext;
import alluxio.client.util.ClientTestUtils;
import alluxio.security.authorization.Permission;
import alluxio.security.group.provider.IdentityUserGroupsMapping;
import alluxio.thrift.CompleteUfsFileTOptions;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for the {@link CompleteUfsFileOptions} class.
 */
public final class CompleteUfsFileOptionsTest {
  /**
   * Tests that building an {@link CompleteUfsFileOptions} with the defaults works.
   */
  @Test
  public void defaultsTest() throws IOException {
    ClientContext.getConf().set(Constants.SECURITY_AUTHENTICATION_TYPE, "SIMPLE");
    ClientContext.getConf().set(Constants.SECURITY_LOGIN_USERNAME, "foo");
    // Use IdentityOwnerGroupMapping to map owner "foo" to group "foo".
    ClientContext.getConf().set(Constants.SECURITY_GROUP_MAPPING,
        IdentityUserGroupsMapping.class.getName());

    CompleteUfsFileOptions options = CompleteUfsFileOptions.defaults();

    Permission expectedPs =
        Permission.defaults().applyFileUMask(ClientContext.getConf());

    Assert.assertEquals("foo", options.getOwner());
    Assert.assertEquals("foo", options.getGroup());
    Assert.assertEquals(expectedPs.getMode().toShort(), options.getMode());
    ClientTestUtils.resetClientContext();
  }

  /**
   * Tests getting and setting fields.
   */
  @Test
  public void fieldsTest() throws IOException {
    CompleteUfsFileOptions options = CompleteUfsFileOptions.defaults();
    String owner = "test-owner";
    String group = "test-group";
    short mode = Constants.DEFAULT_FILE_SYSTEM_MODE;
    options.setOwner(owner);
    options.setGroup(group);
    options.setMode(mode);

    Assert.assertEquals(owner, options.getOwner());
    Assert.assertEquals(group, options.getGroup());
    Assert.assertEquals(mode, options.getMode());
  }

  /**
   * Tests conversion to thrift representation.
   */
  @Test
  public void toThriftTest() throws IOException {
    CompleteUfsFileOptions options = CompleteUfsFileOptions.defaults();
    String owner = "test-owner";
    String group = "test-group";
    short mode = Constants.DEFAULT_FILE_SYSTEM_MODE;

    options.setOwner(owner);
    options.setGroup(group);
    options.setMode(mode);

    CompleteUfsFileTOptions thriftOptions = options.toThrift();
    Assert.assertEquals(owner, thriftOptions.getOwner());
    Assert.assertEquals(group, thriftOptions.getGroup());
    Assert.assertEquals(mode, thriftOptions.getMode());
  }

  @Test
  public void equalsTest() throws Exception {
    CommonTestUtils.testEquals(CompleteUfsFileOptions.class);
  }
}
