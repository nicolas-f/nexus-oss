/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.proxy.maven.wl.internal.scrape;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Scraper for remote Nginx hosted repositories.
 * 
 * @author cstamas
 */
@Named( NginxIndexScraper.ID )
@Singleton
public class NginxIndexScraper
    extends AbstractGeneratedIndexPageScraper
{
    protected static final String ID = "nginx-index";

    /**
     * Default constructor.
     */
    public NginxIndexScraper()
    {
        super( 5000, ID ); // 5th by popularity
    }

    @Override
    protected String getTargetedServer()
    {
        return "Nginx Index Page";
    }

    @Override
    protected Element getParentDirectoryElement( final ScrapeContext context, final Document document )
    {
        final Document doc = Jsoup.parseBodyFragment( "<a href=\"../\">../</a>", document.baseUri() );
        return doc.getElementsByTag( "a" ).first();
    }

    @Override
    protected RemoteDetectionResult detectRemoteRepository( final ScrapeContext context,
                                                            final HttpResponse rootResponse, final Document rootDocument )
    {
        final RemoteDetectionResult result = super.detectRemoteRepository( context, rootResponse, rootDocument );
        if ( RemoteDetectionResult.RECOGNIZED_SHOULD_BE_SCRAPED == result )
        {
            final Header serverHeader = rootResponse.getFirstHeader( "Server" );
            if ( serverHeader != null && serverHeader.getValue() != null
                && serverHeader.getValue().startsWith( "nginx/" ) )
            {
                return RemoteDetectionResult.RECOGNIZED_SHOULD_BE_SCRAPED;
            }
        }
        return RemoteDetectionResult.UNRECOGNIZED;
    }
}
