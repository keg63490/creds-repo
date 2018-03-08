package com.example.credsrepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class CredentialController {
    @Autowired
    private CredentialsRepository credentialRepository;



    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    // "default" view. Lists the entries in the db
    @RequestMapping(path="/list", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> groups = getListOfGroups(auth.getAuthorities().toArray());
        System.out.println(groups);

        model.addAttribute("list", credentialRepository.findByGroups(groups));

        return "list";
    }

    // redirects back to the list view
    @RequestMapping(path = "/", method = RequestMethod.GET)
    String index() { return "redirect:list"; }

    // add paths for adding new credential
    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addCredential(Credential credential, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> groups = getListOfGroups(auth.getAuthorities().toArray());
        model.addAttribute("list", groups);

        return  "newEntry";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addCredential(@ModelAttribute("credential") Credential credential, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Timestamp currentTime = entryModificationTime();
        logger.info("New account {} added to group {} by {} on {}.",
                credential.getAccount(), credential.getGroup(), credential.getCreateUser(), currentTime);

        Credential cred = new Credential();
        cred.setGroup(credential.getGroup());
        cred.setAccount(credential.getAccount());
        cred.setPassword(credential.getPassword());
        cred.setSalt(credential.getSalt());
        cred.setCreateUser(auth.getName());
        cred.setCreateTimeStamp(currentTime);

        credentialRepository.save(cred);

        return  "redirect:list";
    }

    // deletes user
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String deleteCredential(@RequestParam("id") Integer id) {
        if (credentialRepository.exists(id)) {
            Credential cred = credentialRepository.findOne(id);
            logger.info("Account {} in group {} was deleted by {} on {}.",
                    cred.getAccount(), cred.getGroup(), cred.getCreateUser(), entryModificationTime());
            credentialRepository.delete(id);
        }
        return "redirect:list";
    }

    // update paths
    @RequestMapping(path = "/update", method = RequestMethod.GET)
    public String updateCredential(Model model, @RequestParam("id") Integer id, Credential credential) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Credential cred = credentialRepository.findOne(id);
        System.out.println(cred.getId());
        model.addAttribute("cred", cred);
        List<String> groups = getListOfGroups(auth.getAuthorities().toArray());
        model.addAttribute("list", groups);
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
        credentialRepository.save(cred);

        logger.info("Account {} in group {} was updated by {} on {}.",
                cred.getAccount(), cred.getGroup(), cred.getCreateUser(), entryModificationTime());

        return  "redirect:list";
    }

    Timestamp entryModificationTime () {
        Date utilDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        cal.set(Calendar.MILLISECOND, 0);

        return new Timestamp((cal.getTimeInMillis()));
    }

    List<String> getListOfGroups(Object[] groups) {
        List<String> returnList = new ArrayList<>();

        for (int i = 0; i < groups.length; i++) {
            String group = groups[i].toString();
            if (group.startsWith("ROLE_")) {
                group = group.substring(5);
            }
            returnList.add(group);
        }

        return returnList;
    }


}

