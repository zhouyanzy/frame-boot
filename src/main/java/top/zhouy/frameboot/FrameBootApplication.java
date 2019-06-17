package top.zhouy.frameboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("top.zhouy.frameboot.mapper")//将项目中对应的mapper类的路径加进来就可以了
public class FrameBootApplication {


    public static void main(String[] args) {
        //解决SpringBoot netty与ES netty 相关jar冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(FrameBootApplication.class, args);
    }

}
