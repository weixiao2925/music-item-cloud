//package org.example.aggregationserver.config;
//
//import com.alibaba.nacos.common.packagescan.util.ResourceUtils;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.net.ssl.SSLContext;
//
///**
// * SSLContext对象 设置了信任库信息
// *
// * @author yuanzhihao
// * @since 2021/11/26
// */
//@Configuration
//public class SslConfiguration  {
//    @Value("${server.ssl.trust-store}")
//    private String trustStorePath;
//
//    @Value("${server.ssl.trust-store-password}")
//    private String trustStorePassword;
//
//    @Bean
//    public SSLContext sslContext() throws Exception {
//        return SSLContextBuilder.
//                create().
//                loadTrustMaterial(ResourceUtils.getFile(trustStorePath), trustStorePassword.toCharArray()).
//                build();
//    }
//}
//
