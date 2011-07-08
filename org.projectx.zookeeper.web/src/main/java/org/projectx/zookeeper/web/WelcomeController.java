package org.projectx.zookeeper.web;

import java.io.IOException;

import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WelcomeController {
  @Resource
  private Scheduler scheduler;

  @RequestMapping(value = "/welcome", method = RequestMethod.GET)
  public @ResponseBody
  String welcome() throws IOException, SchedulerException {
    return "Welcome to the projectx temple, may your heart be strong and your katana sharp...<br/>Scheduler in standby: "
        + scheduler.isInStandbyMode()
        + "<br/><a href='pause'>Pause the scheduler</a>, <a href='start'>Start the scheduler</a><br/><a href='secure'>Go to secure area (tomcat/tomcat)</a";
  }

  @RequestMapping(value = "/secure", method = RequestMethod.GET)
  public @ResponseBody
  String secure() throws IOException, SchedulerException {
    return "Welcome to the secure shrine of projectxs, please beware of dragons.";
  }

  @RequestMapping(value = "/start", method = RequestMethod.GET)
  public String start() throws IOException, SchedulerException {
    scheduler.start();
    return "welcome";
  }

  @RequestMapping(value = "/pause", method = RequestMethod.GET)
  public String pause() throws IOException, SchedulerException {
    scheduler.standby();
    return "welcome";
  }

}
