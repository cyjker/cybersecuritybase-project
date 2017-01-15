package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.MessageRepository;
import sec.project.repository.AccountRepository;


@Controller
public class MessageController {

   @Autowired
   private MessageRepository messageRepository;
   
   @Autowired
   private AccountRepository accountRepository;

    @RequestMapping(value = "/main", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, @RequestParam String message) {
        Account account = accountRepository.findByUsername(authentication.getName());
        
        Message m = new Message(message);
        m.setAccount(account);
        
        messageRepository.save(m);
        
        return "redirect:/main";
    }
    
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String showmessages(Authentication authentication, Model model) {
        model.addAttribute("messages", accountRepository.findByUsername(authentication.getName()).getMessages());
        model.addAttribute("username", authentication.getName());
        return "main";
    }
    
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String messages(Authentication authentication, Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "messages";
    }
}
