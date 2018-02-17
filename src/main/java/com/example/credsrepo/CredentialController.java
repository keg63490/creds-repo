package com.example.credsrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping(path="/api")
public class CredentialController {
    @Autowired
    private Mailer mailer;

    @Autowired
    private CredentialsRepository credentialRepository;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    String index() { return "redirect:list"; }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addCredential(Credential credential) {
        return  "newEntry";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addCredential(@ModelAttribute("credential") Credential credential, BindingResult bindingResult) {
        Credential cred = new Credential();
        cred.setGroup(credential.getGroup());
        cred.setAccount(credential.getAccount());
        cred.setPassword(credential.getPassword());
        cred.setSalt(credential.getSalt());
        cred.setCreateUser(credential.getCreateUser());
        cred.setCreateTimeStamp(credential.getCreateTimeStamp());
        credentialRepository.save(cred);
        //mailer.setMailSender(mailSender);
        try {
            mailer.sendMail("email address here", "Entry added", "Hello from our app! An entry was added to the list!");
        }
        catch (Exception e){
            System.out.println("Enter a valid email address");
        }

        return  "redirect:list";
    }

    @RequestMapping(path="/list", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        model.addAttribute("list", credentialRepository.findAll());
        return "list";
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String greeting() {
        return "Hello";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String deleteCredential(@RequestParam("id") Integer id) {
        if (credentialRepository.exists(id)) {
            credentialRepository.delete(id);

        }
        return "redirect:list";
    }

    @RequestMapping(path = "/update", method = RequestMethod.GET)
    public String updateCredential(Model model, @RequestParam("id") Integer id, Credential credential) {
        Credential cred = credentialRepository.findOne(id);
        System.out.println(cred.getId());
        model.addAttribute("cred", cred);
        return "update";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateCredential(@ModelAttribute("credential") Credential credential, BindingResult bindingResult) {
        System.out.println(credential.getGroup());
        Credential cred = credentialRepository.findOne(credential.getId());
        cred.setGroup(credential.getGroup());
        cred.setAccount(credential.getAccount());
        cred.setPassword(credential.getPassword());
        cred.setSalt(credential.getSalt());
        cred.setCreateUser(credential.getCreateUser());
        cred.setCreateTimeStamp(credential.getCreateTimeStamp());
        credentialRepository.save(cred);
        //mailer.setMailSender(mailSender);
        try {
            mailer.sendMail("email address here", "Entry added", "An item in your group was updated!");
        }
        catch (Exception e){
            System.out.println("Enter a valid email address");
        }

        return  "redirect:list";
    }




}

