package com.jdroid.javaweb.filter.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import com.jdroid.java.http.HttpService;

public class GZIPResponseStream extends ServletOutputStream {
	
	protected ByteArrayOutputStream baos;
	protected GZIPOutputStream gzipstream;
	protected boolean closed = false;
	protected HttpServletResponse response;
	protected ServletOutputStream output;
	
	public GZIPResponseStream(HttpServletResponse response) throws IOException {
		super();
		closed = false;
		this.response = response;
		output = response.getOutputStream();
		baos = new ByteArrayOutputStream();
		gzipstream = new GZIPOutputStream(baos);
	}
	
	/**
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		if (closed) {
			throw new IOException("This output stream has already been closed");
		}
		gzipstream.finish();
		
		byte[] bytes = baos.toByteArray();
		
		response.addHeader("Content-Length", Integer.toString(bytes.length));
		response.addHeader(HttpService.CONTENT_ENCODING_HEADER, HttpService.GZIP_ENCODING);
		output.write(bytes);
		output.flush();
		output.close();
		closed = true;
	}
	
	@Override
	public void flush() throws IOException {
		if (closed) {
			throw new IOException("Cannot flush a closed output stream");
		}
		gzipstream.flush();
	}
	
	@Override
	public void write(int b) throws IOException {
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		gzipstream.write((byte)b);
	}
	
	@Override
	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}
	
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		gzipstream.write(b, off, len);
	}
}
