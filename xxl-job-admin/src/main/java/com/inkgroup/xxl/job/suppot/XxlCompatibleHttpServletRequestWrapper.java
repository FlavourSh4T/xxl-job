package com.inkgroup.xxl.job.suppot;

import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.rpc.remoting.net.params.XxlRpcRequest;
import com.xxl.rpc.serialize.Serializer;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XxlCompatibleHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] content;

    private RegistryParam registryParam;

    public XxlCompatibleHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        Serializer serializer = Serializer.SerializeEnum.HESSIAN.getSerializer();
        byte[] bytes = FileCopyUtils.copyToByteArray(request.getInputStream());
        XxlRpcRequest rpcXxlRpcRequest = (XxlRpcRequest) serializer.deserialize(bytes, XxlRpcRequest.class);
        if (rpcXxlRpcRequest.getParameters() != null) {
            for (int i = 0; i < rpcXxlRpcRequest.getParameters().length; i++) {
                if (rpcXxlRpcRequest.getParameterTypes()[i] == RegistryParam.class) {
                    RegistryParam registryParam = (RegistryParam) rpcXxlRpcRequest.getParameters()[i];
                    if (registryParam.getRegistryGroup() == null || registryParam.getRegistryGroup().length() <= 0) {
                        registryParam.setRegistryGroup("EXECUTOR");
                        registryParam.setRegistryValue("http://" + registryParam.getRegistryValue() + "/");
                    }
                    this.registryParam = registryParam;
                }
            }
        }
        content = serializer.serialize(rpcXxlRpcRequest);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream is = new ByteArrayInputStream(content);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return is.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    @Override
    public int getContentLength() {
        return content.length;
    }

    @Override
    public long getContentLengthLong() {
        return getContentLength();
    }

    public RegistryParam getRegistryParam() {
        return registryParam;
    }
}
