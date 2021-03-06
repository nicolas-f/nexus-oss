/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-2015 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.httpclient;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.sonatype.sisu.goodies.common.ComponentSupport;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Plan for how to build a {@link HttpClient} instance.
 *
 * @since 3.0
 */
@SuppressWarnings("PackageAccessibility") // FIXME: httpclient usage is producing lots of OSGI warnings in IDEA
public class HttpClientPlan
    extends ComponentSupport
{
  private final HttpClientBuilder client;

  private final ConnectionConfig.Builder connection;

  private final SocketConfig.Builder socket;

  private final RequestConfig.Builder request;

  private final Map<String,String> headers;

  private final Map<String,Object> attributes;

  @Nullable
  private CredentialsProvider credentials;

  @Nullable
  private String userAgent;

  public HttpClientPlan() {
    this.client = HttpClientBuilder.create();
    this.connection = ConnectionConfig.copy(ConnectionConfig.DEFAULT);
    this.socket = SocketConfig.copy(SocketConfig.DEFAULT);
    this.request = RequestConfig.copy(RequestConfig.DEFAULT);
    this.headers = new HashMap<>();
    this.attributes = new HashMap<>();
  }

  public HttpClientBuilder getClient() {
    return client;
  }

  public ConnectionConfig.Builder getConnection() {
    return connection;
  }

  public SocketConfig.Builder getSocket() {
    return socket;
  }

  public RequestConfig.Builder getRequest() {
    return request;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void addCredentials(final AuthScope authScope, final Credentials credentials) {
    // lazy initialized to allow null state to indicate non-customized
    if (this.credentials == null) {
      this.credentials = new BasicCredentialsProvider();
    }
    this.credentials.setCredentials(authScope, credentials);
  }

  @Nullable
  public CredentialsProvider getCredentials() {
    return credentials;
  }

  @Nullable
  public String getUserAgent() { return userAgent; }

  public void setUserAgent(@Nullable final String userAgent) { this.userAgent = userAgent; }

  //
  // Customizer
  //

  public interface Customizer
  {
    void customize(HttpClientPlan plan);
  }
}
