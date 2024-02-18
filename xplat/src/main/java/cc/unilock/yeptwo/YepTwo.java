package cc.unilock.yeptwo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YepTwo {
    public static final String MOD_ID = "yeptwo";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Hello from Yep!");
    }
}
