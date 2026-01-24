package project.ai.customAi.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvConfig {

    private final Environment env;

    public EnvConfig(Environment env) {
        this.env = env;
    }

    public String getCustomVar(String name) {
        return env.getProperty(name);
    }
}
