package com.ghl.manage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ghl.manage.dao")
public class NewManageApplication {
    //nohup java -jar xxx.jar > catalina.out 2>&1 &  如不用此命令启动jar包，则服务运行一段时间就会停止
	public static void main(String[] args) {
		SpringApplication.run(NewManageApplication.class, args);
	}

}
