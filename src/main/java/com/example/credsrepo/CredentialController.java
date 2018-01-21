package com.example.credsrepo;

import com.example.credsrepo.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping(path="/api")
public class CredentialController {

    @Autowired
    private CredentialsRepository credentialRepository;

    //@RequestMapping(path = "/addCredential", method = RequestMethod.POST)
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public @ResponseBody String addCredential(@RequestParam  String userGroup, @RequestParam String accountName, @RequestParam String password, @RequestParam String salt, @RequestParam String createUser, @RequestParam int createTimeStamp) {
        Credential cred = new Credential();
        cred.setGroup(userGroup);
        cred.setAccount(accountName);
        cred.setPassword(password);
        cred.setSalt(salt);
        cred.setCreateUser(createUser);
        cred.setCreateTimeStamp(createTimeStamp);
        credentialRepository.save(cred);
        return  "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Credential> getAllUsers() {
        // This returns a JSON or XML with the users
        return credentialRepository.findAll();
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String greeting() {
        return "Hello";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public @ResponseBody String deleteCredential(@RequestParam Integer id) {
        if (credentialRepository.exists(id)) {
            credentialRepository.delete(id);

        }
        return "redirect:/all";
    }
}

