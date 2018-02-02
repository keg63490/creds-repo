package com.example.credsrepo;

import com.example.credsrepo.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
//@RequestMapping(path="/api")
public class CredentialController {

    @Autowired
    private CredentialsRepository credentialRepository;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    String index() { return "home"; }


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

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addCredential(Credential credential) {
        return  "newEntry";
    }
}

