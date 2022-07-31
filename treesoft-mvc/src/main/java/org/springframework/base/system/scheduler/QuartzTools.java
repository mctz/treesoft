package org.springframework.base.system.scheduler;

import java.text.ParseException;

import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

public class QuartzTools
{
    public static void startQuartz(String triggerName, String groupName, String corn, QuartzManager quartzManager, boolean oneTimes)
        throws SchedulerException, ParseException
    {
        TriggerKey triggerKey = new TriggerKey(triggerName, groupName);
    }
}
