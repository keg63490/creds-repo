package com.example.credsrepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // "default" view. Lists the entries in the db by querying the DB based on logged in user's group
    // param:
    //      Model model - passes the query result to the view
    @RequestMapping(path="/list", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        model.addAttribute("list", credentialRepository.findByGroups(getListOfGroups(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray())));

        return "list";
    }

    // if no path defined by user, will display the list by routing back to "/list" path
    @RequestMapping(path = "/", method = RequestMethod.GET)
    String index() { return "redirect:list"; }

    /**************************** ADD ROUTE *****************************************/
    // displays a form to add a new credential
    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addCredential(@ModelAttribute("credential") Credential credential, Model model) {
        model.addAttribute("list", getListOfGroups(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()));
        return  "newEntry";
    }

    // posts the new credentital to the database
    // params:
    //      credential -  credential object being passed from view to method
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addCredential(@ModelAttribute("credential") Credential credential) {
        Timestamp currentTime = entryModificationTime();
        logger.info("New account {} added to group {} by {} on {}.",
                credential.getAccount(), credential.getGroup(), credential.getCreateUser(), currentTime);

        Credential cred = new Credential();
        cred.setGroup(credential.getGroup());
        cred.setAccount(credential.getAccount());
        //cred.setPassword(credential.getPassword());
        //cred.setSalt(credential.getSalt());
        try {
            cred.setSalt(Passwords.genNextSalt());
            cred.setPassword(Passwords.encrypt(credential.getPassword(), cred.getSalt()));
        }catch (Exception e){
            System.out.println("Problem with making a password.");
        }
        cred.setCreateUser(SecurityContextHolder.getContext().getAuthentication().getName());
        cred.setCreateTimeStamp(currentTime);
        credentialRepository.save(cred);

        return  "redirect:list";
    }

    /**************************** DELETE ROUTE *****************************************/
    // deletes user
    // params:
    //      id -  id of db entry from view. refers to id of row in db
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

    /**************************** UPDATE ROUTE *****************************************/
    // display form to allow user to update entry based on id
    // params:
    //      model - passes credential model object and groups of authenticated user to the update page
    //      id - id of db row to be updated
    @RequestMapping(path = "/update", method = RequestMethod.GET)
    public String updateCredential(@ModelAttribute("credential") Credential credential, Model model, @RequestParam("id") Integer id) {
        Credential cred = credentialRepository.findOne(id);
        model.addAttribute("cred", cred);
        model.addAttribute("list", getListOfGroups(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()));
        return "update";
    }

    // posts the updated query to the db
    // params:
    //      credential -  holds information from update form to update db with
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateCredential(@ModelAttribute("credential") Credential credential) {
        System.out.println(credential.getGroup());
        Credential cred = credentialRepository.findOne(credential.getId());
        cred.setGroup(credential.getGroup());
        cred.setAccount(credential.getAccount());
        //cred.setPassword(credential.getPassword());
        //cred.setSalt(credential.getSalt());
        cred.setSalt(Passwords.genNextSalt());
        cred.setPassword(Passwords.encrypt(credential.getPassword(), cred.getSalt()));
        //cred.setCreateUser(credential.getCreateUser());
        //cred.setCreateTimeStamp(credential.getCreateTimeStamp()); CHANGE THIS LINE TO REFLECT UPDATE TIME OR JUST REMOVE IT
        credentialRepository.save(cred);

        logger.info("Account {} in group {} was updated by {} on {}.",
                cred.getAccount(), cred.getGroup(), cred.getCreateUser(), entryModificationTime());

        return  "redirect:list";
    }



    /**************************** UTILITY FUNCTIONS *****************************************/
    // get the Java datetimestamp, translate it to SQL compliant datetimestamp, and return  that datetimesamp
    private Timestamp entryModificationTime () {
        Date utilDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        cal.set(Calendar.MILLISECOND, 0);

        return new Timestamp((cal.getTimeInMillis()));
    }

    // translates the groups of the authenticated user into an list of strings for UI display
    private List<String> getListOfGroups(Object[] groups) {
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

