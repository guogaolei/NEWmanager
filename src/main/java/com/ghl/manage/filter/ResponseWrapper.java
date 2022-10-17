package com.ghl.manage.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */

    private ByteArrayOutputStream buffer=null;
    private ServletOutputStream out=null;
    private PrintWriter writer=null;
    public ResponseWrapper(HttpServletResponse response) throws IOException{
        super(response);
        buffer=new ByteArrayOutputStream();//真正存储数据的流
        out=new WapperedOutputStream(buffer);
        writer= new PrintWriter(new OutputStreamWriter(buffer,this.getCharacterEncoding()));

    }

    public ServletOutputStream getOutputStream(){
        return out;
    }

    public byte[] getResponseData() throws IOException{
        flushBuffer();
        return buffer.toByteArray();

    }
    public void flushBuffer() throws IOException{
        if(null != out){
            out.flush();
        }
        if(null != writer){
            writer.flush();
        }
    }
    public PrintWriter getWriter(){
        return writer;
    }
    private class WapperedOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos=null;
        public WapperedOutputStream(ByteArrayOutputStream stream) {
            bos=stream;
        }
        public void write(int b){
            bos.write(b);
        }
        public void write(byte[] b) throws IOException {
            bos.write(b);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }

    }

    public void reset(){
        buffer.reset();
    }
}
