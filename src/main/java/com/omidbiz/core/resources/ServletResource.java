package com.omidbiz.core.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletResource extends HttpServlet
{

    private long startupTime;

    @Override
    public void init()
    {
        this.startupTime = System.currentTimeMillis();
    }

    public  URL getResource(String resourceName)
    {
        URL res = ServletResource.class.getResource(resourceName);
        if (res == null)
            Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (res == null)
            ServletResource.class.getClassLoader().getResource(resourceName);
        return res;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String fname = request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/")+1);
        URL resourceUrl = getResource(String.format("/META-INF/resources/pfaces/%s", fname));
        long latestTimestamp = -1;
        long timestamp = getFileTimestamp(resourceUrl);
        if (timestamp > latestTimestamp)
        {
            latestTimestamp = timestamp;
        }
        long lastModified = (latestTimestamp > this.startupTime ? latestTimestamp : this.startupTime);

        if (request.getDateHeader("If-Modified-Since") != -1)
        {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        if (lastModified != -1)
        {
            lastModified -= lastModified % 1000;
            long requestModifiedSince = request.getDateHeader("If-Modified-Since");
            if (lastModified <= requestModifiedSince)
            {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        }
        response.setDateHeader("Last-Modified", lastModified != -1 ? lastModified : this.startupTime);
        //Cache-Control max-age=86400 1 day
//        response.setHeader("Cache-Control", "must-revalidate");
        response.setHeader("Cache-Control", "max-age=86400");
        InputStream in = null;
        OutputStream outputStream = null;
        try
        {
            in = resourceUrl.openStream();
            if(fname.endsWith(".js"))
                response.setContentType("text/javascript; charset=utf-8");
            else if(fname.endsWith(".css"))
                response.setContentType("text/css; charset=utf-8");
            else
                response.setContentType("text/plain; charset=utf-8");
            outputStream = selectOutputStream(request, response);
            if (in != null)
            {
                byte[] buffer = new byte[1024];
                int read = in.read(buffer);
                while (read != -1)
                {
                    outputStream.write(buffer, 0, read);
                    read = in.read(buffer);
                }
                outputStream.flush();

            }
            else
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        finally
        {
            if(in != null)
                in.close();
            if(outputStream != null)
                outputStream.close();            
        }
    }

    protected long getFileTimestamp(URL url)
    {

        try
        {
            URLConnection resource = url.openConnection();
            long lastModifiedTime = resource.getLastModified();
            // if (logger.isDebugEnabled()) {
            // logger.debug("Last-modified timestamp of " + resource + " is " +
            // lastModifiedTime);
            // }
            return lastModifiedTime;
        }
        catch (IOException ex)
        {
            // logger.warn("Couldn't retrieve last-modified timestamp of [" +
            // resource +
            // "] - using ResourceServlet startup time");
            return -1;
        }
    }

    
    protected OutputStream selectOutputStream(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        String acceptEncoding = request.getHeader("Accept-Encoding");
        String mimeType = response.getContentType();

        if (isGzipEnabled() && acceptEncoding != null && acceptEncoding.length() > 0 && acceptEncoding.indexOf("gzip") > -1
                && isCompressedMimeType(mimeType))
        {
            return new GZIPResponseStream(response);
        }
        else
        {
            return response.getOutputStream();
        }
    }

    protected boolean isCompressedMimeType(String mimeType)
    {
        return mimeType.matches("text/.+");
    }

    protected boolean isGzipEnabled()
    {
        return true;
    }

    /*
     * Copyright 2004-2008 the original author or authors.
     * 
     * Licensed under the Apache License, Version 2.0 (the "License"); you may
     * not use this file except in compliance with the License. You may obtain a
     * copy of the License at
     * 
     * http://www.apache.org/licenses/LICENSE-2.0
     * 
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
     * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
     * License for the specific language governing permissions and limitations
     * under the License.
     * 
     * @See org/springframework/js/resource/ResourceServlet.java
     */
    private class GZIPResponseStream extends ServletOutputStream
    {

        private ByteArrayOutputStream byteStream = null;

        private GZIPOutputStream gzipStream = null;

        private boolean closed = false;

        private HttpServletResponse response = null;

        private ServletOutputStream servletStream = null;

        public GZIPResponseStream(HttpServletResponse response) throws IOException
        {
            super();
            closed = false;
            this.response = response;
            this.servletStream = response.getOutputStream();
            byteStream = new ByteArrayOutputStream();
            gzipStream = new GZIPOutputStream(byteStream);
        }

        @Override
        public void close() throws IOException
        {
            if (closed)
            {
                throw new IOException("This output stream has already been closed");
            }
            gzipStream.finish();

            byte[] bytes = byteStream.toByteArray();

            response.setContentLength(bytes.length);
            response.addHeader("Content-Encoding", "gzip");
            servletStream.write(bytes);
            servletStream.flush();
            servletStream.close();
            closed = true;
        }

        @Override
        public void flush() throws IOException
        {
            if (closed)
            {
                throw new IOException("Cannot flush a closed output stream");
            }
            gzipStream.flush();
        }

        @Override
        public void write(int b) throws IOException
        {
            if (closed)
            {
                throw new IOException("Cannot write to a closed output stream");
            }
            gzipStream.write((byte) b);
        }

        @Override
        public void write(byte b[]) throws IOException
        {
            write(b, 0, b.length);
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException
        {
            if (closed)
            {
                throw new IOException("Cannot write to a closed output stream");
            }
            gzipStream.write(b, off, len);
        }

        @SuppressWarnings("unused")
        public boolean closed()
        {
            return (this.closed);
        }

        @SuppressWarnings("unused")
        public void reset()
        {
            // noop
        }

        @Override
        public boolean isReady()
        {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener)
        {
            
        }
    }

}
