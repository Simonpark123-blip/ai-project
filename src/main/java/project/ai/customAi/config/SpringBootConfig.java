//package project.ai.customAi.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.MDC;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import java.util.Locale;
//import java.util.TimeZone;
//
//@Slf4j
//@SpringBootApplication(scanBasePackages = "project.ai.customAi")
//public class SpringBootConfig {
//
//	public static void main(String[] args) {
//        MDC.put("prefix", "Initialization");
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        Locale.setDefault(Locale.US);
//
//		SpringApplication.run(SpringBootConfig.class, args);
//        MDC.clear();
//	}
//
//}
