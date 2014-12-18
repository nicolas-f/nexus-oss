/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2014 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.component.model;

import java.io.InputStream;
import java.util.Map;

import com.google.common.base.Supplier;

/**
 * An asset.
 *
 * @since 3.0
 */
public class Asset
    extends BaseEntity
    implements Entity
{
  private Supplier<InputStream> streamSupplier;

  public Asset(String className) {
    super(className);
  }

  public Asset(String className, Map<String, Object> props) {
    super(className, props);
  }

  public void setStream(final InputStream inputStream) {
    this.streamSupplier = new Supplier<InputStream>() {
      @Override
      public InputStream get() {
        return inputStream;
      }
    };
  }

  public void setStreamSupplier(Supplier<InputStream> streamSupplier) {
    this.streamSupplier = streamSupplier;
  }

  public InputStream openStream() {
    if (streamSupplier == null) {
      return null;
    }
    return streamSupplier.get();
  }
}