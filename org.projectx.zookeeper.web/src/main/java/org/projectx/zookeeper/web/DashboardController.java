package org.projectx.zookeeper.web;

import javax.annotation.Resource;

import org.projectx.zookeeper.web.model.NodeTreeView;
import org.projectx.zookeeper.web.view.NodeTreeViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DashboardController {
  @Resource
  NodeTreeViewBuilder nodeTreeViewBuilder;

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public String view(@PathVariable final Long id, final Model model) {
    final NodeTreeView tree = nodeTreeViewBuilder.build();

    model.addAttribute(tree);
    return "dashboard/view";
  }
}
