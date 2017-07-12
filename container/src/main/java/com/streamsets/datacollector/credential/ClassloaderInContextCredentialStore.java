/*
 * Copyright 2017 StreamSets Inc.
 * <p>
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.datacollector.credential;

import com.streamsets.datacollector.config.CredentialStoreDefinition;
import com.streamsets.datacollector.util.LamdaUtil;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.credential.CredentialStore;
import com.streamsets.pipeline.api.impl.Utils;
import com.streamsets.pipeline.lib.util.ExceptionUtils;

import java.util.List;

/**
 * CredentialStore proxy that ensures the CredentialStore is always invoked in the context of its corresponding
 * classloader.
 */
public class ClassloaderInContextCredentialStore implements CredentialStore {
  private final ClassLoader storeClassLoader;
  private final CredentialStore store;

  public ClassloaderInContextCredentialStore(CredentialStoreDefinition definition, CredentialStore store) {
    Utils.checkNotNull(definition, "definition");
    Utils.checkNotNull(store, "store");
    this.storeClassLoader = definition.getStageLibraryDefinition().getClassLoader();
    this.store = store;
  }

  @Override
  public List<ConfigIssue> init(Context context) {
    return LamdaUtil.withClassLoader(storeClassLoader,() -> store.init(context));
  }

  @Override
  public String get(String group, String name, String credentialStoreOptions) throws StageException {
    return LamdaUtil.withClassLoader(storeClassLoader,() -> {
      try {
        return store.get(group, name, credentialStoreOptions);
      } catch (Exception ex) {
        ExceptionUtils.throwUndeclared(ex);
        return null;
      }
    });
  }

  @Override
  public void destroy() {
    LamdaUtil.withClassLoader(storeClassLoader, () -> {store.destroy();return null;});
  }

}