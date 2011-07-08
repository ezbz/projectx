package org.projectx.zookeeper.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

  private final Map<Long, Account> accounts = new ConcurrentHashMap<Long, Account>();

  @RequestMapping(method = RequestMethod.GET)
  public String getCreateForm(final Model model) {
    model.addAttribute(new Account());
    return "account/createForm";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String create(@Valid final Account account, final BindingResult result) {
    if (result.hasErrors()) {
      return "account/createForm";
    }
    this.accounts.put(account.assignId(), account);
    return "redirect:/account/" + account.getId();
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public String getView(@PathVariable final Long id, final Model model) {
    final Account account = this.accounts.get(id);
    if (account == null) {
      throw new ResourceNotFoundException(id);
    }
    model.addAttribute(account);
    return "account/view";
  }

}
