package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
@RestController
public class EnvController {
    public static final String PORT = "PORT";
    public static final String MEMORY_LIMIT = "MEMORY_LIMIT";
    public static final String CF_INSTANCE_INDEX = "CF_INSTANCE_INDEX";
    public static final String CF_INSTANCE_ADDR = "CF_INSTANCE_ADDR";

    private final Map<String, String> mEnv;

    public EnvController(@Value("${PORT:NOT SET}") String pPort,
                         @Value("${MEMORY_LIMIT:NOT SET}") String pMemeoryLimit,
                         @Value("${CF_INSTANCE_INDEX:NOT SET}") String pCF_InstanceIndex,
                         @Value("${CF_INSTANCE_ADDR:NOT SET}") String pCF_InstanceAddr) {
        Map<String, String> zMap = new HashMap<>();
        zMap.put(PORT, pPort);
        zMap.put(MEMORY_LIMIT, pMemeoryLimit);
        zMap.put(CF_INSTANCE_INDEX, pCF_InstanceIndex);
        zMap.put(CF_INSTANCE_ADDR, pCF_InstanceAddr);
        mEnv = Collections.unmodifiableMap(zMap);
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return mEnv;
    }
}
